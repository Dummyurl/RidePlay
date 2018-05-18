package rnf.taxiad.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import rnf.taxiad.locations.LocationService;

/**
 * Created by Rahil on 24/2/16.
 */
public class AlarmController {

    public static final String TAG = "rnf.taxiad.TAG_ALARM";
    public static final int ALARM_DOWNLOAD = 0;
    public static final int ALARM_STATS = 1;
    public static final int NOTIFICATION_DOWNLOAD = 3;
    public static final int NOTIFICATION_CANCEL = 4;
    //        private static final int MINUTES = 5;
    private static final int HOURS = 5;
    private Context context;
    private AlarmManager alarmManager;

    public AlarmController(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAll() {
        setAlarmDownload();
        setAlarmStats();
    }

    public void setAlarmDownload() {
        Calendar now = Calendar.getInstance();
        final Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 5);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        if (time.before(now)) {
            time.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(TAG, ALARM_DOWNLOAD);
        final PendingIntent pendingIntent = PendingIntent.getService(context, ALARM_DOWNLOAD, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarmDownload() {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(TAG, ALARM_DOWNLOAD);
        final PendingIntent pendingIntent = PendingIntent.getService(context, ALARM_DOWNLOAD, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.alarmManager.cancel(pendingIntent);
    }

    public void setAlarmStats() {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(TAG, ALARM_STATS);
        final PendingIntent pendingIntent = PendingIntent.getService(context, ALARM_STATS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(HOURS), TimeUnit.MINUTES.toMillis(1), pendingIntent);
    }

    public void cancelAlarmStats() {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(TAG, ALARM_STATS);
        final PendingIntent pendingIntent = PendingIntent.getService(context, ALARM_STATS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.alarmManager.cancel(pendingIntent);
    }

    public void cancellAll() {
        cancelAlarmDownload();
        cancelAlarmStats();
    }
}
