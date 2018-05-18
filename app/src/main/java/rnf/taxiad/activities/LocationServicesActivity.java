package rnf.taxiad.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import rnf.taxiad.helpers.InsertStatsService;
import rnf.taxiad.interfaces.LocationBroadcaster;
import rnf.taxiad.locations.LocationService;
import rnf.taxiad.locations.LocationUpdater;
import rnf.taxiad.models.LocationModule;

/**
 * Created by Rahil on 15/12/15.
 */
public class LocationServicesActivity extends BaseActivity
        implements ResultCallback<LocationSettingsResult>, LocationBroadcaster {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationService.LocalBinder binder;
    protected LocationServiceConnection connection = new LocationServiceConnection();
    protected boolean isBound = false;
    private LocationUpdater locationUpdater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBound) {
            bindService(new Intent(this, LocationService.class), connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            binder.stopDistance();
            unbindService(connection);
            isBound = false;
        }
    }


    private class LocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (LocationService.LocalBinder) service;
            if (locationUpdater == null)
                createLocationRequestSettings();
            binder.addLocationChangedListener(LocationServicesActivity.this);
            binder.connect();
            isBound = true;
            binder.startDistance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    }

    private void createLocationRequestSettings() {
        if (binder == null)
            return;
        locationUpdater = binder.getLocationUpdater();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationUpdater.getLocationRequest());
        LocationSettingsRequest request = builder.build();
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        locationUpdater.getGoogleApiClient(),
                        request
                );
        result.setResultCallback(this);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(LocationServicesActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }


    @Override
    public void onLocationChanged(LocationModule locationModule) {

    }

    @Override
    public void onRequestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION}, 0x12);
    }

    public void enterStats(String adId, int played, int viewed, int taped, int duration, String advertiserId) {
        Intent i = new Intent(this, InsertStatsService.class);
        i.putExtra(InsertStatsService.ADD_ID, adId);
        i.putExtra(InsertStatsService.PLAYED, played);
        i.putExtra(InsertStatsService.VIEWED, viewed);
        i.putExtra(InsertStatsService.TAPED, taped);
        i.putExtra(InsertStatsService.DURATION, duration);
        i.putExtra(InsertStatsService.ADVERTISER_ID, advertiserId);
        startService(i);
    }

    public void startDownload() {
        if (binder != null)
            binder.startVideoDownloading();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0x12) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (binder != null)
                    binder.startLocationUpdate();
            }
        }
    }
}
