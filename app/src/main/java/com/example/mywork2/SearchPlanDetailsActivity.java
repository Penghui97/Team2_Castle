package com.example.mywork2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mywork2.adapter.PlanDetailAdapter;
import com.example.mywork2.dao.JourneyDao;
import com.example.mywork2.domain.Journey;

import java.util.ArrayList;

/**
 *this class is for showing the sorted plans based on the users' requirements
 */
public class SearchPlanDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout searchPlanDetailsLayout;
    private String date;
    private String time;

    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:
                    //receive the searched journeys
                    ArrayList<Journey> journeys = (ArrayList<Journey>) msg.obj;
                    showJourneys(journeys);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_plan_details);

        searchPlanDetailsLayout = findViewById(R.id.searchPlanDetailsLayout);

        //get the input of the user
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String departure = (String) extras.get("departure");
        String destination = (String) extras.get("destination");
        time = (String) extras.get("time");
        date = (String) extras.get("date");

        //sort the journeys
        getJourneys(departure, destination);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.planDetailsReturn:
                finish();
                break;
        }
    }


    //get the journeys by departure and destination
    public void getJourneys(String departure, String destination) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //sort the destination
                JourneyDao journeyDao = new JourneyDao();
                ArrayList<Journey> journeys = journeyDao.getJourneysByCastleName(destination);
                //sort the departure
                for (int i = journeys.size() - 1; i >= 0; i--) {
                    if (!journeys.get(i).getDeparture().equals(departure))
                        journeys.remove(i);
                }
                Message message = handler.obtainMessage();
                message.what = 0x11;
                message.obj = journeys;
                handler.sendMessage(message);
            }
        }).start();
    }

    //show the required journeys
    public void showJourneys(ArrayList<Journey> journeys) {
        if (journeys == null || journeys.size() == 0) {
            TextView textView = new TextView(this);
            textView.setText("Sorry, no results fit your requirements.");
            searchPlanDetailsLayout.addView(textView);
        } else {
            //put the journey list into the listView
            ListView listView = findViewById(R.id.planListView);
            listView.setAdapter(new PlanDetailAdapter(journeys, this));
            //set the items listener
            //jump to the info activity with the particular journey id
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(SearchPlanDetailsActivity.this, SearchPlanInfoActivity.class);
                    intent.putExtra("journeyId", journeys.get(i).getJourneyId());
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    startActivity(intent);
                }
            });
        }

    }
}