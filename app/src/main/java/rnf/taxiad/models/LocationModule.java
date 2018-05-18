package rnf.taxiad.models;

import android.location.Address;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import rnf.taxiad.Utility.GlobalUtils;

/**
 * Created by Rahil on 26/2/16.
 */
public class LocationModule implements Serializable {

    private Address currentAddress;
    private Address lastAddress;
    private Location currentLocation;
    private Location lastLocation;
    private Location lastDistanceLocation;
    private boolean startDistance = false;

    public LocationModule() {
        super();
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentAddress(Address currentAddress) {
        setLastAddress(this.currentAddress);
        this.currentAddress = currentAddress;
    }

    public void setCurrentLocation(final Location location) {
        setLastLocation(this.currentLocation);
        this.currentLocation = location;
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                setLastDistanceLocation(location);
            }
        };
        t.start();
    }

    public Address getLastAddress() {
        return lastAddress;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastAddress(Address lastAddress) {
        this.lastAddress = lastAddress;
    }

    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    public String getCurrentCity() {
        if (currentAddress == null)
            return "";
        final String city = currentAddress.getLocality();
        if (city == null)
            return "";
        return city;
    }

    public String getCurrentCountry() {
        if (currentAddress == null)
            return "";
        final String country = currentAddress.getCountryName();
        if (country == null)
            return "";
        return country;
    }

    public String getCurrentZipCode() {
        if (currentAddress == null)
            return "";
        final String zipcode = currentAddress.getPostalCode();
        if (zipcode == null)
            return "";
        return zipcode;
    }

    public String getCurrentState() {
        if (currentAddress == null)
            return "";
        // final String state = currentAddress.getAdminArea();
        final String state = GlobalUtils.getStateAbbrevation(currentAddress.getAdminArea());
        if (state == null)
            return "";
        return state;
    }

    public String getCurrentCounty() {
        if (currentAddress == null)
            return "";
        final String county = currentAddress.getSubAdminArea();
        if (county == null)
            return "";
        return county;
    }


    public String getLastCountry() {
        if (lastAddress == null)
            return "";
        final String country = lastAddress.getCountryName();
        if (country == null)
            return "";
        return country;
    }

    public String getLastZipCode() {
        if (lastAddress == null)
            return "";
        final String zipcode = lastAddress.getPostalCode();
        if (zipcode == null)
            return "";
        return zipcode;
    }

    public String getLastState() {
        if (lastAddress == null)
            return "";
        //  final String state = lastAddress.getAdminArea();
        final String state = GlobalUtils.getStateAbbrevation(lastAddress.getAdminArea());
        if (state == null)
            return "";
        return state;
    }

    public String getLastCounty() {
        if (lastAddress == null)
            return "";
        final String county = lastAddress.getSubAdminArea();
        if (county == null)
            return "";
        return county;
    }


    public String getCountryCode() {
        if (currentAddress == null)
            return "";
        final String countrycode = currentAddress.getCountryCode();
        if (countrycode == null)
            return "";
        return countrycode;
    }


    public String getLastCity() {
        if (lastAddress == null)
            return "";
        final String city = lastAddress.getLocality();
        if (city == null)
            return "";
        return city;
    }

    public String getCurrentAddressLine() {
        if (currentAddress == null || currentAddress.getMaxAddressLineIndex() == 0)
            return "";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentAddress.getMaxAddressLineIndex(); i++) {
            sb.append(sb.length() > 0 ? ", " : "").append(currentAddress.getAddressLine(i));
        }
        return sb.toString();
    }

    public String getLastAddressLine() {
        if (lastAddress == null || lastAddress.getMaxAddressLineIndex() == 0)
            return "";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lastAddress.getMaxAddressLineIndex(); i++) {
            sb.append(sb.length() > 0 ? ", " : "").append(lastAddress.getAddressLine(i));
        }
        return sb.toString();
    }

    public double[] getCurrentLatLong() {
        if (currentAddress == null)
            return null;
        final double latitude = currentAddress.getLatitude();
        final double longitude = currentAddress.getLongitude();
        return new double[]{latitude, longitude};
    }

    public double[] getLasttLatLong() {
        if (lastAddress == null)
            return null;
        final double latitude = lastAddress.getLatitude();
        final double longitude = lastAddress.getLongitude();
        return new double[]{latitude, longitude};
    }

    public synchronized void setLastDistanceLocation(Location location) {
        if (!this.startDistance) {
            this.lastDistanceLocation = null;
            return;
        }
        if (lastDistanceLocation == null) {
            this.lastDistanceLocation = location;
            return;
        }
        synchronized (lastDistanceLocation) {
            try {

                float distanceInMeters = lastDistanceLocation.distanceTo(location);
                double miles = getDistanceCoveredInMiles(distanceInMeters);
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);
                format.setGroupingUsed(false);
                miles = Double.valueOf(format.format(miles));
                if (distanceInMeters >= 100) {
                    this.lastDistanceLocation = location;
                    DriverSession.get().getOverallStats().addDistance(miles);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Location getLastDistanceLocation() {
        return lastDistanceLocation;
    }

    public synchronized double getDistanceCoveredInMiles(float distanceCovered) {
        return (distanceCovered * 0.000621371);
    }

    public void startDistance() {
        this.startDistance = true;
    }

    public void stopDistance() {
        this.startDistance = false;
    }
}
