package rnf.taxiad.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahil on 18/1/16.
 */
public class Driver {

    public int userID;
    public int groupID;
    public int planID;
    public String fname;
    public String lname;
    public String email;
    public String phone;
    public String address;
    public String ssn;
    public String referralCode;
    public String paypalId;
    public String trips;
    public String tablet;
    public String tabletSize;
    public String state;
    public String city;
    public String services;
    public int interest;
    public String comments;
    public String profilePic;
    public String profilePicThumb;
    public String token;
    public String tokenStatus;
    public String status;
    public String visibility;
    public String date; // yyyy-MM-dd hh:mm:ss
    public String updatedDate;  // yyyy-MM-dd hh:mm:ss
    public String password;
    public String driver_advocate_prog;
    public String link_to_checkout;
    public String link_to_demo;
    public String lat;
    public String lng;

    public String new_pass;
    public void parse(String json) {
        if (json == null)
            return;
        try {
            final JSONObject obj = new JSONObject(json);
            userID = obj.has("userID") && !obj.isNull("userID") ? obj.getInt("userID") : null;
            groupID = obj.has("groupID") && !obj.isNull("groupID") ? obj.getInt("groupID") : null;
            planID = obj.has("planID") && !obj.isNull("planID") ? obj.getInt("planID") : null;
            fname = obj.has("fname") && !obj.isNull("fname") ? obj.getString("fname") : null;
            lname = obj.has("lname") && !obj.isNull("lname") ? obj.getString("lname") : null;
            email = obj.has("email") && !obj.isNull("email") ? obj.getString("email") : null;
            phone = obj.has("phone") && !obj.isNull("phone") ? obj.getString("phone") : null;
            lat = obj.has("lat") && !obj.isNull("lat") ? obj.getString("lat") : null;
            lng = obj.has("lng") && !obj.isNull("lng") ? obj.getString("lng") : null;
            address = obj.has("address") && !obj.isNull("address") ? obj.getString("address") : null;
            ssn = obj.has("ssn") && !obj.isNull("ssn") ? obj.getString("ssn") : null;
            referralCode = obj.has("referral_code") && !obj.isNull("referral_code") ? obj.getString("referral_code") : null;
            paypalId = obj.has("paypal_id") && !obj.isNull("paypal_id") ? obj.getString("paypal_id") : null;
            trips = obj.has("trips") && !obj.isNull("trips") ? obj.getString("trips") : null;
            tablet = obj.has("tablet") && !obj.isNull("tablet") ? obj.getString("tablet") : null;
            tabletSize = obj.has("tablet_size") && !obj.isNull("tablet_size") ? obj.getString("tablet_size") : null;
            state = obj.has("state") && !obj.isNull("state") ? obj.getString("state") : null;
            city = obj.has("city") && !obj.isNull("city") ? obj.getString("city") : null;
            services = obj.has("services") && !obj.isNull("services") ? obj.getString("services") : null;
            interest = obj.has("interest") && !obj.isNull("interest") ? obj.getInt("interest") : 1;
            comments = obj.has("comments") && !obj.isNull("comments") ? obj.getString("comments") : null;
            profilePic = obj.has("proflie_pic") && !obj.isNull("proflie_pic") ? obj.getString("proflie_pic") : null;
            profilePicThumb = obj.has("profile_pic_thumb") && !obj.isNull("profile_pic_thumb") ? obj.getString("profile_pic_thumb") : null;
            token = obj.has("pass_token") && !obj.isNull("pass_token") ? obj.getString("pass_token") : null;
            tokenStatus = obj.has("token_status") && !obj.isNull("token_status") ? obj.getString("token_status") : null;
            status = obj.has("status") && !obj.isNull("status") ? obj.getString("status") : null;
            visibility = obj.has("visibility") && !obj.isNull("visibility") ? obj.getString("visibility") : null;
            date = obj.has("date") && !obj.isNull("date") ? obj.getString("date") : null;
            updatedDate = obj.has("updated_date") && !obj.isNull("updated_date") ? obj.getString("updated_date") : null;
            driver_advocate_prog = obj.has("driver_advocate_prog") && !obj.isNull("driver_advocate_prog") ? obj.getString("driver_advocate_prog") : null;
            link_to_checkout = obj.has("link_to_checkout") && !obj.isNull("link_to_checkout") ? obj.getString("link_to_checkout") : null;
            link_to_demo = obj.has("link_to_demo") && !obj.isNull("link_to_demo") ? obj.getString("link_to_demo") : null;

            new_pass = obj.has("new_pass") && !obj.isNull("new_pass") ? obj.getString("new_pass") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("userID", userID);
            jsonObject.accumulate("groupID", groupID);
            jsonObject.accumulate("planID", planID);
            jsonObject.accumulate("fname", fname);
            jsonObject.accumulate("lname", lname);
            jsonObject.accumulate("email", email);
            jsonObject.accumulate("phone", phone);
            jsonObject.accumulate("lat", lat);
            jsonObject.accumulate("lng", lng);
            jsonObject.accumulate("address", address);
            jsonObject.accumulate("ssn", ssn);
            jsonObject.accumulate("referral_code", referralCode);
            jsonObject.accumulate("paypal_id", paypalId);
            jsonObject.accumulate("trips", trips);
            jsonObject.accumulate("tablet", tablet);
            jsonObject.accumulate("tablet_size", tabletSize);
            jsonObject.accumulate("state", state);
            jsonObject.accumulate("city", city);
            jsonObject.accumulate("services", services);
            jsonObject.accumulate("interest", interest);
            jsonObject.accumulate("comments", comments);
            jsonObject.accumulate("proflie_pic", profilePic);
            jsonObject.accumulate("profile_pic_thumb", profilePicThumb);
            jsonObject.accumulate("pass_token", token);
            jsonObject.accumulate("token_status", tokenStatus);
            jsonObject.accumulate("status", status);
            jsonObject.accumulate("visibility", visibility);
            jsonObject.accumulate("date", date);
            jsonObject.accumulate("updated_date", updatedDate);
            jsonObject.accumulate("driver_advocate_prog", driver_advocate_prog);
            jsonObject.accumulate("link_to_checkout", link_to_checkout);
            jsonObject.accumulate("link_to_demo", link_to_demo);

            if(jsonObject.has("new_pass"))
            jsonObject.accumulate("new_pass", new_pass);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }
}
