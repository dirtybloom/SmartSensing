package dirtybloom.smartsensing.sys.speedtest;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

import dirtybloom.smartsensing.sys.SysService;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SpeedtestService extends SysService {

    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;

    @Override
    public void onCreate(){
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        PowerManager.WakeLock lock = getPartialLock(getApplicationContext());
        if(!lock.isHeld() || (flags & START_FLAG_REDELIVERY) != 0){
            lock.acquire();
        }

        return START_REDELIVER_INTENT;
    }

    private class SpeedtestThread extends HandlerThread{

        private ConnectivityManager connectivityManager;

        private TelephonyManager telephonyManager;

        private SpeedtestParameter speedtestParameter;


        public SpeedtestThread(String name,ConnectivityManager connectivityManager,TelephonyManager telephonyManager,SpeedtestParameter speedtestParameter) {
            super(name);
            this.connectivityManager = connectivityManager;
            this.telephonyManager = telephonyManager;
            this.speedtestParameter = speedtestParameter;
        }
    }
}
