package com.example.mywork2.MyAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mywork2.R;

public class AppSettingsActivity extends AppCompatActivity {

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
}