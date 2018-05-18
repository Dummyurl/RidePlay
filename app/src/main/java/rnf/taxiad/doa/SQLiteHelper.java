package rnf.taxiad.doa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rnf.taxiad.interfaces.DbSchema;

/**
 * Created by Rahil on 14/12/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String NAME = "TaxiApp.db";

    public SQLiteHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbSchema.CREATE_VIDEO_TABLE);
        db.execSQL(DbSchema.CREATE_STATS_TABLE);
        db.execSQL(DbSchema.CREATE_VIDEO_FILE_TABLE);
//        db.execSQL(DbSchema.CREATE_TRIGGER_DELETE_ADS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DbSchema.ALTER_TABLE_VIDEO_ADD_COUNTRY);
            db.execSQL(DbSchema.ALTER_TABLE_VIDEO_ADD_STATE);
            db.execSQL(DbSchema.ALTER_TABLE_VIDEO_ADD_COUNTY);
            db.execSQL(DbSchema.ALTER_TABLE_VIDEO_ADD_ZIPCODE);
        }

    }
}
