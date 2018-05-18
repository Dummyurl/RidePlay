package rnf.taxiad.locations;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import rnf.taxiad.controllers.AlarmController;
import rnf.taxiad.controllers.DataController;
import rnf.taxiad.controllers.NotificationController;
import rnf.taxiad.controllers.VideoDownloader;
import rnf.taxiad.interfaces.LocationBroadcaster;
import rnf.taxiad.models.Ads;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 15/12/15.
 */
public class LocationService extends Service implements LocationListener {

    private Object locationObject = new Object();
    private LocationUpdater locationUpdater;
    private final LocalBinder binder = new LocalBinder();
    private WeakReference<LocationBroadcaster> locationBroadcasterWeakReference;
    private VideoDownloader videoDownloadController;
    private NotificationController notificationController;
    private AlarmController alarmController;
    String fromActivity = "no";

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (locationObject) {
            locationUpdater = new LocationUpdater.Builder(this).
                    locationListener(this).build();
            videoDownloadController = new VideoDownloader(this);
            notificationController = new NotificationController(this);
            this.getContentResolver().registerContentObserver(ItemsContract.Videos.CONTENT_URI, true, observer);
            alarmController = new AlarmController(this);
            alarmController.setAll();
        }
        Log.d("RIDE","insdie service onCreate --> ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("RIDE","insdie service onStartCommand --> ");
        fromActivity= intent.getStringExtra("fromHome");
        if (intent != null) {
            int TAG = intent.getIntExtra(AlarmController.TAG, -1);
            switch (TAG) {
                case AlarmController.ALARM_DOWNLOAD:
                    showExplicitDownload();
                    break;

                case AlarmController.ALARM_STATS:
//                    sendAdsStats();
//                    sendDownloadStats();
                    break;

                case AlarmController.NOTIFICATION_DOWNLOAD:
                    startDownloading();
                    break;

                case AlarmController.NOTIFICATION_CANCEL:
                    downloadCancelled();
                    break;
            }
            sendAdsStats();
            sendDownloadStats();
            Toast.makeText(LocationService.this,"call",Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onLocationChanged(Location location, boolean current) {
        Log.d("RIDE","inside LocationService onLocationChanged ---> ");
//        if (!DriverSession.get().isHasSession())
//            return;
        try {
            DataController.getInstance(this).onLocationChanged(location,fromActivity);
            if (locationBroadcasterWeakReference != null && locationBroadcasterWeakReference.get() != null) {
                locationBroadcasterWeakReference.get().onLocationChanged(DataController.getInstance(this).getLocationModule());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermission() {
        try {
            if (locationBroadcasterWeakReference != null && locationBroadcasterWeakReference.get() != null) {
                locationBroadcasterWeakReference.get().onRequestPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {


        public LocationUpdater getLocationUpdater() {
            return locationUpdater;
        }

        public void addLocationChangedListener(LocationBroadcaster locationBroadcaster) {
            locationBroadcasterWeakReference = new WeakReference<LocationBroadcaster>(locationBroadcaster);
        }

        public void startVideoDownloading() {
            startDownloadingVideos();
        }

        public void startLocationUpdate() {
            locationUpdater.startLocationUpdates();
        }

        public void connect() {
            locationUpdater.connect();
        }

        public void startDistance(){
            DataController.getInstance(LocationService.this).startDistance();
        }

        public void stopDistance(){
            DataController.getInstance(LocationService.this).stopDistance();
        }
    }

    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            startDownloadingVideos();
        }
    };

    public void startDownloadingVideos() {
        final List<Ads> ads = DataController.getInstance(getApplicationContext()).
                getAdsController().getAdsToDownload();
        videoDownloadController.download(ads);
    }

    private void showExplicitDownload() {
        if (!DriverSession.get().isHasSession())
            return;
        notificationController.showDownloadNotification();
    }

    private void downloadCancelled() {
        notificationController.cancel();
        DriverSession.get().getOverallStats().setDownloaded(false);
    }

    private void startDownloading() {
        if (!DriverSession.get().isHasSession())
            return;
        notificationController.cancel();
        DataController.getInstance(this).getAds();
    }

    private void sendDownloadStats() {
        if (!DriverSession.get().isHasSession())
            return;
        DriverSession.get().getOverallStats().setSendOverallStats();
    }

    private void sendAdsStats() {
        if (!DriverSession.get().isHasSession())
            return;
        DataController.getInstance(this).sendStats();
    }


}
