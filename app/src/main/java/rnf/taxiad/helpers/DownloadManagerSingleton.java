package rnf.taxiad.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.thin.downloadmanager.ThinDownloadManager;

import rnf.taxiad.doa.SQLiteHelper;

/**
 * Created by Rahil on 14/12/15.
 */
public class DownloadManagerSingleton {


    private static ThinDownloadManager downloadManager;

    public synchronized static ThinDownloadManager getInstance() {
        if (downloadManager == null)
            downloadManager = new ThinDownloadManager();
        return downloadManager;
    }


}
