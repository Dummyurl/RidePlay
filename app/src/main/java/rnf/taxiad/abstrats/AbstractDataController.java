package rnf.taxiad.abstrats;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.controllers.AdsController;
import rnf.taxiad.helpers.GetAdsListClient;
import rnf.taxiad.helpers.SendAdsStatsClient;
import rnf.taxiad.interfaces.OnAdsReceived;
import rnf.taxiad.models.Ads;
import rnf.taxiad.models.LocationAddress;
import rnf.taxiad.models.LocationModule;
import rnf.taxiad.providers.ItemsContract;
import rnf.taxiad.providers.TaxiContentProvider;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 3/3/16.
 */
public abstract class AbstractDataController implements OnAdsReceived {

    private Context context;
    private final Geocoder geocoder;
    private final LocationModule locationModule;
    private final GetAdsListClient getAdsListClient;
    private final AdsController adsController;
    private boolean isDownloading = false;

    public AbstractDataController(Context context) {
        super();
        synchronized (this) {
            this.context = context;
            this.geocoder = new Geocoder(context, Locale.US);
            this.locationModule = new LocationModule();
            getAdsListClient = new GetAdsListClient(this.context, this);
//            sendStatsClient = new SendAdsStatsClient(this.context);
            adsController = new AdsController(context);
        }
    }

    public void onLocationChanged(Location location, String fromActivity) {
        synchronized (geocoder) {
            locationModule.setCurrentLocation(location);
            getAddress(location,fromActivity);
//                final Address address = getAddress.get();
//                if (address != null) {
//                    address.setLatitude(location.getLatitude());
//                    address.setLongitude(location.getLongitude());
//                    this.locationModule.setCurrentAddress(address);
//                    this.locationModule.setCurrentLocation(location);
////                    callLocationRunnable(this.locationModule.getCurrentLatLong()[0] + "", this.locationModule.getCurrentLatLong()[1] + "");
//                    //getLocation(addresses);
////                    addDistance();
//                    getAds();
//                }
        }
    }




    public void getAds() {
        if (isDownloading)
            return;
        getAdsListClient.makeRequest(getCurrentCity(), getCurrentCountry(), getCurrentState(), getCurrentCounty(), getCurrentZip(), getCountyCode());
    }

//    public void addDistance() {
////        if (locationModule.getLastDistanceLocation() == null || locationModule.getCurrentLocation() == null)
////            return;
////        final Intent i = new Intent(context, DistanceService.class);
////        i.putExtra(DistanceService.LAST_LOCATION, locationModule.getLastDistanceLocation());
////        i.putExtra(DistanceService.CURRENT_LOCATION, locationModule.getCurrentLocation());
////        context.startService(i);
//    }

    public String getCurrentCity() {
        return locationModule.getCurrentCity();
    }

    public String getLastCity() {
        return locationModule.getLastCity();
    }


    public String getCurrentCountry() {
        return locationModule.getCurrentCountry();
    }

    public String getLastCountry() {
        return locationModule.getLastCountry();
    }


    public String getCurrentZip() {
        return locationModule.getCurrentZipCode();
    }

    public String getLastZip() {
        return locationModule.getLastZipCode();
    }

    public String getCurrentState() {
        return locationModule.getCurrentState();
    }

    public String getLastState() {
        return locationModule.getLastState();
    }

    public String getCurrentCounty() {
        return locationModule.getCurrentCounty();
    }

    public String getLastCounty() {
        return locationModule.getLastCounty();
    }

    public String getCountyCode() {
        return locationModule.getCountryCode();
    }


    public LocationModule getLocationModule() {
        return locationModule;
    }

    private void insert(Uri uri, List<ContentValues> contentValues) {
        switch (TaxiContentProvider.URI_MATCHER.match(uri)) {
            case TaxiContentProvider.ITEM_LIST:
                applyBatch(contentValues);
                break;
            case TaxiContentProvider.ITEM_ID:
                break;
            case TaxiContentProvider.STATS_LIST:
                break;
            case TaxiContentProvider.STATS_ID:
                break;

        }
    }

