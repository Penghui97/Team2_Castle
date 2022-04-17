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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


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
    //drawerImage is the imageview in the drawerlayout. headImage is the imageview in drawer_head
    private ImageView imageView, drawerImage;

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * (Jing)
         * get the user by username
         */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String username = (String)extras.get("username");
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao userDao = new UserDao();
                user = userDao.getUserByUsername(username);
            }
        }).start();


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





    }

    //override onresume function to get the information changed.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        //init data, 23.2
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





    /**
     * Methods to return home page directly
     * @param keyCode
     * @param event
     * @return
     */
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
}