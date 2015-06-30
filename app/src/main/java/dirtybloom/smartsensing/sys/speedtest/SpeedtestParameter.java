package dirtybloom.smartsensing.sys.speedtest;

import android.os.Bundle;

import dirtybloom.smartsensing.sys.SysBundle;
import dirtybloom.smartsensing.sys.SysConstants;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SpeedtestParameter extends SysBundle {

    public static final String HOST_KEY = SysConstants.KEY + "EXTRA_HOST";

    public static final String CONNECTION_TIMEOUT_KEY = SysConstants.KEY  + "EXTRA_CONNECTION_TIMEOUT";

    public SpeedtestParameter(Bundle bundle) {
        super(bundle);
        setHost(SysConstants.HOST);
        setConnectionTimeout(SysConstants.CONNECTION_TIMEOUT);
    }

    public String getHost(){
        return bundle.getString(HOST_KEY);
    }

    public void setHost(String host){
        bundle.putString(HOST_KEY, host);
    }

    public int getConnectionTimeout(){
        return bundle.getInt(CONNECTION_TIMEOUT_KEY);
    }

    public void setConnectionTimeout(int connectionTimeout){
        bundle.putInt(CONNECTION_TIMEOUT_KEY,connectionTimeout);
    }
}
