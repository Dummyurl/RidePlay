package rnf.taxiad.helpers;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;

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
import rnf.taxiad.interfaces.RequestEntity;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.volley.ErrorParser;
import rnf.taxiad.volley.ITRequest;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 8/3/16.
 */
public abstract class SendMailToAdmin implements RequestEntity, Response.Listener, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    private boolean isRequesting = false;
    private WeakReference<Activity> activityWeakReference;
    private String phone = "@";
    private String referred = "No";

    public SendMailToAdmin(Activity activity) {
        activityWeakReference = new WeakReference<Activity>(activity);
    }


    public void setParams(String phone, String reference) {
        this.phone = phone;
        this.referred = reference;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        return NetworkUrls.SEND_MAIL;
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
        localParams.put("user_id", String.valueOf(DriverSession.get().getDriver().userID));
        localParams.put("phone_no", phone);
        localParams.put("refrerredDriver", referred);
        return localParams;
    }

    @Override
    public JSONObject getJSONObject() {
        return null;
    }

    @Override
    public void parse(String json) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onError();
        try {
            final Activity activity = activityWeakReference.get();
            if (activity == null)
                return;
            final ErrorParser errorParser = new ErrorParser(activity.getApplicationContext(), error);
            ColoredSnackbar.alert(Snackbar.make(activity.findViewById(android.R.id.content),
                    errorParser.getMessage(), Snackbar.LENGTH_LONG)).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRequesting = false;
    }


    @Override
    public void onResponse(Object response) {
        try {
            final JSONObject obj = new JSONObject((String) response);
            Log.e("Ride Play Response..",obj+"....");
            int errorCode = obj.has("error") && !obj.isNull("error") ?
                    obj.getInt("error") : -1;
            String message = obj.has("message") && !obj.isNull("message")
                    ? obj.getString("message") : null;
            final Activity activity = activityWeakReference.get();
            if (errorCode != 0 && activity != null) {
                onError();
                if (errorCode == 2)
                    GlobalUtils.logout(activity, message);
            } else if (errorCode == 0)
                onSuccess(message);
            ColoredSnackbar.alert(Snackbar.make(activity.findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_LONG)).show();
        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
        isRequesting = false;
    }

    public void makeRequest() {
        if (isRequesting)
            return;
        final Activity activity = activityWeakReference.get();
        if (activity == null)
            return;
        final ITRequest request = new ITRequest(this);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG);
        VolleyController.getInstance(activity).addToRequestQueue(request, TAG);
        isRequesting = true;
        onStarted();
    }

    public void cancel() {
        if (!isRequesting)
            return;
        final Activity activity = activityWeakReference.get();
        if (activity == null)
            return;
        VolleyController.getInstance(activity).cancelPendingRequests(TAG);
        onError();
    }

    public abstract void onStarted();

    public abstract void onError();

    public abstract void onSuccess(String message);

}
