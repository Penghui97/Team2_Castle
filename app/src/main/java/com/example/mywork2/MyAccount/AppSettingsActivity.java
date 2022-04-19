package com.example.mywork2.MyAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mywork2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AppSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText("Settings");
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});
        


    }

    @Override
    public void onClick(View view) {

    }
}