package dirtybloom.smartsensing.sys.geolocation;

import android.location.Location;
import android.os.Bundle;

import dirtybloom.smartsensing.sys.SysBundle;
import dirtybloom.smartsensing.sys.SysConstants;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class GeolocationResult extends SysBundle {

    public static final String LOCATION_KEY = SysConstants.KEY + "EXTRA_LOCATION";

    public static final String GEOLOCATION_ERROR_KEY = SysConstants.KEY + "EXTRA_ERROR";

    public GeolocationResult(Bundle bundle){
        super(bundle);
    }

    public Location getLocation(){
        return (Location) bundle.getParcelable(LOCATION_KEY);
    }

    public void setLocation(Location location){
        bundle.putParcelable(LOCATION_KEY, location);
    }

    public String getError(){
        return bundle.getString(GEOLOCATION_ERROR_KEY);
    }

    public void setError(String error){
        bundle.putString(GEOLOCATION_ERROR_KEY, error);
    }
}
