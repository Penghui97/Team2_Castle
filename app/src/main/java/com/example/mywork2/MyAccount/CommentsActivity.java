package com.example.mywork2.MyAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mywork2.R;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.comment);
        title.setTextColor(Color.BLACK);
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});


    }
}