package rnf.taxiad.helpers;

/**
 * Created by neha on 16/3/16.
 */
public interface DownloadCompleted {

    public void onProgress(int total, int downloaded);

    public void onCompleted();

    public void onError(String adId);

    public void onCompleted(String id);
}
