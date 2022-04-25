package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //abc
        FragmentContainerView fragmentContainerView = findViewById(R.id.log_in_nav_host_frag);
        NavHostFragment navHostFragment = fragmentContainerView.getFragment();
        //get Navigation Controller from the Host fragment
        NavController navController = navHostFragment.getNavController();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

}