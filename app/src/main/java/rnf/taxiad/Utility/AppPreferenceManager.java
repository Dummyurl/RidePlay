package rnf.taxiad.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Rahil on 14/10/15.
 */
public class AppPreferenceManager {

    private static AppPreferenceManager instance;
    private SharedPreferences pref;

    public static AppPreferenceManager getInstance(Context context) {
        if (instance == null)
            instance = new AppPreferenceManager(context);

        return instance;
    }

    private AppPreferenceManager(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveString(String key, String value) {
        pref.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public void saveLong(String key, long value) {
        pref.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public void clear() {
        pref.edit().clear().commit();
    }
}
