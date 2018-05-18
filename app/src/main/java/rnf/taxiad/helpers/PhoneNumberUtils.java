package rnf.taxiad.helpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Rahil on 27/5/16.
 */
public class PhoneNumberUtils {

    public ArrayList<PhoneNumbers> phoneNumbers = new ArrayList<>();

    public PhoneNumberUtils(Context context) {
        super();
        readFromJson(context);
    }

    private void readFromJson(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("country.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (json == null)
            return;
        try {
            final JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                phoneNumbers.add(new PhoneNumbers(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(phoneNumbers);
    }

    public class PhoneNumbers implements Comparable<PhoneNumbers> {
        public String name;
        public String dial_code;
        public String code;

        public PhoneNumbers(JSONObject jsonObject) {
            super();
            parse(jsonObject);
        }

        private void parse(JSONObject jsonObject) {
            if (jsonObject == null)
                return;
            try {
                name = jsonObject.getString("name");
                dial_code = jsonObject.getString("dial_code");
                code = jsonObject.getString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
//            ArrayList<String> list = new ArrayList<>();
//            list.add(code);
//            list.add(dial_code);
//            return TextUtils.join(" ", list);
            return code + " (" + dial_code + ")";
        }

        @Override
        public int compareTo(PhoneNumbers another) {
            return this.code.compareTo(another.code);
        }
    }
}
