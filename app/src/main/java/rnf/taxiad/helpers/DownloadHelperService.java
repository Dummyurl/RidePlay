package rnf.taxiad.helpers;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 8/3/16.
 */
public class DownloadHelperService extends IntentService {

    private static final String NAME = "DownloadHelperService";


    public DownloadHelperService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String id = intent.getStringExtra("AdId");
        final ContentValues cv = intent.getParcelableExtra("Values");
        String whereStr = ItemsContract.VideoFile._AD_ID + " = " + id;
        getContentResolver().update(ItemsContract.VideoFile.CONTENT_URI, cv, whereStr, null);
    }
}
