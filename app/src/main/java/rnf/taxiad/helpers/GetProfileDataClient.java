package rnf.taxiad.helpers;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
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
import rnf.taxiad.volley.ITRequest;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 31/3/16.
 */
public abstract class GetProfileDataClient implements RequestEntity, Response.Listener, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    private WeakReference<Activity> activityWeakReference;
    private boolean isRequesting = false;

    public GetProfileDataClient(Activity activity) {
        this.activityWeakReference = new WeakReference<Activity>(activity);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        return NetworkUrls.GET_PROFILE;
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
        return localParams;
    }

    @Override
    public JSONObject getJSONObject() {
        return null;
    }

    @Override
    public void parse(String json) {
        isRequesting = false;
        try {
            final JSONObject obj = new JSONObject(json);
            JSONObject driverInfo = obj.has("driverInfo") && !obj.isNull("driverInfo")
                    ? obj.getJSONObject("driverInfo") : null;
            if (driverInfo != null)
                DriverSession.get().createSession(driverInfo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        isRequesting = false;
        onError();
//        try {
//            final Activity activity = activityWeakReference.get();
//            if (activity == null)
//                return;
//            final ErrorParser errorParser = new ErrorParser(activity.getApplicationContext(), error);
//            ColoredSnackbar.alert(Snackbar.make(activity.findViewById(android.R.id.content),
//                    errorParser.getMessage(), Snackbar.LENGTH_LONG)).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onResponse(Object response) {
        isRequesting = false;
        try {
            final JSONObject obj = new JSONObject((String) response);
            int errorCode = obj.has("error") && !obj.isNull("error") ?
                    obj.getInt("error") : -1;
            String message = obj.has("message") && !obj.isNull("message")
                    ? obj.getString("message") : null;
            final Activity activity = activityWeakReference.get();
            if (errorCode != 0 && activity != null) {
                onError();
                ColoredSnackbar.alert(Snackbar.make(activity.findViewById(android.R.id.content),
                        message, Snackbar.LENGTH_LONG)).show();
                if(errorCode==2)
                GlobalUtils.logout(activity, message);
            } else if (errorCode == 0)
                onSuccess();
        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
    }

    public abstract void onStarted();

    public abstract void onError();

    public abstract void onSuccess();


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
