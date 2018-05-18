package rnf.taxiad.controllers;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import rnf.taxiad.Utility.NetworkUrls;
import rnf.taxiad.volley.VolleyController;

/**
 * Created by Rahil on 25/2/16.
 */
public final class BroadcastController {

    public synchronized static void sendGhostCarBroadcast(Context c, Address last,
                                                          Address current, String driverId, String advertisementName
            , String adId, String advertiserId) throws UnsupportedEncodingException {
        if (current == null || last == null || adId == null || driverId == null ||
                advertisementName == null || advertiserId == null)
            return;
        String city1 = current.getLocality();
        Log.e("ghost car....", city1 + "<>");
        String city = URLEncoder.encode(current.getLocality().toLowerCase(Locale.US), "UTF-8").replaceAll("\\+", "%20");
        String uri = NetworkUrls.GHOST_CAR + driverId + "~" + "xyz" + "~" + advertiserId + "~" + adId + "~" +
                last.getLatitude() + "~" + last.getLongitude() + "~" + current.getLatitude() + "~" + current.getLongitude() + "~"
                + city;
        final StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("BroadcastController", "<>" + response);
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BroadcastController", "<>" + error);
                System.out.println("Error");
            }
        });
        VolleyController.getInstance(c).addToRequestQueue(request);
    }
}
