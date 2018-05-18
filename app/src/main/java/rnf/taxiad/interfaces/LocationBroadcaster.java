package rnf.taxiad.interfaces;

import rnf.taxiad.models.LocationModule;

/**
 * Created by Rahil on 25/2/16.
 */
public interface LocationBroadcaster {

    void onLocationChanged(LocationModule locationModule);

    void onRequestPermission();
}
