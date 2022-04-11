package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SearchPlans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_plans);
    }

    public static void startSearchPlansActivity(Context context){
        Intent intent = new Intent(context,SearchPlans.class);
        context.startActivity(intent);
    }
}