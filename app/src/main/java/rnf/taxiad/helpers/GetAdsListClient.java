package rnf.taxiad.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rnf.taxiad.Utility.ContentType;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.NetworkUrls;
import rnf.taxiad.interfaces.OnAdsReceived;
import rnf.taxiad.interfaces.RequestEntity;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.volley.ITRequest;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 19/1/16.
 */
public class GetAdsListClient implements RequestEntity, Response.Listener, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    //    private boolean isRequesting = false;
    private Context context;
    private WeakReference<OnAdsReceived> weakReference;
    private String city;
    private String country;
    private String zipcode;
    private String state;
    private String county;
    private String countrycode;

    public GetAdsListClient(Context context, OnAdsReceived listener) {
        this.context = context;
        this.weakReference = new WeakReference<OnAdsReceived>(listener);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        return NetworkUrls.GET_ADS;
    }

    @Override
    public Response.ErrorListener getErrorListener() {
        return this;
    }

    @Override
    public Response.Listener getResponseListener() {
        return this;
    }

    @Override
    public String getContentType() {
        return ContentType.URL_ENCODED;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public Request.Priority getPriority() {
        return Request.Priority.HIGH;
    }

    @Override
    public Map<String, String> getParams() {
        final Map<String, String> localParams = new HashMap<>();
        localParams.put("zip", zipcode == null ? "" : zipcode);
        localParams.put("city", city == null ? DriverSession.get().getDriver().city : city);
        localParams.put("county", county == null ? "" : county.replace(" County",""));
        localParams.put("state", state == null ? "" : state);
        localParams.put("country", countrycode == null ? "" : countrycode);
        String logtext = " params is...Z.." + zipcode + "..City.." + city + "..County.." + county + "..State.." + state + "..Country..." + country;
        GlobalUtils.writeLogs("Request Param" + "\n" + logtext);
        Log.d("RIDE","Request Params ---> "+localParams.toString());
        localParams.put("user_id", String.valueOf(DriverSession.get().getDriver().userID));
        return localParams;
    }

    @Override
    public JSONObject getJSONObject() {
        return null;
    }

    @Override
    public void parse(String json) {
//        isRequesting = false;
        try {
            final JSONObject jsonObject = new JSONObject(json);
            int errorCode = jsonObject.has("error") && !jsonObject.isNull("error") ?
                    jsonObject.getInt("error") : -1;
            if (errorCode != 0)
                return;
            final Object object = jsonObject.has("ads") && !jsonObject.isNull("ads") ? jsonObject.get("ads") : null;
            if (!(object instanceof JSONArray))
                return;
            final JSONArray jsonArray = (JSONArray) object;
            final OnAdsReceived listener = weakReference.get();
            if (listener != null)
                listener.onReceived(jsonArray, city, country, state, county, zipcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        isRequesting = false;
    }

    @Override
    public void onResponse(Object response) {
       // String logtext = "..Zip.." + zipcode + "..City.." + city + "..County.." + county + "..State.." + state + "..Country..." + country;
       // GlobalUtils.writeLogs("REQUEST PARAM" + "\n" + "\n" + logtext + "\n" + "\n" + "RESPONSE " + "\n" + "\n" + response);
        try {
            final JSONObject jsonObject = new JSONObject((String) response);
            int errorCode = jsonObject.has("error") && !jsonObject.isNull("error") ?
                    jsonObject.getInt("error") : -1;
            String message = jsonObject.has("message") && !jsonObject.isNull("message")
                    ? jsonObject.getString("message") : null;
            if (errorCode == 0)
                return;
            if (errorCode == 1)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            if (errorCode == 2)
                GlobalUtils.logout(context, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        isRequesting = false;
    }

    public void makeRequest(String currentCity, String currentCountry, String currentState, String currentCounty, String currentZipcode, String countrycode) {
//        if (isRequesting)
//            return;
        if (context == null)
            return;
        this.city = currentCity;
        this.country = currentCountry;
        this.state = currentState;
        this.county = currentCounty;
        this.zipcode = currentZipcode;
        this.countrycode = countrycode;
        final ITRequest request = new ITRequest(this);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG);
        VolleyController.getInstance(context).addToRequestQueue(request, TAG);
//        isRequesting = true;
    }

    public void cancel() {
//        if (!isRequesting)
//            return;
        if (context == null)
            return;
        VolleyController.getInstance(context).cancelPendingRequests(TAG);
        onErrorResponse(new VolleyError());
    }
}
