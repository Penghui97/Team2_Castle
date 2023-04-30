package com.example.mywork2;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mywork2.MyAccount.AccountEditActivity;
import com.example.mywork2.MyAccount.AppSettingsActivity;
import com.example.mywork2.MyAccount.CommentsActivity;
import com.example.mywork2.Util.ImageUtil;
import com.example.mywork2.dao.AvatarDao;
import com.example.mywork2.dao.UserDao;
import com.example.mywork2.domain.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

/**
 * @author Penghui Xiao(methods, function),Jiacheng(UI behavior)
 * function: show user main page, update user information from database and local cache,
 * navigate to other pages(with data transmissions)
 * modification date and description can be found in github repository history
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    @SuppressLint("StaticFieldLeak")
    private static NavController navController;
    //drawerImage is the imageview in the drawerlayout.
    private ImageView imageView, drawerImage;
    //nickname_v is the username textview.
    private TextView nickname_v, email_v;
    public User user, customer;
    public String username, nickname, passW;
    String lang, imageBase64;
    int version;


    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:
                    nickname_v.setText(nickname);//set the username on the view
                    email_v.setText(user.getEmail());//set the email on the view
                    break;
                case 0x22:
                    nickname_v.setText(customer.getNickname());//set the customer's username on the view
                    email_v.setText(customer.getEmail());//set the customer's email on the view
                    break;
                case 0x33://set avatar
                    imageView.setImageBitmap(ImageUtil.base64ToImage(imageBase64));
                    drawerImage.setImageBitmap(ImageUtil.base64ToImage(imageBase64));
                    getAvatarFromDB();
                    imageView.setImageBitmap(ImageUtil.base64ToImage(imageBase64));
                    drawerImage.setImageBitmap(ImageUtil.base64ToImage(imageBase64));
                    break;
                case 0x44:
                    noAvatar();
            }
        }
    };

    public static void toMyPlans(){
        navController.navigate(R.id.myPlansFragment);
    }

    private void noAvatar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.noavatar).setNegativeButton("OK"
                , (dialogInterface,i) -> dialogInterface.dismiss()).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //get username from login page
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = (String)extras.get("username");
        passW = (String) extras.get("password");
        if(username == null || username.equals("")){
            username = "root";
        }
        //show the particular user's info


        showUserInfo();

        setContentView(R.layout.drawer_main);

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
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(botNavView,navController);

        //drawer Navigation
        drawer = findViewById(R.id.drawer_layout);
        NavigationView drawerView = findViewById(R.id.drawerNavView);
        NavigationUI.setupWithNavController(drawerView,navController);

        //the account avatar
        CardView account = findViewById(R.id.account_avatar);
        account.setOnClickListener(this);

        //the more button
        ImageView more = findViewById(R.id.app_helper);
        more.setOnClickListener(this);

        //the drawer navigation bar
        CardView account_avatar_head = drawerView.getHeaderView(0).findViewById(R.id.account_avatar_head);
        account_avatar_head.setOnClickListener(this);

        // the user information in the drawer header
        drawerImage = drawerView.getHeaderView(0).findViewById(R.id.avatar);

        nickname_v = drawerView.getHeaderView(0).findViewById(R.id.nickname_profile);

        email_v = drawerView.getHeaderView(0).findViewById(R.id.user_email);

        //set event listener for the navi item
        drawerView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.app_logout){//logout
                Intent intent1 = new Intent(MainActivity.this,LogInActivity.class);
                intent1.putExtra("remName",username);
                intent1.putExtra("RemPass", user.getPassword());
                startActivity(intent1
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            }else if (item.getItemId()==R.id.accountEditActivity){//my account
                //transmit
                Intent intent3 = new Intent(MainActivity.this, AccountEditActivity.class);
                if(username == null) {
                    intent3.putExtra("username", customer.getUsername());
                    intent3.putExtra("email", customer.getEmail());
                }else {
                    intent3.putExtra("username", username);
                    intent3.putExtra("email",email_v.getText().toString());
                }
                startActivity(intent3);
            }else if (item.getItemId()==R.id.appSettingsActivity) {
                //transmit
                Intent intent2 = new Intent(MainActivity.this, AppSettingsActivity.class);
                if(username == null)
                    intent2.putExtra("username", customer.getUsername());
                else
                    intent2.putExtra("username", username);
                startActivity(intent2);
            }else if (item.getItemId() == R.id.menu_comment){
                //give the username to the commentsActivity
                Intent intent1 = new Intent(MainActivity.this, CommentsActivity.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
            }
            return true;
        });

        SharedPreferences spfRecord = getSharedPreferences("remName", MODE_PRIVATE);
        SharedPreferences.Editor edit = spfRecord.edit();
        try{
            edit.putString("remName", username);
            if(passW==null) {
                edit.putString("RemPass", user.getPassword());
                Log.e("-----------wu",user.getPassword());
            }else {
                edit.putString("RemPass", passW);
                Log.e("------------you", passW);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        edit.apply();

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

    // set on click event
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
                intent1.putExtra("username", username);
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

    //set language
    private void setLocale(String lang) {
        //Initialize resources
        Resources resources = getResources();
        //Initialize metrics
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //initialize configurations
        Configuration configuration = resources.getConfiguration();
        //initialize locale
        configuration.locale = new Locale(lang);
        //update configuration
        resources.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);

    }

    /**
     * methods to initialize data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData(){
        SharedPreferences spfLang = getSharedPreferences("spfLang", MODE_PRIVATE);
        lang = spfLang.getString("Lang","");
        if (lang.equals("en")){//setting language
            setLocale(lang);
        }else if (lang.equals("zh")){
            setLocale(lang);
        }else {
            setLocale("en");
        }
        showUserInfo();
        getDataFromSpf();
    }

    //get data
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromSpf(){
        SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
        String image64 = spfRecord.getString("image_64","");
        SharedPreferences.Editor edit = spfRecord.edit();
        if (image64.length()!=0) {
            imageView.setImageBitmap(ImageUtil.base64ToImage(image64));
            drawerImage.setImageBitmap(ImageUtil.base64ToImage(image64));
        }
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            int temp = avatarDao.getAvatarVersionByUsername(username);
            if (temp!=0&&temp!=version) {
                version = avatarDao.getAvatarVersionByUsername(username);
                edit.putInt("imageVersion",version);
                edit.apply();
                getSmallFromDB();
            }
            else if (temp==0) {
                Message message = handler.obtainMessage();
                message.what = 0x44;
                handler.sendMessage(message);
            }
        }).start();

    }

    private void getSmallFromDB() {
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            byte[] bytes = avatarDao.getLittleByUsername(username);
            imageBase64 = ImageUtil.ByteArray2Base64(bytes);
            Message message = handler.obtainMessage();
            message.what = 0x33;
            handler.sendMessage(message);
        }).start();
    }

    private void getAvatarFromDB() {
        new Thread(()->{
            AvatarDao avatarDao = new AvatarDao();
            byte[] bytes = avatarDao.getAvatarByUsername(username);
            if(bytes!=null){//if the user has an avatar in DB
                imageBase64 = ImageUtil.ByteArray2Base64(bytes);
                SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
                SharedPreferences.Editor edit = spfRecord.edit();
                edit.putString("image_64", imageBase64);
                edit.apply();
            }
        }).start();
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
        SharedPreferences spfRecord = getSharedPreferences("spfRecord"+username, MODE_PRIVATE);
        version = spfRecord.getInt("imageVersion",0);
        new Thread(() -> {
            UserDao userDao = new UserDao();
            user = userDao.getUserByUsername(username);
            customer = userDao.getUserByUsername("root");


            Message message = handler.obtainMessage();
            if(user != null){
                message.what = 0x11;
                nickname = user.getNickname();
            }else {
                message.what = 0x22;
                nickname = customer.getNickname();
            }
            handler.sendMessage(message);
        }).start();
    }
}