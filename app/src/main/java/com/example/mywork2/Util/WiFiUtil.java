package com.example.mywork2.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.mywork2.LogInActivity;

import static androidx.core.content.ContextCompat.getSystemService;


public class WiFiUtil {

    //check wifi connected or not
    public static String isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected()){
            return "true";
        }else return "false";

    }

    //check wifi ssid
    public static String getConnectWifiSsid(WifiManager wifiManager){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.e("wifiInfo", wifiInfo.toString());
        Log.e("SSID",wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }


}
