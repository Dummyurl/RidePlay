package rnf.taxiad.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Rahil on 19/1/16.
 */
public class DownloadBroadcast extends BroadcastReceiver {

    public static final String ACTION_PROGRESS = "PROGRESS";
    public static final String ACTION_ERROR = "ERROR";
    public static final String ACTION_COMPLETED = "COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
