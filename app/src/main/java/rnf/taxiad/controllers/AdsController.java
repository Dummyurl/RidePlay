package rnf.taxiad.controllers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rnf.taxiad.models.Ads;
import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 17/2/16.
 */
public class AdsController {

    private Context context;
    private FileUtils fileUtils;

    public AdsController(Context context) {
        super();
        this.context = context;
        this.fileUtils = new FileUtils(this.context);
    }

    public synchronized Ads getAds(Cursor cursor) {
        final Ads ads = new Ads(cursor);
        return ads;
    }

    public synchronized Ads getByAdId(String adId) {
        String whereStr = ItemsContract.Videos._AD_ID + " = " + adId;
        final Cursor c = context.getContentResolver().query(ItemsContract.Videos.CONTENT_URI,
                ItemsContract.Videos.PROJECTION_ALL, whereStr, null, null);
        if (c == null || c.getCount() == 0)
            return null;
        c.moveToFirst();
        return getAds(c);
    }

    public synchronized Ads getById(String id) {
        Uri singleUri = ContentUris.withAppendedId(ItemsContract.Videos.CONTENT_URI,
                Long.parseLong(id));
        final Cursor c = context.getContentResolver().query(singleUri,
                ItemsContract.Videos.PROJECTION_ALL, null, null, null);
        if (c == null || c.getCount() == 0)
            return null;
        return getAds(c);
    }

    public synchronized List<Ads> getByCurrentCity() throws NullPointerException {
        List<Ads> ads = new ArrayList<>();
        String whereStr = ItemsContract.Videos._LOCATION + " = " + DataController.getInstance(context).getCurrentCity();
        final Cursor c = context.getContentResolver().query(ItemsContract.Videos.CONTENT_URI,
                ItemsContract.Videos.PROJECTION_ALL, whereStr, null, "");
        if (c == null || c.getCount() == 0)
            return null;
        while (c.moveToNext()) {
            ads.add(getAds(c));
        }
        return ads;
    }

    public synchronized List<Ads> getByNotCurrentCity() throws NullPointerException {
        List<Ads> ads = new ArrayList<>();
        String whereStr = ItemsContract.Videos._LOCATION + " != " + DataController.getInstance(context).getCurrentCity();
        final Cursor c = context.getContentResolver().query(ItemsContract.Videos.CONTENT_URI,
                ItemsContract.Videos.PROJECTION_ALL, whereStr, null, "");
        if (c == null || c.getCount() == 0)
            return null;
        while (c.moveToNext()) {
            ads.add(getAds(c));
        }
        return ads;
    }

    public synchronized List<Ads> getAllAds() {
        final List<Ads> ads = new ArrayList<>();
        final Cursor c = context.getContentResolver().query(ItemsContract.Videos.CONTENT_URI,
                ItemsContract.Videos.PROJECTION_ALL, null, null, null);
        if (c == null || c.getCount() == 0)
            return ads;
        while (c.moveToNext()) {
            final Ads ad = new Ads(c);
            ads.add(ad);
        }
        return ads;
    }

