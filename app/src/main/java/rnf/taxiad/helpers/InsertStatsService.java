package rnf.taxiad.helpers;

import android.app.IntentService;
import android.content.Intent;

import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.controllers.DataController;
import rnf.taxiad.controllers.StatsController;
import rnf.taxiad.models.Stats;

/**
 * Created by Rahil on 3/3/16.
 */
public class InsertStatsService extends IntentService {

    private static final String NAME = "InsertStatsService";
    public static final String ADD_ID = "ADD_ID";
    public static final String PLAYED = "PLAYED";
    public static final String VIEWED = "VIEWED";
    public static final String TAPED = "TAPED";
    public static final String DURATION = "DURATION";
    public static final String ADVERTISER_ID = "ADVERTISER_ID";

    public InsertStatsService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String currentDate = GlobalUtils.getCurrentDate();

            String key = GlobalUtils.formatTimestamp(System.currentTimeMillis(), "yyyy-MM-dd");

            String adId = intent.getStringExtra(ADD_ID);

            int played = intent.getIntExtra(PLAYED, -1);

            int viewed = intent.getIntExtra(VIEWED, 0);
            ;
            int taped = intent.getIntExtra(TAPED, 0);
            ;
            int duration = intent.getIntExtra(DURATION, 0);
            ;
            String advertiserId = intent.getStringExtra(ADVERTISER_ID);

            final StatsController statsController = new StatsController(this);
            if (DataController.getInstance(this).getLocationModule() == null)
                return;
            final Stats stats = new Stats();
            stats.adId = adId;
            String lat = DataController.getInstance(this).getLocationModule().getCurrentLatLong() == null ? ""
                    : DataController.getInstance(this).getLocationModule().getCurrentLatLong()[0] + "";
            String longt = DataController.getInstance(this).getLocationModule().getCurrentLatLong() == null ? ""
                    : DataController.getInstance(this).getLocationModule().getCurrentLatLong()[1] + "";

            // stats.latitude = String.valueOf(DataController.getInstance(this).getLocationModule().getCurrentLatLong()[0]);
            //stats.longitude = String.valueOf(DataController.getInstance(this).getLocationModule().getCurrentLatLong()[1]);

            stats.latitude = lat;
            stats.longitude = longt;
            stats.city = DataController.getInstance(this).getLocationModule().getCurrentCity();
            stats.address = DataController.getInstance(this).getLocationModule().getCurrentAddressLine();
            stats.advertiserId = advertiserId;
            stats.played = String.valueOf(played);
            stats.viewed = String.valueOf(viewed);
            stats.taped = String.valueOf(taped);
            stats.duration = String.valueOf(duration);
            stats.date = currentDate;
            stats.key = key;
            statsController.insertStats(stats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
