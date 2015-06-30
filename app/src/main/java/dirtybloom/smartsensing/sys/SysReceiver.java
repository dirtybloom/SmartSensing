package dirtybloom.smartsensing.sys;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import dirtybloom.smartsensing.sys.geolocation.GeolocationParameter;
import dirtybloom.smartsensing.sys.geolocation.GeolocationResult;
import dirtybloom.smartsensing.sys.geolocation.GeolocationService;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SysReceiver extends BroadcastReceiver {

    public SysReceiver() {
    }

    public static final String ACTION_REQUEST_LOCATION = SysConstants.KEY + "ACTION_REQUEST_LOCATION";

    public static final String ACTION_GEOLOCATION_DONE = SysConstants.KEY + "ACTION_GEOLOCATION_DONE";

    public static final String ACTION_GEOLOCATION_ERROR = SysConstants.KEY + "ACTION_GEOLOCATION_ERROR";

    public static final String ACTION_REQUEST_SPEEDTEST = SysConstants.KEY + "ACTION_REQUEST_SPEEDTEST";

    public static final String ACTION_SPEEDTEST_DONE = SysConstants.KEY + "ACTION_SPEEDTEST_DONE";

    public static final String ACTION_SPEEDTEST_ERROR = SysConstants.KEY + "ACTION_SPEEDTEST_ERROR";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            scheduleOperations(context);

        }else if(action.equals(ACTION_REQUEST_LOCATION)){
            Log.d("SysReceiver",ACTION_REQUEST_LOCATION);
            GeolocationService.requestLocation(context, intent);

        }else if(action.equals(ACTION_GEOLOCATION_DONE)){
            Log.d("SysReceiver",ACTION_GEOLOCATION_DONE);
            GeolocationResult geolocationResult = new GeolocationResult(intent.getExtras());
            handleLocation(geolocationResult.getLocation());

        }else if(action.equals(ACTION_GEOLOCATION_ERROR)){
            Log.d("SysReceiver",ACTION_GEOLOCATION_ERROR);
            GeolocationResult geolocationResult = new GeolocationResult(intent.getExtras());
            handleGeolocationError(geolocationResult.getError());

        }else if(action.equals(ACTION_REQUEST_SPEEDTEST)){


        }else if(action.equals(ACTION_SPEEDTEST_DONE)){
            handleSpeedtestResult();

        }else if(action.equals(ACTION_SPEEDTEST_ERROR)){
            handleSpeedtestError();
        }
    }



    public static void scheduleOperations(Context context){

        Intent intent = new Intent();
        intent.setClass(context, SysReceiver.class);
        intent.setAction(ACTION_REQUEST_LOCATION);

        GeolocationParameter geolocationParameter = new GeolocationParameter(new Bundle());
        geolocationParameter.setIntentToBroadcast(new Intent());

        intent.putExtras(geolocationParameter.getBundle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,1000,pendingIntent);
    }

    private void handleLocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Log.d("handleLocation", "latitude = " + latitude + " longitude = " + longitude);
    }

    private void handleGeolocationError(String error){

        Log.d("handleGeolocationError", error);
    }

    private void handleSpeedtestResult(){}

    private void handleSpeedtestError(){}
}
