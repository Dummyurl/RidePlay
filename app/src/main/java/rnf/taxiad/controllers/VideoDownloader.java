package rnf.taxiad.controllers;

import android.content.Context;
import android.os.Handler;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.helpers.Broadcaster;
import rnf.taxiad.models.Ads;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.providers.ItemsContract;

/**
 * Created by Rahil on 30/3/16.
 */
public class VideoDownloader extends FileDownloadListener {

    public static final String TIME = "LastDownloaded";
    private Context context;
    private Handler handler = new Handler();
    private HashMap<String, Integer> totalBytesMap = new HashMap<>();
    private HashMap<String, Integer> downloadedBytes = new HashMap<>();
    private ArrayList<String> downloadingIds = new ArrayList<>();
    private boolean isDownloading = false;
    private FileUtils fileUtils;

    public VideoDownloader(Context context) {
        super();
        this.context = context;
        fileUtils = new FileUtils(this.context);
        FileDownloadUtils.setDefaultSaveRootPath(FileUtils.getVideoFolderPath(context));
    }

    public void download(List<Ads> ads) {
        if (isDownloading)
            return;
        if (ads.size() == 0)
            return;
        isDownloading = true;
        for (Ads a : ads) {
            final String path = FileUtils.getVideoFolderPath(context) + File.separator +
                    FileUtils.getFileName(a._adUrl);
            BaseDownloadTask task = FileDownloader.getImpl().create(a._adUrl);
            task.setPath(path).setForceReDownload(false).setListener(this).ready();
            task.setTag(a);
            downloadingIds.add(a._adId);
        }
        FileDownloader.getImpl().start(this, false);
        DataController.getInstance(context).setDownloading(isDownloading);
    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

    }

    @Override
    protected void progress(BaseDownloadTask baseDownloadTask, int soFarBytes, int totalBytes) {
        final Ads a = (Ads) baseDownloadTask.getTag();
        totalBytesMap.put(a._adId, totalBytes);
        downloadedBytes.put(a._adId, soFarBytes);
        handler.post(sendBytes);
    }

    @Override
    protected void blockComplete(BaseDownloadTask baseDownloadTask) {

    }

    @Override
    protected void completed(BaseDownloadTask baseDownloadTask) {
        final Ads a = (Ads) baseDownloadTask.getTag();
        downloadingIds.remove(a._adId);
        Broadcaster.get().sendCompleted(a._adId);
        handler.post(sendAllCompleted);
    }

    @Override
    protected void paused(BaseDownloadTask baseDownloadTask, int soFarBytes, int totalBytes) {

    }

    @Override
    protected void error(BaseDownloadTask baseDownloadTask, Throwable throwable) {
        final Ads a = (Ads) baseDownloadTask.getTag();
        downloadingIds.remove(a._adId);
        try {
            List<Ads> list = new ArrayList();
            list.add(a);
            String whereStr = ItemsContract.Videos._AD_ID + " = " + a._adId;
            context.getContentResolver().delete(ItemsContract.Videos.CONTENT_URI, whereStr, null);
            fileUtils.deleteAll(list);
        } catch (Exception e) {
        }
        handler.post(sendAllCompleted);
        Broadcaster.get().onError(a._adId);
    }

    @Override
    protected void warn(BaseDownloadTask baseDownloadTask) {

    }

    private Runnable sendBytes = new Runnable() {
        @Override
        public void run() {
            int total = 0;
            int downloaded = 0;
            for (Map.Entry<String, Integer> entry : totalBytesMap.entrySet()) {
                total += entry.getValue();
            }
            for (Map.Entry<String, Integer> entry : downloadedBytes.entrySet()) {
                downloaded += entry.getValue();
            }

            Broadcaster.get().sendProgress(total, downloaded);
        }
    };

    private Runnable sendAllCompleted = new Runnable() {
        @Override
        public void run() {
            if (downloadingIds.size() != 0)
                return;
            long downloaded = 0;
            for (Map.Entry<String, Integer> entry : downloadedBytes.entrySet()) {
                downloaded += entry.getValue();
            }
            AppPreferenceManager.getInstance(context).saveLong(TIME, System.currentTimeMillis());
            DriverSession.get().getOverallStats().addBytes(downloaded);
            Broadcaster.get().sendCompleted();
            totalBytesMap.clear();
            downloadedBytes.clear();
            isDownloading = false;
            DataController.getInstance(context).setDownloading(false);

        }
    };
}
