package rnf.taxiad.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.models.ProgressData;

/**
 * Created by Rahil on 18/2/16.
 */
public class ProgressController {

    public static final int PROGRESS_DATA = 0;
    public static final int COMPLETED = 1;
    public static final String LAST_DOWNLOAD = "rnf.taxiad.LAST_DOWNLOAD";
    private static ProgressController instance;
    private HashMap<Integer, ProgressData> map = new HashMap<>();
    private HashMap<Integer, Boolean> mapCompleted = new HashMap<>();
    private WeakReference<Handler> handlerWeakReference;
    private Context context;

    private ProgressController(Context context) {
        super();
        this.context = context;
    }

    public static synchronized ProgressController get(Context context) {
        if (instance == null)
            instance = new ProgressController(context);
        return instance;
    }

    public void addHandler(Handler handler) {
        handlerWeakReference = new WeakReference<Handler>(handler);
    }

    public synchronized void publishProgress(int id, long totalBytes, long downloadedBytes) {
        if (handlerWeakReference == null)
            return;
        Handler handler = handlerWeakReference.get();
        if (handler == null)
            return;
        ProgressData data = map.get(id);
        if (data == null)
            data = new ProgressData();
        map.put(id, data);
        data.totalBytes = totalBytes;
        data.downloadedBytes = downloadedBytes;
        Message msg = handler.obtainMessage();
        msg.what = PROGRESS_DATA;
        msg.obj = map;
        handler.sendMessage(msg);
    }

    public void complete(int id) {
        mapCompleted.put(id, true);
        setCompleted(id);
    }

    public void setCompleted(int id) {
        if (!isAllCompleted())
            return;
        DriverSession.get().getOverallStats().setDownloaded(true);
        AppPreferenceManager.getInstance(context).saveLong(LAST_DOWNLOAD, System.currentTimeMillis());
        if (handlerWeakReference == null)
            return;
        Handler handler = handlerWeakReference.get();
        if (handler == null)
            return;
        Message msg = handler.obtainMessage();
        msg.what = COMPLETED;
        handler.sendMessage(msg);
    }

    private boolean isAllCompleted() {
        for (Map.Entry<Integer, Boolean> entry : mapCompleted.entrySet()
                ) {
            boolean isComplete = entry.getValue();
            if (!isComplete)
                return false;
        }
        return true;
    }

    public long getLastDownload() {
        return AppPreferenceManager.getInstance(context).getLong(LAST_DOWNLOAD, 0L);
    }

    public void remove(int id) {
        map.remove(id);
        mapCompleted.remove(id);
    }
}
