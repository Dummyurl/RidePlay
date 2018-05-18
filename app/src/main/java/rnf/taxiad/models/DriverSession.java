package rnf.taxiad.models;

import android.content.Context;
import android.content.Intent;

import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.controllers.AlarmController;
import rnf.taxiad.controllers.FileUtils;
import rnf.taxiad.locations.LocationService;
import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 18/1/16.
 */
public class DriverSession {

    private final String TAG = this.getClass().getSimpleName();
    private static DriverSession session = null;
    private Driver driver = new Driver();
    private boolean hasSession = false;
    private static Context context;
    private OverallStats overallStats;


    public synchronized static void init(Context c) {
        context = c;
    }

    public synchronized static DriverSession get() {
        if (session == null)
            session = new DriverSession();
        return session;
    }

    private DriverSession() {
        super();
        if (context == null)
            throw new NullPointerException("The class must be initialized first with init() method.");
        overallStats = new OverallStats(context);
        final String json = AppPreferenceManager.getInstance(context).getString(TAG, null);
        driver.parse(json);
        hasSession = driver.token == null ? false : true;
    }

    public void createSession(String json) {
        driver.parse(json);
        hasSession = driver.token == null ? false : true;
        if (isHasSession())
            save();
    }

    public void removeSession() {
        deleteAllTable();
        context.stopService(new Intent(context, LocationService.class));
        AppPreferenceManager.getInstance(context).clear();
        AlarmController alarmController = new AlarmController(context);
        alarmController.cancellAll();
        overallStats.clear();
        session = null;
    }

    private void deleteAllTable() {
        context.getContentResolver().delete(ItemsContract.Videos.CONTENT_URI, null, null);
        context.getContentResolver().delete(ItemsContract.Stats.CONTENT_URI, null, null);
        context.getContentResolver().delete(ItemsContract.VideoFile.CONTENT_URI, null, null);
        FileUtils fileUtils = new FileUtils(context);
        fileUtils.deleteAll();
    }

    public Driver getDriver() {
        return driver;
    }

    public OverallStats getOverallStats() {
        return overallStats;
    }

    private void save() {
        AppPreferenceManager.getInstance(context).saveString(TAG, driver.toString());
    }

    public boolean isHasSession() {
        return hasSession;
    }

    public void update(Driver d) {
        driver.fname = d.fname;
        driver.lname = d.lname;
        driver.phone = d.phone;
        driver.lat = d.lat;
        driver.lng = d.lng;
        driver.referralCode = d.referralCode;
        driver.paypalId = d.paypalId;
        driver.tablet = d.tablet;
        driver.tabletSize = d.tabletSize;
        driver.city = d.city;
        driver.interest = d.interest;
        driver.comments = d.comments;
        driver.services = d.services;
        save();
    }

}