    private void applyBatch(List<ContentValues> contentValues) {
        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        for (ContentValues cv : contentValues) {
            ops.add(ContentProviderOperation.newInsert(ItemsContract.Videos.CONTENT_URI)
                    .withValue(ItemsContract.Videos._AD_ID, cv.get(ItemsContract.Videos._AD_ID))
                    .withValue(ItemsContract.Videos._USER_ID, cv.get(ItemsContract.Videos._USER_ID))
                    .withValue(ItemsContract.Videos._AD_URL, cv.get(ItemsContract.Videos._AD_URL))
                    .withValue(ItemsContract.Videos._THUMBNAIL_URL, cv.get(ItemsContract.Videos._THUMBNAIL_URL))
                    .withValue(ItemsContract.Videos._AD_TITLE, cv.get(ItemsContract.Videos._AD_TITLE))
                    .withValue(ItemsContract.Videos._KEYWORD, cv.get(ItemsContract.Videos._KEYWORD))
                    .withValue(ItemsContract.Videos._NUMBER, cv.get(ItemsContract.Videos._NUMBER))
                    .withValue(ItemsContract.Videos._ADVERTISER_ID, cv.get(ItemsContract.Videos._ADVERTISER_ID))
                    .withValue(ItemsContract.Videos._ADVERTISER_NAME, cv.get(ItemsContract.Videos._ADVERTISER_NAME))
                    .withValue(ItemsContract.Videos._TAGLINE, cv.get(ItemsContract.Videos._TAGLINE))
                    .withValue(ItemsContract.Videos._TYPE, cv.get(ItemsContract.Videos._TYPE))
                    .withValue(ItemsContract.Videos._PRIORITY, cv.get(ItemsContract.Videos._PRIORITY))
                    .withValue(ItemsContract.Videos._LOCATION, locationModule.getCurrentCity())
                    .withValue(ItemsContract.Videos._COUNTRY, locationModule.getCurrentCountry())
                    .withValue(ItemsContract.Videos._STATE, locationModule.getCurrentState())
                    .withValue(ItemsContract.Videos._COUNTY, locationModule.getCurrentCounty())
                    .withValue(ItemsContract.Videos._ZIP_CODE, locationModule.getCurrentZipCode())
                    .withYieldAllowed(true)
                    .build());
        }
        try {
            context.getContentResolver().applyBatch(ItemsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
        } catch (OperationApplicationException e) {
        }
    }

    @Override
    public synchronized void onReceived(JSONArray jsonArray, String city, String country, String state, String county, String zipcode) {
        if (jsonArray == null || jsonArray.length() == 0)
            return;
        if (getLastCity().trim().length() != 0 && !getLastCity().equalsIgnoreCase(city)
                || getLastCountry().trim().length() != 0 && !getLastCountry().equalsIgnoreCase(country)
                || getLastState().trim().length() != 0 && !getLastState().equalsIgnoreCase(state)
                || getLastCounty().trim().length() != 0 && !getLastCounty().equalsIgnoreCase(county)
                || getLastZip().trim().length() != 0 && !getLastZip().equalsIgnoreCase(zipcode))
            deleteOldData(getLastCity(), getLastCountry(), getLastState(), getLastCounty(), getLastZip());
        deleteAdsWhichAreNotAvailable(jsonArray);
        final List<Ads> ads = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final Ads a = new Ads(jsonArray.getJSONObject(i));
                if (adsController.needToUpdate(a))
                    adsController.update(a);
                else if (!adsController.isAvailable(a)) {
                    ads.add(a);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ads.size() > 0)
            insert(ItemsContract.Videos.CONTENT_URI, adsController.getValues(ads));
    }

    private void deleteAdsWhichAreNotAvailable(JSONArray jsonArray) {
        final List<Ads> ads = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final Ads a = new Ads(jsonArray.getJSONObject(i));
                ads.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ads.size() > 0)
            adsController.deleteAdsWhichAreNotAvailable(ads);
    }

    private void deleteOldData(String lastCity, String lastcountry, String laststate, String lastcounty, String lastzipcode) {
        adsController.deleteDataByCity(lastCity, lastcountry, laststate, lastcounty, lastzipcode);
    }

    public AdsController getAdsController() {
        return adsController;
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }


    public void sendStats() {
        final SendAdsStatsClient statsClient = new SendAdsStatsClient(context);
        statsClient.makeRequest();
    }

    public void stopDistance() {
        if (this.locationModule != null)
            this.locationModule.stopDistance();

    }

    public void startDistance() {
        if (this.locationModule != null)
            this.locationModule.startDistance();
    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });


    private synchronized void getAddress(final Location location, final String fromActivity ) {

        String url = GlobalUtils.getAddressUrl(location.getLatitude()+"",location.getLongitude()+"");
        final StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                synchronized (geocoder) {
                    try {
                        final Address address = getAddress(response);
                        if (address != null) {
                            address.setLatitude(location.getLatitude());
                            address.setLongitude(location.getLongitude());
                            locationModule.setCurrentAddress(address);
                            locationModule.setCurrentLocation(location);
                            if(fromActivity != null && fromActivity.equalsIgnoreCase("yes")) {
                                getAds();
                            }else if(fromActivity != null && fromActivity.equalsIgnoreCase("no")){
                                Log.d("RIDE","Location service response --> "+ response);
                            }else{
                                Log.d("RIDE","Inside else --> ");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null);

        VolleyController.getInstance(context).addToRequestQueue(request);
    }

    private Address getAddress(String response) {
        Gson gson = new Gson();
        LocationAddress locationAddress = gson.fromJson(response, LocationAddress.class);
        if (locationAddress != null && locationAddress.getResults().size() > 0) {
            Address address = new Address(Locale.US);
            List<LocationAddress.ResultsBean.AddressComponentsBean> addresslist = locationAddress.getResults().get(0).getAddress_components();
            if (addresslist != null && addresslist.size() > 0) {
                for (int i = 0; i < addresslist.size(); i++) {
                    if (addresslist.get(i).getTypes().contains("country")) {
                        address.setCountryCode(addresslist.get(i).getShort_name());
                        address.setCountryName(addresslist.get(i).getLong_name());
                    }
                    if (addresslist.get(i).getTypes().contains("administrative_area_level_1")) {
                        address.setAdminArea(addresslist.get(i).getLong_name());
                    }

                    if (addresslist.get(i).getTypes().contains("administrative_area_level_2")) {
                        address.setSubAdminArea(addresslist.get(i).getLong_name());
                    }

                    if (addresslist.get(i).getTypes().contains("locality")) {
                        address.setLocality(addresslist.get(i).getLong_name());
                    }

                    if (addresslist.get(i).getTypes().contains("postal_code")) {
                        address.setPostalCode(addresslist.get(i).getLong_name());
                    }

                    if (addresslist.get(i).getTypes().contains("neighborhood")) {

                    }
                }

                return address;
            }
        }
        return null;
    }





}
