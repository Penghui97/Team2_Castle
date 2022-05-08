package com.example.mywork2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork2.LogInFragment.LogInMainFragment;
import com.example.mywork2.LogInFragment.UserLogInFragment;
import com.example.mywork2.Util.WiFiUtil;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    //check wifi
    WifiManager wifiManager;
    String ssid, username, password,remName, RemPass,forgetName, forgetPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        FragmentContainerView fragmentContainerView = findViewById(R.id.log_in_nav_host_frag);
        NavHostFragment navHostFragment = fragmentContainerView.getFragment();
        //get Navigation Controller from the Host fragment
        NavController navController = navHostFragment.getNavController();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        try{
            //new account
            username = (String) extras.get("newUser");
            password = (String) extras.get("Password_");
            //remember user
            remName = (String) extras.get("remName");
            RemPass = (String) extras.get("RemPass");
            //forget password
            forgetName = (String) extras.get("username");
            forgetPass = (String) extras.get("password");

            //remember username and password
            SharedPreferences getUsername = getSharedPreferences("remName", MODE_PRIVATE);
            remName = getUsername.getString("remName","");
            RemPass = getUsername.getString("RemPass","");



        }
        catch (Exception e){
            e.printStackTrace();
        }

        //remember account
//        SharedPreferences spfRecord = getSharedPreferences("remName", MODE_PRIVATE);
//        SharedPreferences.Editor edit = spfRecord.edit();
//        edit.putString("remName", remName);
//        edit.putString("RemPass", RemPass);
//        edit.apply();

        //check if wifi belongs to newcastle university
        checkWifi();

        //login with the new account
        if (username!=null&&password!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.newaccountsignin)).setNegativeButton("Cancel"
                    , (dialogInterface,i) -> dialogInterface.dismiss()).setPositiveButton("Ok",
                    ((dialogInterface, i) -> {
                        if(username!=null&&password!=null){
                            //传参
                            //transmit data to login fragment
                            UserLogInFragment userLogInFragment = new UserLogInFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("username",username);
                            bundle.putString("password",password);
                            userLogInFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.log_in_nav_host_frag,userLogInFragment);
                            fragmentTransaction.commit();
                        }
                        navController.navigate(R.id.action_logInMainFragment_to_userLogInFragment);
                    })).show();

        }else if(forgetName!=null&&forgetPass!=null){//password is found
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.usernameis)+forgetName+"\n"+getString(R.string.passwordis)
            +forgetPass+"\n"+getString(R.string.loginornot)).setNegativeButton("Cancel"
                    , (dialogInterface,i) -> dialogInterface.dismiss()).setPositiveButton("Ok",
                    ((dialogInterface, i) -> {
                        if(forgetPass!=null&&forgetName!=null){
                            //传参
                            //transmit data to login fragment
                            UserLogInFragment userLogInFragment = new UserLogInFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("username",forgetName);
                            bundle.putString("password",forgetPass);
                            userLogInFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.log_in_nav_host_frag,userLogInFragment);
                            fragmentTransaction.commit();
                        }
                        navController.navigate(R.id.action_logInMainFragment_to_userLogInFragment);
                    })).show();
        }
        else {
            //remembered account
            if(!Objects.equals(remName, "") && !Objects.equals(RemPass, "")){
                if (remName!=null&&RemPass!=null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.loginremember)).setNegativeButton("Cancel"
                            , (dialogInterface,i) -> dialogInterface.dismiss()).setPositiveButton("Ok",
                            ((dialogInterface, i) -> {
                                if(remName!=null&&RemPass!=null){
                                    //传参
                                    //transmit data to login fragment
                                    UserLogInFragment userLogInFragment = new UserLogInFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username",remName);
                                    bundle.putString("password",RemPass);
                                    userLogInFragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.log_in_nav_host_frag,userLogInFragment);
                                    fragmentTransaction.commit();
                                }
                                navController.navigate(R.id.action_logInMainFragment_to_userLogInFragment);
                            })).show();
                }
            }
        }



    }

    private void checkWifi() {
         wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
         assert wifiManager != null;
        if (WiFiUtil.isWifiConnected(getApplicationContext()).equals("false")){//if wifi is not connected
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.nowifi).setNegativeButton(getString(R.string.wlansetting)
                    , (dialogInterface,i) -> {
                        dialogInterface.dismiss();
                        //open WiFi setting
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        finish();
                    }).show();
        }else {
            //wifi connected, check wifi ssid
            //安卓9.0以上版本进行定位权限获取
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {//if lower than android 9
                ssid = WiFiUtil.getConnectWifiSsid(wifiManager);
            }else {//higher than Android 9
                if(ContextCompat.checkSelfPermission(LogInActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
                    //申请定位权限,200是标识码
                    ActivityCompat.requestPermissions(LogInActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                }
                wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                ssid = wifiInfo.getSSID();
            }
            Log.e("-----------", ssid);
            alertWifi();


        }
    }

    //create alert message for wifi
    private void alertWifi() {
        if (ssid.contains("unknown ssid")) {//cannot get ssid
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.cannotgetwifi) +
                    ". " + getString(R.string.campuswifi)).setNegativeButton("OK"
                    , (dialogInterface, i) -> dialogInterface.dismiss()).show();
        } else if (ssid.contains("AndroidWifi")) {//emulator
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.emulator))
                    .setNegativeButton("OK"
                            , (dialogInterface, i) -> dialogInterface.dismiss()).show();


        } else {
            if (!(ssid.contains("newcastle") || ssid.contains("eduroam"))) {//not campus wifi
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.yourssid) + ssid +
                        ". " + getString(R.string.campuswifi)).setNegativeButton(getString(R.string.wlansetting)
                        , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            //open WiFi setting
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            finish();

                        }).setPositiveButton(getString(R.string.withoutwifi), ((dialogInterface, i)
                        -> dialogInterface.dismiss())).show();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

}