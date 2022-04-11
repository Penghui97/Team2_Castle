package com.example.mywork2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        //ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation
        BottomNavigationView botNavView = findViewById(R.id.bottomNavigationView);
        FragmentContainerView fragmentContainerView = findViewById(R.id.mainpage_fragment_container_view_tag);
        NavHostFragment navHostFragment = fragmentContainerView.getFragment();
        //get Navigation Controller from the Host fragment
        NavController NavController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(botNavView,NavController);

        //drawer Navigation
        drawer = findViewById(R.id.drawer_layout);
        NavigationView drawerView = findViewById(R.id.drawerNavView);
        NavigationUI.setupWithNavController(drawerView,NavController);

        CardView account = findViewById(R.id.account_avatar);
        account.setOnClickListener(this);

        ImageView more = findViewById(R.id.app_helper);
        more.setOnClickListener(this);

        CardView account_avatar_head = drawerView.getHeaderView(0).findViewById(R.id.account_avatar_head);
        account_avatar_head.setOnClickListener(this);



    }

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