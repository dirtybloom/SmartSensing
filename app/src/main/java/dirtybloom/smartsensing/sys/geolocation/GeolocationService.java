package dirtybloom.smartsensing.sys.geolocation;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;

import dirtybloom.smartsensing.sys.SysReceiver;
import dirtybloom.smartsensing.sys.SysService;
import dirtybloom.smartsensing.utils.WakefulThread;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class GeolocationService extends SysService {

    private LocationManager locationManager;

    public static void requestLocation(Context context,Intent intent){
        getPartialLock(context).acquire();
        intent.setClass(context, GeolocationService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        PowerManager.WakeLock lock = getPartialLock(getApplicationContext());
        if(!lock.isHeld() || (flags & START_FLAG_REDELIVERY) != 0){
            lock.acquire();
        }

        GeolocationThread geolocationThread = new GeolocationThread(lock, locationManager, new GeolocationParameter(intent.getExtras()));
        geolocationThread.start();

        return START_REDELIVER_INTENT;
    }

    private class GeolocationThread extends WakefulThread {

        private LocationManager locationManager;

        private GeolocationParameter geolocationParameter;

        private Listener listener;

        private HandlerTask handlerTask;

        private Handler handler;

        private int currentProviderIndex;

        public GeolocationThread(PowerManager.WakeLock lock,LocationManager locationManager,GeolocationParameter geolocationParameter) {
            super("GeolocationThread",lock);
            this.locationManager = locationManager;
            this.geolocationParameter = geolocationParameter;
            this.listener = new Listener();
            this.handlerTask = new HandlerTask();
            this.handler = new Handler();
            this.currentProviderIndex = 0;
        }

        @Override
        protected void onUnlocked(){
            stopSelf();
        }

        @Override
        protected void onPreExecute(){
            requestLocation();
        }

        @Override
        protected void onPostExecute(){
            locationManager.removeUpdates(listener);
        }

        private void requestLocation(){
            handler.postDelayed(handlerTask,geolocationParameter.getTimeout());
            locationManager.requestLocationUpdates(getCurrentProvider(), 0, 0, listener);
        }

        private String getCurrentProvider(){
            return geolocationParameter.getProviders()[currentProviderIndex];
        }

        private boolean hasNextProvider(){
            return currentProviderIndex < geolocationParameter.getProviders().length - 1;
        }

        private class Listener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {
                handler.removeCallbacks(handlerTask);

                //if accuracy is fine broadcast result
                if(location.getAccuracy() <= geolocationParameter.getAccuracy()){
                    locationManager.removeUpdates(listener);
                    broadcastLocation(location);
                    quit();
                }else{
                    handler.postDelayed(handlerTask, geolocationParameter.getTimeout());
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}


        }

        private Intent getIntentToBroadcast(){
            Intent intent = geolocationParameter.getIntentToBroadcast();
            intent.setClass(getApplicationContext(), SysReceiver.class);
            intent.setPackage(getPackageName());
            return intent;
        }

        private void broadcastLocation(Location location){
            Intent intent = getIntentToBroadcast();
            intent.setAction(SysReceiver.ACTION_GEOLOCATION_DONE);
            intent.putExtra(GeolocationResult.LOCATION_KEY, location);
            sendBroadcast(intent);
        }

        private void broadcastError(String error){
            Intent intent = getIntentToBroadcast();
            intent.setAction(SysReceiver.ACTION_GEOLOCATION_ERROR);
            intent.putExtra(GeolocationResult.GEOLOCATION_ERROR_KEY, error);
            sendBroadcast(intent);
        }

        private class HandlerTask implements Runnable{
            @Override
            public void run(){
                locationManager.removeUpdates(listener);
                if(hasNextProvider()){
                    currentProviderIndex++;
                    requestLocation();
                }else{
                    broadcastError("Unable to find location!!");
                    quit();
                }
            }
        }
    }
}
