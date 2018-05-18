package rnf.taxiad.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rnf.taxiad.Utility.ContentType;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.NetworkUrls;
import rnf.taxiad.controllers.DataController;
import rnf.taxiad.controllers.StatsController;
import rnf.taxiad.interfaces.RequestEntity;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.models.Stats;
import rnf.taxiad.volley.ITRequest;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 26/2/16.
 */
public class SendAdsStatsClient implements RequestEntity, Response.Listener, Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    private boolean isRequesting = false;
    private Context context;
    private StatsController statsController;
    private ArrayList<String> ids = new ArrayList<>();

    public SendAdsStatsClient(Context context) {
        this.context = context;
        statsController = new StatsController(context);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getUrl() {
        return NetworkUrls.ADS_STATS;
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
        localParams.put("ads", createParams().toString());
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
//                statsController.deleteFromStats(ids);
                final List<Stats> stats = statsController.getAll();

                statsController.readupdate(stats.get(0));
            }

        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
        ids.clear();
        isRequesting = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        isRequesting = false;
        ids.clear();
    }

    @Override
    public void onResponse(Object response) {
        ids.clear();
        try {
            final JSONObject obj = new JSONObject((String) response);
            int errorCode = obj.has("error") && !obj.isNull("error") ?
                    obj.getInt("error") : -1;
            String message = obj.has("message") && !obj.isNull("message")
                    ? obj.getString("message") : null;
            if (errorCode == 2) {
                GlobalUtils.logout(context, message);
            }
        } catch (Exception e) {
            onErrorResponse(new ParseError(e));
        }
        isRequesting = false;
    }

    public void makeRequest() {
        if (context == null)
            return;
        final ITRequest request = new ITRequest(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG);
        VolleyController.getInstance(context).addToRequestQueue(request, TAG);
        isRequesting = true;
    }

    private JSONArray createParams() {
        final JSONArray jsonArray = new JSONArray();
        try {

            final List<Stats> stats = statsController.getAll();
            for (Stats s : stats) {
                final JSONObject obj = new JSONObject();
                obj.put("adsID", s.adId);
                obj.put("driverID", s.userId);
                obj.put("advertiserID", s.advertiserId);
                obj.put("lat", s.latitude);
                obj.put("long", s.longitude);
                obj.put("city", s.city);
                obj.put("address", s.address);
//                obj.put("noOfTimesPlayed", s.played);
//                obj.put("displayCount", s.viewed);
                obj.put("numberOfTimesTaped", s.taped);
                obj.put("displayDuration", GlobalUtils.stringForTime(Integer.valueOf(s.duration)));
                obj.put("displayCount", s.viewed);
                obj.put("updated", s.date);
                jsonArray.put(obj);
                if (!ids.contains(s.id))
                    ids.add(s.id);
//                Toast.makeText(context,"displayCount value pass="+s.viewed,Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

}
