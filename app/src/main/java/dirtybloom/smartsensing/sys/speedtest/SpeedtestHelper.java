package dirtybloom.smartsensing.sys.speedtest;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SpeedtestHelper {

    public static final String UNKNOWN = "?";

    public static String checkDownloadSpeed(String host,int connectionTimeout){

        try{

            URL url = new URL(host);

            long beforeTime = System.currentTimeMillis();

            URLConnection urlConnection = url.openConnection();

            urlConnection.setConnectTimeout(connectionTimeout);

            InputStream is = urlConnection.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);

            byte [] buffer = new byte[1024];

            int red;
            int size = 0;

            while((red = bis.read(buffer)) != -1){
                size += red;
            }

            bis.close();
            is.close();

            long afterTime = System.currentTimeMillis();

            long totalTime = afterTime - beforeTime;

            Log.d("checkDownloadSpeed() ", "download ended " + totalTime / 1000 + " secs");

            double rate = (((double) size / ((double)1024)) / ((double)totalTime/(double)1000)) * (double)8;

            String rateValue;
            if(rate > 1000){
                rateValue = String.valueOf(rate/(double)1024).concat(" Mbps");
            }else{
                rateValue = String.valueOf(rate).concat(" Kbps");
            }

            return rateValue;

        }catch(IOException e){
            return UNKNOWN;
        }
    }

    public static String checkUploadSpeed(String host,int connectionTimeout){

        try {
            URL url = new URL(host);

            long beforeTime = System.currentTimeMillis();

            URLConnection urlConnection = url.openConnection();

            urlConnection.setConnectTimeout(connectionTimeout);

            OutputStream os = urlConnection.getOutputStream();

            BufferedOutputStream bos = new BufferedOutputStream(os);

            byte[] buffer = new byte[1024];

            int size = buffer.length;

            bos.write(buffer);
            bos.flush();
            bos.close();

            os.close();

            long afterTime = System.currentTimeMillis();

            long totalTime = afterTime - beforeTime;

            Log.d("checkUploadSpeed()", "upload ended " + totalTime/1000 + " secs");

            double rate = (((double) size / (double) 1024) / ((double)totalTime / (double)1000)) * (double)8;

            String rateValue;

            if(rate > 1000){
                 rateValue = String.valueOf(rate/(double)1024).concat(" Mbps");
            }else{
                rateValue = String.valueOf(rate).concat(" Kbps");
            }

            return rateValue;

        }catch(IOException e){
            return UNKNOWN;
        }
    }

    public static String getNetworkClass(ConnectivityManager connectivityManager){

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){

            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){

                return networkInfo.getSubtypeName();
            }
        }
        return UNKNOWN;
    }

    /*switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
            case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
            case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                return "4G";
            default:
                return "?";
         }*/

    //TODO : completare controllo latenza
    public static String checkLatency(String host,int connectionTimeout){

        long latency = 0;

        String latencyValue = String.valueOf(latency).concat(" millis");

        return latencyValue;

    }
}
