package dirtybloom.smartsensing.sys.speedtest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import dirtybloom.smartsensing.sys.SysReceiver;
import dirtybloom.smartsensing.sys.SysService;
import dirtybloom.smartsensing.sys.geolocation.GeolocationParameter;
import dirtybloom.smartsensing.sys.geolocation.GeolocationResult;
import dirtybloom.smartsensing.utils.WakefulThread;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SpeedtestService extends SysService {

    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;

    public static void requestSpeedtest(Context context,Intent intent){
        getPartialLock(context).acquire();
        intent.setClass(context, SpeedtestService.class);
        context.startService(intent);
    }

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

        SpeedtestParameter speedtestParameter = new SpeedtestParameter(intent.getExtras());

        SpeedtestThread speedtestThread = new SpeedtestThread(lock,connectivityManager,telephonyManager,speedtestParameter);
        speedtestThread.start();

        return START_REDELIVER_INTENT;
    }

    private class SpeedtestThread extends WakefulThread {

        private ConnectivityManager connectivityManager;

        private TelephonyManager telephonyManager;

        private SpeedtestParameter speedtestParameter;

        private HandlerTask handlerTask;

        private Handler handler;

        private Listener listener;

        private SpeedtestResult speedtestResult;


        public SpeedtestThread(PowerManager.WakeLock lock,ConnectivityManager connectivityManager,TelephonyManager telephonyManager,SpeedtestParameter speedtestParameter) {
            super("SpeedtestThread",lock);
            this.connectivityManager = connectivityManager;
            this.telephonyManager = telephonyManager;
            this.speedtestParameter = speedtestParameter;
            this.handlerTask = new HandlerTask();
            this.handler = new Handler();
            this.listener = new Listener();
        }

        private void setupResult(SpeedtestParameter speedtestParameter){
            Location location = speedtestParameter.getBundle().getParcelable(GeolocationResult.LOCATION_KEY);

            speedtestResult = new SpeedtestResult(new Bundle());
            speedtestResult.getBundle().putParcelable(GeolocationResult.LOCATION_KEY, location);
        }

        @Override
        protected void onUnlocked(){
            stopSelf();
        }

        @Override
        protected void onPreExecute(){
            Log.d("SpeedTestThread", "start");
            handler.postDelayed(handlerTask, 60 * 1000);
            telephonyManager.listen(listener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            quit();
        }

        @Override
        protected void onPostExecute(){

            telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);

            String downloadSpeed = SpeedtestHelper.checkDownloadSpeed(speedtestParameter.getHost(), speedtestParameter.getConnectionTimeout());
            if(downloadSpeed == SpeedtestHelper.UNKNOWN){
                broadcastError("unable to check download speed");
            }

            String uploadSpeed = SpeedtestHelper.checkUploadSpeed(speedtestParameter.getHost(),speedtestParameter.getConnectionTimeout());
            if(uploadSpeed == SpeedtestHelper.UNKNOWN){
                broadcastError("unable to check upload speed");
            }

            String latency = SpeedtestHelper.checkLatency(speedtestParameter.getHost(),speedtestParameter.getConnectionTimeout());

            String networkClass = SpeedtestHelper.getNetworkClass(connectivityManager);

            String networkOperatorName = telephonyManager.getNetworkOperatorName();

            speedtestResult.setDownloadSpeed(downloadSpeed);
            speedtestResult.setUploadSpeed(uploadSpeed);
            speedtestResult.setLatency(latency);
            speedtestResult.setNetworkOperatorName(networkOperatorName);
            speedtestResult.setNetworkClass(networkClass);
            speedtestResult.setManufacturer(Build.MANUFACTURER);
            speedtestResult.setModel(Build.MODEL);

            broadcastResult();
        }

        private Intent getIntentToBroadcast(){
            Intent intent = speedtestParameter.getIntentToBroadcast();
            intent.setPackage(getPackageName());
            intent.setClass(getApplicationContext(), SysReceiver.class);
            return intent;
        }

        private void broadcastResult(){
            Intent intent = getIntentToBroadcast();
            intent.setAction(SysReceiver.ACTION_SPEEDTEST_DONE);
            intent.putExtras(speedtestResult.getBundle());
            sendBroadcast(intent);
        }

        public void broadcastError(String error){
            Intent intent = getIntentToBroadcast();
            intent.setAction(SysReceiver.ACTION_SPEEDTEST_ERROR);
            intent.putExtra(SpeedtestResult.ERROR_KEY, error);
            sendBroadcast(intent);
        }

        private class HandlerTask implements Runnable{
            @Override
            public void run(){
                broadcastError("Unable to check dbm");
                quit();
            }
        }

        private class Listener extends PhoneStateListener{

            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                handler.removeCallbacks(handlerTask);
                telephonyManager.listen(this,LISTEN_NONE);

                setupResult(speedtestParameter);

                String dbmValue = String.valueOf(signalStrength.getGsmSignalStrength()).concat(" dbm");
                speedtestResult.setDbm(dbmValue);

                quit();
            }
        }
    }


}