    public String getId(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._ID));
    }

    public String getAdId(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._AD_ID));
    }

    public String getAdTitle(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._AD_TITLE));
    }

    public String getDriverId(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._USER_ID));
    }

    public String getAdUrl(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._AD_URL));
    }

    public String getThumnail(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._THUMBNAIL_URL));
    }

    public String getKeyword(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._KEYWORD));
    }

    public String getNumber(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._NUMBER));
    }

    public String getAdvertiserId(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._ADVERTISER_ID));
    }

    public String getAdvertiserName(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._ADVERTISER_NAME));
    }

    public String getLocation(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._LOCATION));
    }



    public String getTagline(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._TAGLINE));
    }

    public String getType(Cursor c) {
        return c.getString(c.getColumnIndex(ItemsContract.Videos._TYPE));
    }

    public boolean getStatus(Cursor c) {
        return c.getInt(c.getColumnIndex(ItemsContract.Videos._STATUS)) == 0 ? false : true;
    }


    public String getCountry(Cursor c){
        return c.getString(c.getColumnIndex(ItemsContract.Videos._COUNTRY));
    }

    public String getStates(Cursor c){
        return c.getString(c.getColumnIndex(ItemsContract.Videos._STATE));
    }

    public String getCounty(Cursor c){
        return c.getString(c.getColumnIndex(ItemsContract.Videos._COUNTY));
    }

    public String getZipCode(Cursor c){
        return c.getString(c.getColumnIndex(ItemsContract.Videos._ZIP_CODE));
    }

    public List<ContentValues> getValues(List<Ads> ads) {
        final List<ContentValues> contentValues = new ArrayList<>();
        for (Ads ad : ads) {
            contentValues.add(getValue(ad));
        }
        return contentValues;
    }

    public ContentValues getValue(Ads ad) {
        final ContentValues cv = new ContentValues();
        cv.put(ItemsContract.Videos._AD_ID, ad._adId);
        cv.put(ItemsContract.Videos._USER_ID, ad._userId);
        cv.put(ItemsContract.Videos._AD_URL, ad._adUrl);
        cv.put(ItemsContract.Videos._THUMBNAIL_URL, ad._thumnailUrl);
        cv.put(ItemsContract.Videos._AD_TITLE, ad._adTitle);
        cv.put(ItemsContract.Videos._KEYWORD, ad._keyword);
        cv.put(ItemsContract.Videos._NUMBER, ad._number);
        cv.put(ItemsContract.Videos._ADVERTISER_ID, ad._advertiserId);
        cv.put(ItemsContract.Videos._ADVERTISER_NAME, ad._advertiserName);
        cv.put(ItemsContract.Videos._TAGLINE, ad._tagline);
        cv.put(ItemsContract.Videos._TYPE, ad._type);
        cv.put(ItemsContract.Videos._PRIORITY, ad._adsPriority);
        return cv;
    }

    public void update(Ads a) {
        String whereStr = ItemsContract.Videos._AD_ID + " = " + a._adId;
        context.getContentResolver().update(ItemsContract.Videos.CONTENT_URI, getValue(a), whereStr, null);
    }

    public boolean needToUpdate(Ads a) {
        final Ads ad = getByAdId(a._adId);
        if (ad == null)
            return false;
        if (!a._adUrl.equals(ad._adUrl) || !a._thumnailUrl.equals(ad._thumnailUrl) ||
                !a._adTitle.equals(ad._adTitle)
                || !a._keyword.equals(ad._keyword) || !a._number.equals(ad._number) ||
                !a._tagline.equals(ad._tagline) ||
                !a._type.equals(ad._type) || a._adsPriority != ad._adsPriority)
            return true;
        return false;
    }


    public boolean isAvailable(Ads a) {
        final Ads ad = getByAdId(a._adId);
        if (ad == null)
            return false;
        return true;

    }

    public void deleteDataByCity(String lastCity,String country,String state,String county,String zipcode) {
        final List<Ads> adsForFileDelete = new ArrayList<>();
        final List<Ads> ads = getAllAds();
        for (Ads ad : ads) {
            try {
                if (ad._location.equalsIgnoreCase(lastCity)) {
                    String whereStr = new StringBuilder().append(ItemsContract.Videos._LOCATION + " = " + lastCity)
                            .append(" AND "+ItemsContract.Videos._COUNTRY+" = "+country)
                            .append(" AND "+ItemsContract.Videos._STATE+" = "+state)
                            .append(" AND "+ItemsContract.Videos._COUNTY+" = "+county)
                            .append(" AND "+ItemsContract.Videos._ZIP_CODE+" = "+zipcode).toString();
                    context.getContentResolver().delete(ItemsContract.Videos.CONTENT_URI, whereStr, null);
                    adsForFileDelete.add(ad);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.fileUtils.deleteAll(adsForFileDelete);
    }

    public List<Ads> getAdsToDownload() {
        final List<Ads> toDownload = new ArrayList<>();
        final List<Ads> ads = getAllAds();
        for (Ads a : ads) {
            final String path = FileUtils.getVideoFolderPath(context) + File.separator +
                    FileUtils.getFileName(a._adUrl);
            File file = new File(path);
            if (file.exists())
                continue;
            toDownload.add(a);
        }
        return toDownload;
    }

    public void deleteAdsWhichAreNotAvailable(List<Ads> downloadedAds) {
        List<Ads> unavailableAds = getUnavailableAds(downloadedAds);
        for (Ads ad : unavailableAds) {
            try {
                String whereStr = ItemsContract.Videos._AD_ID + " = " + ad._adId;
                context.getContentResolver().delete(ItemsContract.Videos.CONTENT_URI, whereStr, null);
                context.getContentResolver().delete(ItemsContract.Stats.CONTENT_URI, whereStr, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.fileUtils.deleteAll(unavailableAds);
    }

    private List<Ads> getUnavailableAds(List<Ads> downloadedAds) {
        List<Ads> unavailable = new ArrayList<>();
        List<Ads> ads = getAllAds();
        for (Ads a : ads) {
            if (!a.isAvailable(downloadedAds))
                unavailable.add(a);
        }
        return unavailable;
    }
}
