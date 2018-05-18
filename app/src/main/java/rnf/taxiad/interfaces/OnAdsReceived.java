package rnf.taxiad.interfaces;

import org.json.JSONArray;

/**
 * Created by Rahil on 3/3/16.
 */
public interface OnAdsReceived {

    public void onReceived(JSONArray jsonArray, String city, String country, String state, String county, String zipcode);
}
