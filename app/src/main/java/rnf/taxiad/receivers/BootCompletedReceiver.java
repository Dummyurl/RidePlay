package rnf.taxiad.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rnf.taxiad.locations.LocationService;
import rnf.taxiad.models.DriverSession;

/**
 * Created by Rahil on 24/2/16.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DriverSession.get().isHasSession())
            context.startService(new Intent(context, LocationService.class));
    }
}
