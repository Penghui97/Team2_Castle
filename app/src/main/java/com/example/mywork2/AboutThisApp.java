package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

/**
 * @author Penghui Xiao, JiaCheng Cheng
 * function: display aboutus page
 * modification date and description can be found in github repository history
 */
public class AboutThisApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);
        ImageView header_back = findViewById(R.id.back_button);

        //set header event
        header_back.setOnClickListener(view ->{
            this.finish();
        });
    }
}