package rnf.taxiad.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rnf.taxiad.Utility.ColoredSnackbar;
import rnf.taxiad.Utility.ContentType;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.NetworkUrls;
import rnf.taxiad.activities.ManageAccountActivity;
import rnf.taxiad.interfaces.RequestEntity;
import rnf.taxiad.models.Driver;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.volley.ErrorParser;
import rnf.taxiad.volley.ITRequest;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 19/1/16.
 */
public abstract class UpdateProfileClient implements RequestEntity, Response.Listener, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    private WeakReference<Activity> activityWeakReference;
    private boolean isRequesting = false;
    private Driver driver;
    Context mContext;
    public UpdateProfileClient(Activity activity) {
        this.activityWeakReference = new WeakReference<Activity>(activity);
        this.mContext = activity;
    }

    public void setParams(Driver driver) {
        this.driver = driver;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        return NetworkUrls.UPDATE_PROFILE;
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
        return Request.Priority.IMMEDIATE;
    }

    @Override
    public Map<String, String> getParams() {
        final Map<String, String> localParams = new HashMap<>();
        localParams.put("user_id", String.valueOf(DriverSession.get().getDriver().userID));
        localParams.put("fname", driver.fname);
        localParams.put("lname", driver.lname);
        localParams.put("city", driver.city);
        localParams.put("comments", driver.comments);
        localParams.put("interest", String.valueOf(driver.interest == 1 ? 0 : 1));
        localParams.put("paypal_id", driver.paypalId);
        localParams.put("phone", driver.phone);
        localParams.put("referral_code", driver.referralCode);
        localParams.put("services", driver.services);
        localParams.put("state", driver.state);
        localParams.put("tablet", driver.tablet);
        localParams.put("tablet_size", driver.tabletSize);
        localParams.put("trips", driver.trips);
        localParams.put("new_pass", driver.new_pass);
        return localParams;
    }

    @Override
    public JSONObject getJSONObject() {
        return null;
    }

    @Override
    public void parse(String json) {
        isRequesting = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        isRequesting = false;
        onError();
        final Activity activity = activityWeakReference.get();
        if (activity == null)
            return;
        final ErrorParser errorParser = new ErrorParser(activity, error);
        ColoredSnackbar.alert(Snackbar.make(activity.findViewById(android.R.id.content),
                errorParser.getMessage(), Snackbar.LENGTH_LONG)).show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            final JSONObject obj = new JSONObject((String) response);
            int errorCode = obj.has("error") && !obj.isNull("error") ?
                    obj.getInt("error") : -1;
            String message = obj.has("message") && !obj.isNull("message")
                    ? obj.getString("message") : null;

            if (errorCode != 0) {
                onError();
                final Activity activity = activityWeakReference.get();
                if (activity != null && errorCode == 2)
                    GlobalUtils.logout(activity, message);
            } else if (errorCode == 0)
                onSuccess(driver);
            final Activity activity = activityWeakReference.get();
            if (activity != null && message != null)
                ColoredSnackbar.alert(Snackbar.make(activity.findViewById(android.R.id.content),
                        message, Snackbar.LENGTH_LONG)).show();
            Toast.makeText(activity,"Profile successfully updated",Toast.LENGTH_LONG).show();
            ((ManageAccountActivity) mContext).finish();

        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
        isRequesting = false;
    }

    public abstract void onStarted();

    public abstract void onError();

    public abstract void onSuccess(Driver driver);


    public void makeRequest() {
        if (isRequesting)
            return;
        final Activity activity = activityWeakReference.get();
        if (activity == null)
            return;
        onStarted();
        final ITRequest request = new ITRequest(this);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG);
        VolleyController.getInstance(activity.getApplicationContext()).addToRequestQueue(request, TAG);
        isRequesting = true;
    }

    public void cancel() {
        onError();
        final Activity activity = activityWeakReference.get();
        if (activity == null)
            return;
        VolleyController.getInstance(activity.getApplicationContext()).cancelPendingRequests(TAG);
        isRequesting = false;
    }
}