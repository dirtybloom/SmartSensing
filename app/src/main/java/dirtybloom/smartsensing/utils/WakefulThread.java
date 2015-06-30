package dirtybloom.smartsensing.utils;

import android.os.HandlerThread;
import android.os.PowerManager;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class WakefulThread extends HandlerThread {

    private PowerManager.WakeLock lock;

    public WakefulThread(String name,PowerManager.WakeLock lock) {
        super(name);
        this.lock = lock;
    }

    protected void onUnlocked(){}

    protected void onPreExecute(){}

    protected void onPostExecute(){}

    private final void unlock(){
        if(lock.isHeld()){
            lock.release();
        }

        if(!lock.isHeld()){
            onUnlocked();
        }
    }

    @Override
    public void onLooperPrepared(){
        try{
            onPreExecute();
        }catch(RuntimeException e){
            onPostExecute();
            unlock();
            throw e;
        }
    }

    @Override
    public void run(){
        try{
            super.run();
        }finally{
            onPostExecute();
            unlock();
        }
    }
}
