package rnf.taxiad.helpers;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Rahil on 29/2/16.
 */
public class DistanceService extends IntentService {

    private static final String NAME = "DistanceService";
    public static final String LAST_LOCATION = "LAST_LOCATION";
    public static final String CURRENT_LOCATION = "CURRENT_LOCATION";

    public DistanceService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        try {
//            final Location lastLocation = (Location) intent.getParcelableExtra(LAST_LOCATION);
//            final Location currentLocation = (Location) intent.getParcelableExtra(CURRENT_LOCATION);
////            double lastLat = GlobalUtils.getTwoDecimal(lastLocation.getLatitude());
////            double lastLong = GlobalUtils.getTwoDecimal(lastLocation.getLongitude());
////            double currentLat = GlobalUtils.getTwoDecimal(currentLocation.getLatitude());
////            double currentLong = GlobalUtils.getTwoDecimal(currentLocation.getLongitude());
////            if (lastLat == currentLat && lastLong == currentLong)
////                return;
//            float distance = lastLocation.distanceTo(currentLocation);
//            DriverSession.get().getOverallStats().addDistance(distance);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
