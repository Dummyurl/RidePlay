package rnf.taxiad.locations;

import android.location.Location;

/**
 * Created by Rahil on 14/12/15.
 */
public interface LocationListener {

    public void onLocationChanged(Location location, boolean current);

    public void onRequestPermission();
}
