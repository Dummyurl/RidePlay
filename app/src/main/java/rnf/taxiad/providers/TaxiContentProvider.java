package rnf.taxiad.providers;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;

import rnf.taxiad.doa.DatabaseHelper;
import rnf.taxiad.interfaces.DbSchema;

/**
 * Created by Rahil on 19/1/16.
 */
public class TaxiContentProvider extends ContentProvider {

    public static final int ITEM_LIST = 1;
    public static final int ITEM_ID = 2;
    public static final int STATS_LIST = 3;
    public static final int STATS_ID = 4;
    public static final int VIDEO_LIST = 5;
    public static final int VIDEO_ID = 6;
    public static final UriMatcher URI_MATCHER;
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>();
    private DatabaseHelper helper;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ItemsContract.AUTHORITY, "videos", ITEM_LIST);
        URI_MATCHER.addURI(ItemsContract.AUTHORITY, "videos/#", ITEM_ID);
        URI_MATCHER.addURI(ItemsContract.AUTHORITY, "stats", STATS_LIST);
        URI_MATCHER.addURI(ItemsContract.AUTHORITY, "stats/#", STATS_ID);
        URI_MATCHER.addURI(ItemsContract.AUTHORITY, "videofile", VIDEO_LIST);
        URI_MATCHER.addURI(ItemsContract.AUTHORITY, "videofile/#", VIDEO_ID);
    }

    @Override
    public boolean onCreate() {
        helper = DatabaseHelper.get(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getDatabase(false);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        boolean useAuthorityUri = false;
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                builder.setTables(DbSchema.TABLE_VIDEOS);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ItemsContract.Videos.SORT_ORDER_DEFAULT;
                }
                break;
            case ITEM_ID:
                builder.setTables(DbSchema.TABLE_VIDEOS);
                // limit query to one row at most:
                builder.appendWhere(ItemsContract.Videos._ID + " = "
                        + uri.getLastPathSegment());
                break;
            case STATS_LIST:
                builder.setTables(DbSchema.TABLE_STATS);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ItemsContract.Stats.SORT_ORDER_DEFAULT;
                }
                break;
            case STATS_ID:
                builder.setTables(DbSchema.TABLE_STATS);
                // limit query to one row at most:
                builder.appendWhere(ItemsContract.Stats._ID + " = "
                        + uri.getLastPathSegment());
            case VIDEO_LIST:
                builder.setTables(DbSchema.TABLE_VIDEO_FILE);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ItemsContract.Stats.SORT_ORDER_DEFAULT;
                }
                break;
            case VIDEO_ID:
                builder.setTables(DbSchema.TABLE_VIDEO_FILE);
                // limit query to one row at most:
                builder.appendWhere(ItemsContract.VideoFile._ID + " = "
                        + uri.getLastPathSegment());
                break;

        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        // if we want to be notified of any changes:
        if (useAuthorityUri) {
            cursor.setNotificationUri(getContext().getContentResolver(), ItemsContract.CONTENT_URI);
        } else {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getDatabase(true);
        if (URI_MATCHER.match(uri) == ITEM_LIST) {
            long id = db.insert(DbSchema.TABLE_VIDEOS, null, values);
            return getUriForId(id, uri);
        } else if (URI_MATCHER.match(uri) == STATS_LIST) {
            long id = db.insert(DbSchema.TABLE_STATS, null, values);
            return getUriForId(id, uri);
        } else if (URI_MATCHER.match(uri) == VIDEO_LIST) {
            long id = db.insert(DbSchema.TABLE_VIDEO_FILE, null, values);
            return getUriForId(id, uri);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported URI for insertion: " + uri);
//            // this insertWithOnConflict is a special case; CONFLICT_REPLACE
//            // means that an existing entry which violates the UNIQUE constraint
//            // on the item_id column gets deleted. That is this INSERT behaves
//            // nearly like an UPDATE. Though the new row has a new primary key.
//            // See how I mentioned this in the Contract class.
//            long id = db.insertWithOnConflict(DbSchema.TABLE_VIDEOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//            return getUriForId(id, uri);
        }
    }

    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase db = helper.getDatabase(true);
        mIsInBatchMode.set(true);
        // the next line works because SQLiteDatabase
        // uses a thread local SQLiteSession object for
        // all manipulations
        db.beginTransaction();
        try {
            final ContentProviderResult[] retResult = super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(ItemsContract.Videos.CONTENT_URI, null);
            return retResult;
        } finally {
            mIsInBatchMode.remove();
            db.endTransaction();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getDatabase(true);
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                delCount = db.delete(DbSchema.TABLE_VIDEOS, selection, selectionArgs);
                break;
            case ITEM_ID:
                String idStr = uri.getLastPathSegment();
                String where = ItemsContract.Videos._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                delCount = db.delete(DbSchema.TABLE_VIDEOS, where, selectionArgs);
                break;
            case STATS_LIST:
                delCount = db.delete(DbSchema.TABLE_STATS, selection, selectionArgs);
                break;
            case STATS_ID:
                String id = uri.getLastPathSegment();
                String whereStr = ItemsContract.Videos._ID + " = " + id;
                if (!TextUtils.isEmpty(selection)) {
                    whereStr += " AND " + selection;
                }
                delCount = db.delete(DbSchema.TABLE_STATS, whereStr, selectionArgs);
                break;
            case VIDEO_LIST:
                delCount = db.delete(DbSchema.TABLE_VIDEO_FILE, selection, selectionArgs);
                break;
            case VIDEO_ID:
                String idVideo = uri.getLastPathSegment();
                String whereStrVideo = ItemsContract.Videos._ID + " = " + idVideo;
                if (!TextUtils.isEmpty(selection)) {
                    whereStrVideo += " AND " + selection;
                }
                delCount = db.delete(DbSchema.TABLE_VIDEO_FILE, whereStrVideo, selectionArgs);
                break;
        }
        // notify all listeners of changes:
        if (delCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getDatabase(true);
        int updateCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                updateCount = db.update(DbSchema.TABLE_VIDEOS, values, selection,
                        selectionArgs);
                break;
            case ITEM_ID:
                String idStr = uri.getLastPathSegment();
                String where = ItemsContract.Videos._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                updateCount = db.update(DbSchema.TABLE_VIDEOS, values, where,
                        selectionArgs);
                break;
            case STATS_LIST:
                updateCount = db.update(DbSchema.TABLE_STATS, values, selection,
                        selectionArgs);
                break;
            case STATS_ID:
                String id = uri.getLastPathSegment();
                String whereStr = ItemsContract.Videos._ID + " = " + id;
                if (!TextUtils.isEmpty(selection)) {
                    whereStr += " AND " + selection;
                }
                updateCount = db.update(DbSchema.TABLE_STATS, values, whereStr,
                        selectionArgs);
                break;
            case VIDEO_LIST:
                updateCount = db.update(DbSchema.TABLE_VIDEO_FILE, values, selection,
                        selectionArgs);
                break;
            case VIDEO_ID:
                String idVideo = uri.getLastPathSegment();
                String whereStrVideo = ItemsContract.Videos._ID + " = " + idVideo;
                if (!TextUtils.isEmpty(selection)) {
                    whereStrVideo += " AND " + selection;
                }
                updateCount = db.update(DbSchema.TABLE_VIDEO_FILE, values, whereStrVideo,
                        selectionArgs);
                break;

        }
        // notify all listeners of changes:
        if (updateCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                return ItemsContract.Videos.CONTENT_TYPE;
            case ITEM_ID:
                return ItemsContract.Videos.CONTENT_ITEM_TYPE;
            case STATS_LIST:
                return ItemsContract.Stats.CONTENT_TYPE;
            case STATS_ID:
                return ItemsContract.Stats.CONTENT_ITEM_TYPE;
            case VIDEO_LIST:
                return ItemsContract.Stats.CONTENT_TYPE;
            case VIDEO_ID:
                return ItemsContract.Stats.CONTENT_ITEM_TYPE;
        }
        return "";
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                // notify all listeners of changes and return itemUri:
                getContext().
                        getContentResolver().
                        notifyChange(itemUri, null);
            }
            return itemUri;
        }
        // s.th. went wrong:
        throw new SQLException("Problem while inserting into uri: " + uri);
    }

    private boolean isInBatchMode() {
        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }

}
