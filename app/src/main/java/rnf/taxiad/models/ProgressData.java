package rnf.taxiad.models;

import java.io.Serializable;

/**
 * Created by Rahil on 18/2/16.
 */
public class ProgressData implements Serializable {

    public long totalBytes = 0L;
    public long downloadedBytes = 0L;
    public boolean isDownloading = false;
}
