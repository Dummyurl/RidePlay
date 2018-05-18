package rnf.taxiad.helpers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rnf.taxiad.Utility.ContentType;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.NetworkUrls;
import rnf.taxiad.interfaces.RequestEntity;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.models.OverallStats;
import rnf.taxiad.volley.ITRequest;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 29/2/16.
 */
public class SendDownloadStats implements RequestEntity, Response.Listener, Response.ErrorListener {

    private Context context;
    private JSONArray jsonArray = new JSONArray();
    private OverallStats overallStats;

    public SendDownloadStats(Context context, OverallStats overallStats) {
        this.context = context;
        this.overallStats = overallStats;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        return NetworkUrls.DOWNLOADED_STATS;
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
        localParams.put("ads", jsonArray.toString());
        return localParams;
    }

    @Override
    public JSONObject getJSONObject() {
        return null;
    }

    @Override
    public void parse(String json) {
        try {
            final JSONObject obj = new JSONObject(json);
            int errorCode = obj.has("error") && !obj.isNull("error") ?
                    obj.getInt("error") : -1;
            String message = obj.has("message") && !obj.isNull("message")
                    ? obj.getString("message") : null;
            if (errorCode == 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String key = jsonObject.getString("key");
                    overallStats.clear(key);
                }

            }
        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {

        try {
            final JSONObject obj = new JSONObject((String) response);
            int errorCode = obj.has("error") && !obj.isNull("error") ?
                    obj.getInt("error") : -1;
            String message = obj.has("message") && !obj.isNull("message")
                    ? obj.getString("message") : null;
            if (errorCode == 2)
                GlobalUtils.logout(context, message);
        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
    }

    public void makeRequest() {
        if (jsonArray.length() == 0)
            return;
        if (context == null)
            return;
        final ITRequest request = new ITRequest(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyController.getInstance(context).addToRequestQueue(request);
    }

    public void addStats(OverallStats.Statistics stats) {
        try {
            final JSONObject obj = new JSONObject();
            obj.put("driverID", String.valueOf(DriverSession.get().getDriver().userID));
            obj.put("distanceCovered", String.valueOf(stats.getDistanceCoveredInMiles()));
//        localParams.put("downloadedData", android.text.format.Formatter.formatFileSize(context,
//                statistics.downloadedBytes));
            obj.put("downloadedData", String.valueOf(stats.downloadedBytes));
            obj.put("downloaded", stats.downloadedBytes > 0 ? String.valueOf(1) : String.valueOf(0));
            obj.put("updated", stats.date);
            obj.put("key", stats.key);
            jsonArray.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
