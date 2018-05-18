package rnf.taxiad.models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.helpers.SendDownloadStats;

/**
 * Created by php-android02 on 29/2/16.
 */
public class OverallStats implements Serializable {

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private LinkedHashMap<String, Statistics> statistics = new LinkedHashMap<>();

    public OverallStats(Context c) {
        super();
        this.context = c;
        parse(AppPreferenceManager.getInstance(c).getString(TAG, null));
    }

    public synchronized void addDistance(double distanceCovered) {
        if (distanceCovered == 0)
            return;
        String date = GlobalUtils.getCurrentDate();
        String key = GlobalUtils.formatTimestamp(System.currentTimeMillis(), "yyyy-MM-dd");
        Statistics stats = statistics.get(key);
        if (stats == null) {
            stats = new Statistics();
            stats.distanceCovered = distanceCovered;
        } else
            stats.distanceCovered += distanceCovered;
        stats.date = date;
        stats.key = key;
        statistics.put(key, stats);
        save();
    }


    public synchronized void addBytes(long downloadedBytes) {
        if (downloadedBytes == 0)
            return;
        String date = GlobalUtils.getCurrentDate();
        String key = GlobalUtils.formatTimestamp(System.currentTimeMillis(), "yyyy-MM-dd");
        Statistics stats = statistics.get(key);
        if (stats == null) {
            stats = new Statistics();
            stats.downloadedBytes = downloadedBytes;
        } else
            stats.downloadedBytes += downloadedBytes;
        stats.date = date;
        stats.key = key;
        statistics.put(key, stats);
        save();
    }

    public synchronized void setDownloaded(boolean downloaded) {
        String date = GlobalUtils.getCurrentDate();
        String key = GlobalUtils.formatTimestamp(System.currentTimeMillis(), "yyyy-MM-dd");
        Statistics stats = statistics.get(key);
        if (stats == null) {
            stats = new Statistics();
            stats.downloaded = downloaded;
        } else
            stats.downloaded = downloaded;
        stats.date = date;
        stats.key = key;
        statistics.put(key, stats);
        save();
    }

    public synchronized Statistics getCurrent() {
        String key = GlobalUtils.formatTimestamp(System.currentTimeMillis(), "yyyy-MM-dd");
        Statistics stats = statistics.get(key);
        if (stats == null) {
            Statistics statistics = new Statistics();
            statistics.date = GlobalUtils.getCurrentDate();
            statistics.key = key;
            this.statistics.put(statistics.key, statistics);
        }
        return stats;
    }

    private void parse(String json) {
        if (json == null)
            return;
        try {
            final JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                final Statistics stats = new Statistics(obj);
                this.statistics.put(stats.key, stats);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String toJSON() {
        final JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Statistics> stats : statistics.entrySet()) {
            jsonArray.put(stats.getValue().toJson());
        }
        return jsonArray.toString();
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public synchronized void save() {
        AppPreferenceManager.getInstance(context).saveString(TAG, toString());
    }

    public void setSendOverallStats() {
        for (Map.Entry<String, Statistics> stats : statistics.entrySet()) {
            final SendDownloadStats sendOverallStats = new SendDownloadStats(context, this);
            sendOverallStats.addStats(stats.getValue());
            sendOverallStats.makeRequest();
        }
    }

    public void clear() {
        statistics.clear();
        save();
    }

    public void clear(String date) {
        statistics.remove(date);
        save();
    }

    public static class Statistics {

        public double distanceCovered = 0.0;
        public long downloadedBytes = 0L;
        public boolean downloaded = false;
        public String date;
        public String key;

        public Statistics() {
            super();
        }


        public Statistics(JSONObject json) {
            super();
            parse(json);
        }

        private void parse(JSONObject json) {
            if (json == null)
                return;
            try {
                distanceCovered = json.getDouble("distanceCovered");
                downloadedBytes = json.getLong("downloadedBytes");
                downloaded = json.getBoolean("downloaded");
                date = json.getString("date");
                key = json.getString("key");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public JSONObject toJson() {
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("distanceCovered", distanceCovered);
                jsonObject.put("downloadedBytes", downloadedBytes);
                jsonObject.put("downloaded", downloaded);
                jsonObject.put("date", date);
                jsonObject.put("key", key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        public String toString() {
            return toJson().toString();
        }

        public synchronized double getDistanceCoveredInMiles() {
            return distanceCovered;
        }
    }
}
