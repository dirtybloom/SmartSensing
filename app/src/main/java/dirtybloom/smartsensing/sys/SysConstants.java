package dirtybloom.smartsensing.sys;

import android.location.LocationManager;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SysConstants {

    public static final String KEY = "dirtybloom.smartsensing.sys.";

    public static final float GEOLOCATION_ACCURACY = 100f;

    public static final String [] GEOLOCATION_PROVIDERS =
            new String[] { LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER };

    public static final long GEOLOCATION_TIMEOUT = 60 * 1000;

    public static final String HOST = "127.0.0.1"; //localhost

    public static final int CONNECTION_TIMEOUT = 3 * 1000;
}
