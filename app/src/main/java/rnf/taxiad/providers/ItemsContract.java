package rnf.taxiad.providers;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import rnf.taxiad.interfaces.StatsColumn;
import rnf.taxiad.interfaces.VideoFileColumn;
import rnf.taxiad.interfaces.VideosColumn;

/**
 * Created by Rahil on 19/1/16.
 */
public final class ItemsContract implements BaseColumns {

    /**
     * The authority of the items provider.
     */
    public static final String AUTHORITY = "rnf.taxiad.items";
    /**
     * The content URI for the top-level items authority.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    /**
     * A selection clause for ID based queries.
     */
    public static final String SELECTION_ID_BASED = BaseColumns._ID + " = ? ";

    /**
     * Constants for the Items table of the lentitems provider.
     */
    public static final class Videos implements VideosColumn {
        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ItemsContract.CONTENT_URI, "videos");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/rnf.taxiad.items_videos";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/rnf.taxiad.items_videos";
        /**
         * A projection of all columns in the items table.
         */
        public static final String[] PROJECTION_ALL = {_ID, _AD_ID, _AD_TITLE, _USER_ID,
                _AD_URL, _THUMBNAIL_URL, _KEYWORD, _NUMBER, _LOCATION, _ADVERTISER_ID, _ADVERTISER_NAME, _TAGLINE,
                _STATUS, _TYPE,_PRIORITY, _COUNTRY, _STATE, _COUNTY, _ZIP_CODE};

        public static final String[] PROJECTION_VIDEOS = {_AD_URL, _STATUS};
        /**
         * The default sort order for queries containing NAME fields.
         */
        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class Stats implements StatsColumn {

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ItemsContract.CONTENT_URI, "stats");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/rnf.taxiad.items_stats";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/rnf.taxiad.items_stats";
        /**
         * A projection of all columns in the items table.
         */
        public static final String[] PROJECTION_ALL = {_ID, _AD_ID, _USER_ID,
                LATITUDE, LONGITUDE, CITY, ADDRESS, PLAYED, VIEWED, TAPED, DURATION, _ADVERTISER_ID, SENT, DATE,KEY};

        /**
         * The default sort order for queries containing NAME fields.
         */
        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }


    public static final class VideoFile implements VideoFileColumn {

        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ItemsContract.CONTENT_URI, "videofile");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/rnf.taxiad.items_videofile";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/rnf.taxiad.items_videofile";
        /**
         * A projection of all columns in the items table.
         */
        public static final String[] PROJECTION_ALL = {_ID, _AD_ID, _AD_URL, _TOTAL, _DOWNLOADED, _PROGRESS};

        /**
         * The default sort order for queries containing NAME fields.
         */
        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

}
