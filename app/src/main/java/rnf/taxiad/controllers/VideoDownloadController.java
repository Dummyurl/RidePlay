package rnf.taxiad.controllers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.List;

import rnf.taxiad.models.Ads;


/**
 * Created by Rahil on 18/2/16.
 */
public class VideoDownloadController {

    private Context context;
    private ThinDownloadManager downloadManager;
    private RetryPolicy retryPolicy;

    public VideoDownloadController(Context context) {
        super();
        this.context = context;
        downloadManager = new ThinDownloadManager();
        retryPolicy = new DefaultRetryPolicy(10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public void cancelAll() {
        Log.e("cancelAll ", "Request Cancel");
        downloadManager.cancelAll();
    }

    public void addRequest(DownloadRequest request) {
        if (request == null)
            return;
        synchronized (downloadManager) {
            int id = downloadManager.add(request);
        }
    }

    public void addRequest(List<DownloadRequest> requests) {
        if (requests == null || requests.size() == 0)
            return;
        synchronized (downloadManager) {
            for (DownloadRequest request : requests) {
                int id = downloadManager.add(request);
            }
        }
    }

    public DownloadRequest createRequest(Ads ads) {
        final DownloadRequest downloadRequest = new DownloadRequest(Uri.parse(ads._adUrl))
                .setRetryPolicy(retryPolicy)
                .setDestinationURI(Uri.parse(FileUtils.getVideoFolderPath(context) + File.separator +
                        FileUtils.getFileName(ads._adUrl)))
                .setDownloadContext(ads)
                .setPriority(DownloadRequest.Priority.HIGH);

        return downloadRequest;
    }

    public void download(List<Ads> ads) {

    }
}
