package rnf.taxiad.locations;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Rahil on 14/12/15.
 */
public class LocationUpdater implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private int INTERVAL;
    private int FATEST_INTERVAL;
    //    private int MIN_DISTANCE;
    private int PRIORITY;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private rnf.taxiad.locations.LocationListener mLocationChangedListener;

    private LocationUpdater(Builder builder) {
        super();
        this.context = builder.context;
        this.INTERVAL = builder.INTERVAL;
        this.FATEST_INTERVAL = builder.FATEST_INTERVAL;
//        this.MIN_DISTANCE = builder.MIN_DISTANCE;
        this.PRIORITY = builder.PRIORITY;
        this.mLocationChangedListener = builder.locationListener;
        buildGoogleApiClient();
        createLocationRequest();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(PRIORITY);
    }


    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (mLocationChangedListener != null)
                mLocationChangedListener.onRequestPermission();
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public LocationRequest getLocationRequest() {
        return mLocationRequest;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocationChangedListener != null && mLastLocation != null)
            mLocationChangedListener.onLocationChanged(mLastLocation, false);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

//        Toast.makeText(context, "Connection Suspended: " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

//        Toast.makeText(context, "Connection Failed: " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mLocationChangedListener != null && mLastLocation != null)
            mLocationChangedListener.onLocationChanged(mLastLocation, true);
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public static class Builder {
        private int INTERVAL = 10000;
        private int FATEST_INTERVAL = 5000;
        //        private int MIN_DISTANCE = 1;
        private int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
        private rnf.taxiad.locations.LocationListener locationListener;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder interval(int intervalInSeconds) {
            this.INTERVAL = intervalInSeconds;
            return this;
        }

        public Builder fatestInterval(int fatestIntervalInSeconds) {
            this.FATEST_INTERVAL = fatestIntervalInSeconds;
            return this;
        }

//        public Builder minDistance(int minDistanceInMeters) {
//            this.MIN_DISTANCE = minDistanceInMeters;
//            return this;
//        }

        public Builder priority(int priority) {
            this.PRIORITY = priority;
            return this;
        }

        public Builder locationListener(rnf.taxiad.locations.LocationListener locationListener) {
            this.locationListener = locationListener;
            return this;
        }

        public LocationUpdater build() {
            return new LocationUpdater(this);
        }


    }
}
