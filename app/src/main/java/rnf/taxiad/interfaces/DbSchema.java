package rnf.taxiad.interfaces;

import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 16/2/16.
 */
public interface DbSchema {

    public static String TABLE_VIDEOS = "videos";
    public static String TABLE_STATS = "stats";
    public static String TABLE_VIDEO_FILE = "videofile";

    public static String CREATE_VIDEO_TABLE =
            "CREATE TABLE " + TABLE_VIDEOS + " (" + ItemsContract.Videos._ID +
                    " INTEGER  PRIMARY KEY AUTOINCREMENT, " + ItemsContract.Videos._AD_ID +
                    " TEXT NOT NULL  UNIQUE , " + ItemsContract.Videos._AD_TITLE +
                    " TEXT, " + ItemsContract.Videos._USER_ID +
                    " TEXT, " + ItemsContract.Videos._AD_URL +
                    " TEXT, " + ItemsContract.Videos._THUMBNAIL_URL +
                    " TEXT, " + ItemsContract.Videos._TAGLINE +
                    " TEXT, " + ItemsContract.Videos._KEYWORD +
                    " TEXT, " + ItemsContract.Videos._ADVERTISER_ID +
                    " TEXT, " + ItemsContract.Videos._ADVERTISER_NAME +
                    " TEXT, " + ItemsContract.Videos._LOCATION +
                    " TEXT, " + ItemsContract.Videos._COUNTRY +
                    " TEXT, " + ItemsContract.Videos._STATE +
                    " TEXT, " + ItemsContract.Videos._COUNTY +
                    " TEXT, " + ItemsContract.Videos._ZIP_CODE +
                    " TEXT, " + ItemsContract.Videos._TYPE +
                    " TEXT, " + ItemsContract.Videos._STATUS +
                    " INTEGER DEFAULT 0, " + ItemsContract.Videos._PRIORITY +
                    " INTEGER DEFAULT 0, " + ItemsContract.Videos._NUMBER + " TEXT " +

                    ")";

    public static String CREATE_STATS_TABLE =
            "CREATE TABLE " + TABLE_STATS + " (" + ItemsContract.Stats._ID +
                    " INTEGER  PRIMARY KEY AUTOINCREMENT, " + ItemsContract.Stats._AD_ID +
                    " TEXT NOT NULL, " + ItemsContract.Stats._USER_ID +
                    " TEXT, " + ItemsContract.Stats.LATITUDE +
                    " TEXT NOT NULL , " + ItemsContract.Stats.LONGITUDE +
                    " TEXT NOT NULL, " + ItemsContract.Stats.CITY +
                    " TEXT, " + ItemsContract.Stats.ADDRESS +
                    " TEXT, " + ItemsContract.Videos._ADVERTISER_ID +
                    " TEXT, " + ItemsContract.Stats.DATE +
                    " TEXT, " + ItemsContract.Stats.KEY +
                    " TEXT, " + ItemsContract.Stats.PLAYED +
                    " INTEGER DEFAULT 0, " + ItemsContract.Stats.VIEWED +
                    " INTEGER DEFAULT 0, " + ItemsContract.Stats.SENT +
                    " INTEGER DEFAULT 0, " + ItemsContract.Stats.DURATION +
                    " INTEGER DEFAULT 0, " + ItemsContract.Stats.TAPED + " INTEGER DEFAULT 0 " +
                    ")";

    public static String CREATE_VIDEO_FILE_TABLE =
            "CREATE TABLE " + TABLE_VIDEO_FILE + " (" + ItemsContract.VideoFile._ID +
                    " INTEGER  PRIMARY KEY AUTOINCREMENT, " + ItemsContract.VideoFile._AD_ID +
                    " TEXT NOT NULL  UNIQUE , " + ItemsContract.VideoFile._AD_URL +
                    " TEXT, " + ItemsContract.VideoFile._TOTAL +
                    " TEXT, " + ItemsContract.VideoFile._DOWNLOADED +
                    " TEXT, " + ItemsContract.VideoFile._PROGRESS + " INTEGER DEFAULT 0 " +
                    ")";

    public static String CREATE_TRIGGER_DELETE_ADS =
            "CREATE TRIGGER delete_videos AFTER INSERT ON videos \n"
                    + "begin\n"
                    + "  delete from videos where _location != old._location;\n"
                    + "end\n";



    public static String ALTER_TABLE_VIDEO_ADD_COUNTRY = "ALTER TABLE " + TABLE_VIDEOS +
            " ADD COLUMN " + ItemsContract.Videos._COUNTRY + " TEXT; ";

    public static String ALTER_TABLE_VIDEO_ADD_STATE = "ALTER TABLE " + TABLE_VIDEOS +
            " ADD COLUMN " +
            ItemsContract.Videos._STATE + " TEXT; ";

    public static String ALTER_TABLE_VIDEO_ADD_COUNTY = "ALTER TABLE " + TABLE_VIDEOS +
            " ADD COLUMN " +
            ItemsContract.Videos._COUNTY + " TEXT; ";

    public static String ALTER_TABLE_VIDEO_ADD_ZIPCODE = "ALTER TABLE " + TABLE_VIDEOS +
            " ADD COLUMN " + ItemsContract.Videos._ZIP_CODE + " TEXT ;";


    /*public static String ALTER_TABLE_STATS = "ALTER TABLE " + TABLE_STATS +
            " ADD COLUMN " + ItemsContract.Videos._COUNTRY + " TEXT," +
            " ADD COLUMN " + ItemsContract.Videos._STATE + " TEXT, " +
            " ADD COLUMN " + ItemsContract.Videos._COUNTY + " TEXT, " +
            " ADD COLUMN " + ItemsContract.Videos._ZIP_CODE + " TEXT ;";*/
}
