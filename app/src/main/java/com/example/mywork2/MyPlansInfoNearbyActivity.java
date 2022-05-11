package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mywork2.adapter.NearbyPOIsAdapter;
import com.example.mywork2.domain.NearbyPOI;

import java.util.ArrayList;

/**
 * @author Jing
 * function: used for handling the behaviors of the nearby information
 */
public class MyPlansInfoNearbyActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<NearbyPOI> nearbyPOIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plans_info_nearby);
        nearbyPOIS = (ArrayList<NearbyPOI>) getIntent().getExtras().get("nearbyPOIs");
        ListView poiList = findViewById(R.id.poiList);
        poiList.setAdapter(new NearbyPOIsAdapter(nearbyPOIS, this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.poiReturn:
                finish();
                break;
        }
    }
}