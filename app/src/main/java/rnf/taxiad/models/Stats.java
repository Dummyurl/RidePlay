package rnf.taxiad.models;

import android.database.Cursor;

import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 7/3/16.
 */
public class Stats {

    public String id;
    public String adId;
    public String userId;
    public String latitude;
    public String longitude;
    public String city;
    public String address;
    public String advertiserId;
    public String played;
    public String viewed;
    public String duration;
    public String taped;
    public String date;
    public String key;

    public Stats() {
        super();
    }

    public Stats(Cursor c) {
        super();
        init(c);
    }

    private void init(Cursor c) {
        id = c.getString(c.getColumnIndex(ItemsContract.Stats._ID));
        adId = c.getString(c.getColumnIndex(ItemsContract.Stats._AD_ID));
        userId = c.getString(c.getColumnIndex(ItemsContract.Stats._USER_ID));
        latitude = c.getString(c.getColumnIndex(ItemsContract.Stats.LATITUDE));
        longitude = c.getString(c.getColumnIndex(ItemsContract.Stats.LONGITUDE));
        city = c.getString(c.getColumnIndex(ItemsContract.Stats.CITY));
        address = c.getString(c.getColumnIndex(ItemsContract.Stats.ADDRESS));
        advertiserId = c.getString(c.getColumnIndex(ItemsContract.Stats._ADVERTISER_ID));
        played = c.getString(c.getColumnIndex(ItemsContract.Stats.PLAYED));
        viewed = c.getString(c.getColumnIndex(ItemsContract.Stats.VIEWED));
        duration = c.getString(c.getColumnIndex(ItemsContract.Stats.DURATION));
        taped = c.getString(c.getColumnIndex(ItemsContract.Stats.TAPED));
        date = c.getString(c.getColumnIndex(ItemsContract.Stats.DATE));
        key = c.getString(c.getColumnIndex(ItemsContract.Stats.KEY));
    }
}
