package rnf.taxiad.helpers;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 8/3/16.
 */
public class ProgressService extends IntentService {
    private static final String NAME = "ProgressService";
    private ResultReceiver receiver;

    public ProgressService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra("ResultReceiver");
        final Cursor c = getContentResolver().
                query(ItemsContract.VideoFile.CONTENT_URI,
                        new String[]{ItemsContract.VideoFile._TOTAL, ItemsContract.VideoFile._DOWNLOADED}, null, null, null);
        if (c == null || c.getCount() == 0)
            return;
        long total = 0;
        long download = 0;
        while (c.moveToNext()) {
            String totalStr = c.getString(c.getColumnIndex(ItemsContract.VideoFile._TOTAL));
            totalStr = totalStr == null || totalStr.trim().length() == 0 ||
                    !TextUtils.isDigitsOnly(totalStr) ? "0" : totalStr;
            String downloadStr = c.getString(c.getColumnIndex(ItemsContract.VideoFile._DOWNLOADED));
            downloadStr = downloadStr == null || downloadStr.trim().length() == 0 ||
                    !TextUtils.isDigitsOnly(downloadStr) ? "0" : downloadStr;
            total += Long.valueOf(totalStr);
            download += Long.valueOf(downloadStr);
        }
        c.close();
        if (total == 0 || download == 0)
            return;
        Bundle b = new Bundle();
        b.putLong("Total", total);
        b.putLong("Downloaded", download);
        if (receiver != null)
            receiver.send(1, b);
    }
}
