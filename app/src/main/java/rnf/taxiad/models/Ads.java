package rnf.taxiad.models;

import android.database.Cursor;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 3/3/16.
 */
public class Ads implements Serializable {

    public int _id;
    public String _adId;
    public String _adTitle;
    public String _userId;
    public String _adUrl;
    public String _thumnailUrl;
    public String _keyword;
    public String _number;
    public String _tagline;
    public String _advertiserId;
    public String _advertiserName;
    public String _location;
    public String _type;
    public String _country;
    public String _state;
    public String _county;
    public String _zipcode;
    public boolean _status;
    public int _adsPriority = 0;


    public Ads(Cursor cursor) {
        super();
        _id = cursor.getInt(cursor.getColumnIndex(ItemsContract.Videos._ID));
        _adId = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._AD_ID));
        _adTitle = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._AD_TITLE));
        _userId = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._USER_ID));
        _adUrl = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._AD_URL));
        _thumnailUrl = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._THUMBNAIL_URL));
        _keyword = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._KEYWORD));
        _number = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._NUMBER));
        _advertiserId = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._ADVERTISER_ID));
        _advertiserName = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._ADVERTISER_NAME));
        _location = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._LOCATION));
        int status = cursor.getInt(cursor.getColumnIndex(ItemsContract.Videos._STATUS));
        _tagline = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._TAGLINE));
        _type = cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._TYPE));
        _adsPriority = cursor.getInt(cursor.getColumnIndex(ItemsContract.Videos._PRIORITY));
        _country=cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._COUNTRY));
        _state=cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._STATE));
        _county=cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._COUNTY));
        _zipcode=cursor.getString(cursor.getColumnIndex(ItemsContract.Videos._ZIP_CODE));
        _status = status == 1 ? true : false;
    }

    public Ads(JSONObject obj) {
        try {
            _adId = obj.getString("adsID");
            _userId = obj.getString("userID");
            _adUrl = obj.getString("ad");
            _thumnailUrl = obj.getString("ad_thumbnail");
            _adTitle = obj.getString("title");
            _keyword = obj.getString("keyword");
            _number = obj.getString("number");
            _advertiserId = obj.getString("advertiserID");
            _advertiserName = obj.getString("advertiserName");
            _tagline = obj.getString("tagline");
            _type = obj.getString("adsType");
            _adsPriority = obj.getInt("adsPriority");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAvailable(List<Ads> downloadedAds) {
        for (Ads a : downloadedAds) {
            if (this._adId.equals(a._adId))
                return true;
        }
        return false;
    }
}
