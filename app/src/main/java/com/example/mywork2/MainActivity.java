package com.example.mywork2;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mywork2.MyAccount.AppSettingsActivity;
import com.example.mywork2.Util.ImageUtil;
import com.example.mywork2.Util.UserThreadLocal;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;
import com.example.mywork2.domain.Ticket;
import com.example.mywork2.domain.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    //drawerImage is the imageview in the drawerlayout.
    private ImageView imageView, drawerImage;
    //username_v is the username textview.
    private TextView username_v, email_v;
    public User user, customer;
    public String username;


    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:
                    username_v.setText(username);//set the username on the view
                    email_v.setText(user.getEmail());//set the email on the view
                    break;
                case 0x22:
                    username_v.setText(customer.getUsername());//set the customer's username on the view
                    email_v.setText(customer.getEmail());//set the customer's email on the view
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * (Jing)
         * get the user by username
         */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = (String)extras.get("username");
        //show the particular user's info
        showUserInfo();

        //
        Intent intent2 = new Intent(MainActivity.this, AppSettingsActivity.class);
        intent2.putExtra("username", username);



        setContentView(R.layout.drawer_main);//avatar

        //init view, 23.2
        initView();

        //ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation
        BottomNavigationView botNavView = findViewById(R.id.bottomNavigationView);
        FragmentContainerView fragmentContainerView = findViewById(R.id.mainpage_fragment_container_view_tag);
        NavHostFragment navHostFragment = fragmentContainerView.getFragment();
        //get Navigation Controller from the Host fragment
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(botNavView,navController);

        //drawer Navigation
        drawer = findViewById(R.id.drawer_layout);
        NavigationView drawerView = findViewById(R.id.drawerNavView);
        NavigationUI.setupWithNavController(drawerView,navController);

        CardView account = findViewById(R.id.account_avatar);
        account.setOnClickListener(this);

        ImageView more = findViewById(R.id.app_helper);
        more.setOnClickListener(this);

        CardView account_avatar_head = drawerView.getHeaderView(0).findViewById(R.id.account_avatar_head);
        account_avatar_head.setOnClickListener(this);

        drawerImage = drawerView.getHeaderView(0).findViewById(R.id.avatar);

        username_v = drawerView.getHeaderView(0).findViewById(R.id.username_profile);

        email_v = drawerView.getHeaderView(0).findViewById(R.id.user_email);


    }

    //override on_resume function to get the information changed.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        //init data, 23.2
        initData();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_avatar:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.app_helper:
                Intent intent = new Intent(MainActivity.this,AboutThisApp.class);
                startActivity(intent);
                break;
            case R.id.account_avatar_head:
                Intent intent1 = new Intent(MainActivity.this,Avatar.class);
                startActivity(intent1);
            default:
                break;

        }
    }

    /**
     * method to initialize views
     */
    private void initView(){
        imageView = findViewById(R.id.avatar);
    }

    /**
     * methods to initialize data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData(){

        showUserInfo();
        getDataFromSpf();
    }

    //get data
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromSpf(){
        SharedPreferences spfRecord = getSharedPreferences("spfRecord", MODE_PRIVATE);
        String image64 = spfRecord.getString("image_64","");
        imageView.setImageBitmap(ImageUtil.base64ToImage(image64));
        drawerImage.setImageBitmap(ImageUtil.base64ToImage(image64));

        //change avatars in other layouts
        //created by Penghui
        //reference from https://blog.csdn.net/yu_qiushui/article/details/84784958
//        LayoutInflater inflater1 = LayoutInflater.from(MainActivity.this);
//        View v = inflater1.inflate(R.layout.activity_avatar,null);
//        avatar2 = (ImageView) v.findViewById(R.id.avatar);
//        avatar2.setImageBitmap(ImageUtil.base64ToImage(image64));

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //show the particular user's info
    public void showUserInfo(){
        new Thread(() -> {
            UserDao userDao = new UserDao();
            user = userDao.getUserByUsername(username);
            customer = userDao.getUserByUsername("root");

            if(user != null){
                Message message = handler.obtainMessage();
                message.what = 0x11;
                handler.sendMessage(message);
            }else {
                Message message = handler.obtainMessage();
                message.what = 0x22;
                handler.sendMessage(message);
            }
        }).start();
    }
}