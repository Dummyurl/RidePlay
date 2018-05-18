package rnf.taxiad.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.NotificationCompat;

import rnf.taxiad.R;
import rnf.taxiad.locations.LocationService;

/**
 * Created by Rahil on 24/2/16.
 */
public class NotificationController {

    private Context context;
    private NotificationManager notificationManager;
    private int id = 0077;

    public NotificationController(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showDownloadNotification() {
//        if (isConnectedToWifi())
//            return;
        final Intent intentDownload = new Intent(context, LocationService.class);
        intentDownload.putExtra(AlarmController.TAG, AlarmController.NOTIFICATION_DOWNLOAD);
        final PendingIntent pendingIntentDownload = PendingIntent.getService(context,
                AlarmController.NOTIFICATION_DOWNLOAD, intentDownload, PendingIntent.FLAG_UPDATE_CURRENT);
        final Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(AlarmController.TAG, AlarmController.NOTIFICATION_CANCEL);
        final PendingIntent pendingIntent = PendingIntent.getService(context,
                AlarmController.NOTIFICATION_CANCEL, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setCategory(Notification.CATEGORY_REMINDER);
        builder.setSmallIcon(R.mipmap.ic_app);
        builder.setTicker(context.getString(R.string.notification_ticker));
        builder.setContentTitle(context.getString(R.string.notification_title));
        builder.setContentText(context.getString(R.string.notification_subject));
        builder.addAction(0, "Download", pendingIntentDownload);
        builder.addAction(0, "Cancel", pendingIntent);
        builder.setAutoCancel(false);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(id, notification);
    }

    public void cancel() {
        notificationManager.cancel(id);
    }

    public boolean isConnectedToWifi() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connManager.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) return true;
        return false;
    }

}
