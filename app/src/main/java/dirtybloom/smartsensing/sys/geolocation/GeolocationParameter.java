package dirtybloom.smartsensing.sys.geolocation;

import android.location.LocationManager;
import android.os.Bundle;

import dirtybloom.smartsensing.sys.SysBundle;
import dirtybloom.smartsensing.sys.SysConstants;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class GeolocationParameter extends SysBundle {

    public static final long GEOLOCATION_DEFAULT_TIMEOUT = 60 * 1000;

    public static final float GEOLOCATION_DEFAULT_ACCURACY = 100f;

    public static final String [] GEOLOCATION_DEFAULT_PROVIDERS =
            new String[] { LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER };

    public static final String GEOLOCATION_TIMEOUT_KEY = SysConstants.KEY + "EXTRA_GEOLOCATION_TIMEOUT";

    public static final String GEOLOCATION_ACCURACY_KEY = SysConstants.KEY + "EXTRA_GEOLOCATION_ACCURACY";

    public static final String GEOLOCATION_PROVIDERS_KEY = SysConstants.KEY + "EXTRA_GEOLOCATION_PROVIDERS";

    public GeolocationParameter(Bundle bundle){
        super(bundle);
        setTimeout(GEOLOCATION_DEFAULT_TIMEOUT);
        setAccuracy(GEOLOCATION_DEFAULT_ACCURACY);
        setProviders(GEOLOCATION_DEFAULT_PROVIDERS);
    }

    public long getTimeout(){
        return bundle.getLong(GEOLOCATION_TIMEOUT_KEY);
    }

    public void setTimeout(long timeout){
        bundle.putLong(GEOLOCATION_TIMEOUT_KEY, timeout);
    }

    public float getAccuracy(){
        return bundle.getFloat(GEOLOCATION_ACCURACY_KEY);
    }

    public void setAccuracy(float accuracy){
        bundle.putFloat(GEOLOCATION_ACCURACY_KEY, accuracy);
    }

    public String [] getProviders(){
        return bundle.getStringArray(GEOLOCATION_PROVIDERS_KEY);
    }

    public void setProviders(String [] providers){
        bundle.putStringArray(GEOLOCATION_PROVIDERS_KEY, providers);
    }
}
