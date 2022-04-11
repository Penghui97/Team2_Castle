package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CastleDetails extends AppCompatActivity {
    private static String castleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_castle_details);
        TextView castleTitle = findViewById(R.id.castleName);
        castleTitle.setText(castleName);
    }

    public static void startCastleActivity(Context context,String inputCastleName){
        Intent intent = new Intent(context,CastleDetails.class);
        castleName = inputCastleName;
        context.startActivity(intent);
    }
}