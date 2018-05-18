package rnf.taxiad.helpers;

import java.lang.ref.WeakReference;

/**
 * Created by neha on 16/3/16.
 */
public class Broadcaster {

    private static Broadcaster broadcaster;
    private WeakReference<DownloadCompleted> weakReference;

    private Broadcaster() {
        super();
    }

    public synchronized static Broadcaster get() {
        if (broadcaster == null)
            broadcaster = new Broadcaster();
        return broadcaster;

    }

    public void addDownloadCompleteListener(DownloadCompleted listener) {
        this.weakReference = new WeakReference<DownloadCompleted>(listener);
    }

    public void sendProgress(int total, int downloaded) {
        if (weakReference == null)
            return;
        if (weakReference.get() == null)
            return;
        weakReference.get().onProgress(total, downloaded);
    }

    public void sendCompleted() {
        if (weakReference == null)
            return;
        if (weakReference.get() == null)
            return;
        weakReference.get().onCompleted();
    }

    public void sendCompleted(String id) {
        if (weakReference == null)
            return;
        if (weakReference.get() == null)
            return;
        weakReference.get().onCompleted(id);
    }

    public void onError(String adId) {
        if (weakReference == null)
            return;
        if (weakReference.get() == null)
            return;
        weakReference.get().onError(adId);
    }

}
