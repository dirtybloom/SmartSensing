package dirtybloom.smartsensing.sys;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SysService extends Service {

    public static final String LOCK_NAME_STATIC = SysConstants.KEY + "PARTIAL_LOCK";

    private static PowerManager.WakeLock partialLock;

    synchronized public static PowerManager.WakeLock getPartialLock(Context context){
        if(partialLock == null){
            PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
            partialLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
            partialLock.setReferenceCounted(true); //Just to be sure
        }
        return partialLock;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
