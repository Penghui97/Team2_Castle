package com.example.mywork2.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.mywork2.LogInActivity;

import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;
/**
 * @author  Penghui Xiao
 * function: static methods to get WiFi connection information
 * modification date and description can be found in github repository history
 */

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
        String ssid = wifiInfo.getSSID();
        int networkId = wifiInfo.getNetworkId();
        @SuppressLint("MissingPermission") List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfiguration : configuredNetworks){
            if(wifiConfiguration.networkId==networkId)
                ssid = wifiConfiguration.SSID;
        }
        Log.e("wifiInfo", wifiInfo.toString());
        Log.e("SSID",wifiInfo.getSSID());
        return ssid.replace("\"","");
    }

//    public static String getID(Context context){
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        return networkInfo.getExtraInfo();
//    }


}
