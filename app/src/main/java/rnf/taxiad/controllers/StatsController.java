package rnf.taxiad.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rnf.taxiad.models.DriverSession;
import rnf.taxiad.models.Stats;
import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 7/3/16.
 */
public class StatsController {

    private Context context;

    public StatsController(Context context) {
        this.context = context;
    }

    public synchronized void insertStats(final Stats stats) {
        final ContentValues cv = new ContentValues();
        cv.put(ItemsContract.Stats._USER_ID, DriverSession.get().getDriver().userID);
        cv.put(ItemsContract.Stats.LATITUDE, stats.latitude);
        cv.put(ItemsContract.Stats.LONGITUDE, stats.longitude);
        cv.put(ItemsContract.Stats.CITY, stats.city);
        cv.put(ItemsContract.Stats.ADDRESS, stats.address);
        cv.put(ItemsContract.Stats._ADVERTISER_ID, stats.advertiserId);
        cv.put(ItemsContract.Stats.PLAYED, stats.played);
        cv.put(ItemsContract.Stats.VIEWED, stats.viewed);
        cv.put(ItemsContract.Stats.DURATION, stats.duration);
        cv.put(ItemsContract.Stats.TAPED, stats.taped);
        cv.put(ItemsContract.Stats.DATE, stats.date);
        cv.put(ItemsContract.Stats.KEY, stats.key);
        final Stats s = get(stats.adId, stats.key);
        if (s == null) {
            cv.put(ItemsContract.Stats._AD_ID, stats.adId);
            context.getContentResolver().insert(ItemsContract.Stats.CONTENT_URI, cv);
            Toast.makeText(context,""+stats.played,Toast.LENGTH_LONG).show();
        } else {
            int played = Integer.valueOf(s.played) + Integer.valueOf(stats.played);
            int viewed = Integer.valueOf(s.viewed) + Integer.valueOf(stats.viewed);
            int taped = Integer.valueOf(s.taped) + Integer.valueOf(stats.taped);
            int duration = Integer.valueOf(s.duration) + Integer.valueOf(stats.duration);
            cv.put(ItemsContract.Stats.PLAYED, played);
            cv.put(ItemsContract.Stats.VIEWED, viewed);
            cv.put(ItemsContract.Stats.DURATION, duration);
            cv.put(ItemsContract.Stats.TAPED, taped);
            cv.put(ItemsContract.Stats.DATE, stats.date);
            String whereStr = ItemsContract.Stats._AD_ID + " =? AND " + ItemsContract.Stats.KEY + " =?";
            context.getContentResolver().update(ItemsContract.Stats.CONTENT_URI, cv, whereStr, new String[]{stats.adId, stats.key});
            Toast.makeText(context,""+played,Toast.LENGTH_LONG).show();
            //            readupdate(stats);
        }
    }

    public boolean readupdate(final Stats stats) {
        ContentValues cv = new ContentValues();
        cv.put(ItemsContract.Stats.PLAYED, 0);
        cv.put(ItemsContract.Stats.PLAYED, 0);
        cv.put(ItemsContract.Stats.VIEWED, 0);
        cv.put(ItemsContract.Stats.DURATION, 0);
        cv.put(ItemsContract.Stats.TAPED, 0);
        cv.put(ItemsContract.Stats.DATE, stats.date);
        String whereStr = ItemsContract.Stats._AD_ID + " =? AND " + ItemsContract.Stats.KEY + " =?";
        context.getContentResolver().update(ItemsContract.Stats.CONTENT_URI, cv, whereStr, new String[]{stats.adId, stats.key});


        return true;
    }

    public List<Stats> getAll() {
        List<Stats> stats = new ArrayList<>();
        final Cursor c = context.getContentResolver().query(ItemsContract.Stats.CONTENT_URI,
                ItemsContract.Stats.PROJECTION_ALL, null, null, null);
        if (c == null || c.getCount() == 0)
            return stats;
        while (c.moveToNext()) {
            final Stats s = new Stats(c);
            stats.add(s);
        }
        return stats;
    }

    public List<Stats> getStatsByDate(String date) {
        List<Stats> stats = new ArrayList<>();
        List<Stats> all = getAll();
        for (Stats s : all
                ) {
            if (s.date.equals(date))
                stats.add(s);
        }
        return stats;
    }

    public Stats get(String id) {
        String whereStr = ItemsContract.Stats._AD_ID + " = " + id;
        final Cursor c = context.getContentResolver().query(ItemsContract.Stats.CONTENT_URI,
                ItemsContract.Stats.PROJECTION_ALL, whereStr, null, null);
        if (c == null || c.getCount() == 0)
            return null;
        c.moveToPosition(0);
        final Stats s = new Stats(c);
        return s;
    }

    public Stats get(String id, String key) {
        String whereStr = ItemsContract.Stats._AD_ID + " =? AND " + ItemsContract.Stats.KEY + " =?";
        final Cursor c = context.getContentResolver().query(ItemsContract.Stats.CONTENT_URI,
                ItemsContract.Stats.PROJECTION_ALL, whereStr, new String[]{id, key}, null);
        if (c == null || c.getCount() == 0)
            return null;
        c.moveToPosition(0);
        final Stats s = new Stats(c);
        return s;
    }

    public synchronized void deleteFromStats(ArrayList<String> ids) {
        for (String id : ids) {
            String whereStr = ItemsContract.Stats._ID + " =?";
            context.getContentResolver().delete(ItemsContract.Stats.CONTENT_URI, whereStr, new String[]{id});
        }
    }

    public synchronized void updateStats(ArrayList<String> ids) {
        for (String id : ids) {
            String whereStr = ItemsContract.Stats._ID + " = " + id;
            ContentValues cv = new ContentValues();
            cv.put(ItemsContract.Stats.SENT, 1);
            context.getContentResolver().update(ItemsContract.Stats.CONTENT_URI, cv, whereStr, null);
        }
    }

}
