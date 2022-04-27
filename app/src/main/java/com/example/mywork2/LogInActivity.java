package com.example.mywork2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork2.Util.WiFiUtil;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    //check wifi
    WifiManager wifiManager;
    String ssid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        FragmentContainerView fragmentContainerView = findViewById(R.id.log_in_nav_host_frag);
        NavHostFragment navHostFragment = fragmentContainerView.getFragment();
        //get Navigation Controller from the Host fragment
        NavController navController = navHostFragment.getNavController();

        //check if wifi belongs to newcastle university
        checkWifi();


    }

    private void checkWifi() {
        //if wifi is not connected
         wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (WiFiUtil.isWifiConnected(getApplicationContext()).equals("false")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.nowifi).setNegativeButton("OK"
                    , (dialogInterface,i) -> dialogInterface.dismiss()).show();
        }else {//wifi connected, check wifi ssid
            WiFiUtil wiFiUtil = new WiFiUtil();
            ssid = WiFiUtil.getConnectWifiSsid(wifiManager);
            if (!(ssid.contains("newcastle")||ssid.contains("eduroam"))){//not campus wifi
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.yourssid)+ ssid+
                        ". "+getString(R.string.campuswifi)).setNegativeButton("OK"
                        , (dialogInterface,i) -> dialogInterface.dismiss()).show();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

}