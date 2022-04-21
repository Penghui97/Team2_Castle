package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class AboutThisApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);

        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.About_this_app);
        title.setTextColor(Color.BLACK);
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});
    }
}