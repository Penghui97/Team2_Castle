package com.example.mywork2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mywork2.dao.DepartureTimeDao;
import com.example.mywork2.dao.JourneyDao;
import com.example.mywork2.dao.TicketDao;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;
import com.example.mywork2.domain.Route;
import com.example.mywork2.domain.Ticket;
import com.example.mywork2.domain.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author  Jing()
 * function: this class is for showing the detailed information of one particular plan
 * when the user choose one brief plan in the searching function
 */
public class SearchPlanInfoActivity extends AppCompatActivity implements View.OnClickListener {

    //store the current journey
    private Journey currentJourney;
    private String date;
    private String time;
    private String returnTime;
    private int inputAdultNum;
//    private int inputKidsNum;
    private ArrayList<DepartureTime> routeDepartureTimes;
    private ArrayList<DepartureTime> returnRouteDepartureTimes;

    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:
                    //receive the particular journey
                    Journey journey = (Journey) msg.obj;
                    currentJourney = journey;
                    getDepartureTimes();
                    break;
                case 0x22:
                    ArrayList both = (ArrayList) msg.obj;
                    routeDepartureTimes = (ArrayList<DepartureTime>) both.get(0);
                    returnRouteDepartureTimes = (ArrayList<DepartureTime>) both.get(1);
                    showJourneyInfo(currentJourney);
                    break;
                case 0x33:
                    //there is no appropriate bus
                    showJourneyInfo(currentJourney);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_plan_info);

        Intent intent = getIntent();
        String journeyId = (String) intent.getExtras().get("journeyId");
        date = (String) intent.getExtras().get("date");
        date = transferTimeFormat(date);
        time = (String) intent.getExtras().get("time");

        getJourneyById(journeyId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchPlanInfoSave:
//                getInput();
                saveJourney();
                alertSuccess();
                break;
        }
    }

    //get a particular journey by its id
    public void getJourneyById(String journeyId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JourneyDao journeyDao = new JourneyDao();
                Journey journey = journeyDao.getJourneyById(journeyId);
                Message message = handler.obtainMessage();
                message.what = 0x11;
                message.obj = journey;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void getDepartureTimes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<DepartureTime> tempRouteDepartureTimes = new ArrayList<>();
                ArrayList<DepartureTime> tempReturnRouteDepartureTimes = new ArrayList<>();
                DepartureTimeDao departureTimeDao = new DepartureTimeDao();
                Time currentTime = new Time(time);
                for (Route route : currentJourney.getRoutes()) {
                    //if this route is not by walk
                    //check the time of the vehicle
                    if (!route.getTransport().getType().equals("walk")) {
                        DepartureTime departureTime = departureTimeDao.getDepartureTimeByRouteId(route.getRouteId(), currentTime.toString());
                        if (departureTime == null) {
                            //there is no appropriate bus
                            Message message = handler.obtainMessage();
                            message.what = 0x33;
                            handler.sendMessage(message);
                            return;
                        }
                        tempRouteDepartureTimes.add(departureTime);
                        currentTime.setTime(departureTime.getDepTime());
                    }
                    currentTime.add(route.getDuration());
                }
                //give 2 hours to explore the castle
                currentTime.add(120);
                for (Route route : currentJourney.getReturnRoutes()) {
                    //if this route is not by walk
                    //check the time of the vehicle
                    if (!route.getTransport().getType().equals("walk")) {
                        DepartureTime departureTime = departureTimeDao.getDepartureTimeByRouteId(route.getRouteId(), currentTime.toString());
                        if (departureTime == null) {
                            //there is no appropriate bus
                            Message message = handler.obtainMessage();
                            message.what = 0x33;
                            handler.sendMessage(message);
                            return;
                        }
                        tempReturnRouteDepartureTimes.add(departureTime);
                        currentTime.setTime(departureTime.getDepTime());
                    }
                    currentTime.add(route.getDuration());
                }
                Message message = handler.obtainMessage();
                message.what = 0x22;
                ArrayList both = new ArrayList();
                both.add(tempRouteDepartureTimes);
                both.add(tempReturnRouteDepartureTimes);
                message.obj = both;
                handler.sendMessage(message);
            }
        }).start();
    }

    //show the journey information
    public void showJourneyInfo(Journey journey) {

        TextView fromView = findViewById(R.id.searchPlanInfoFrom);
        TextView toView = findViewById(R.id.searchPlanInfoTo);
        fromView.append(journey.getDeparture());
        toView.append(journey.getCastle().getName());

        if(routeDepartureTimes == null || returnRouteDepartureTimes == null){
            //if there is no bus or train at this time at this stop
            //make the route views disappear
            //show a signal to the user
            //and break this loop
            timeNotAppropriate();
            return;
        }
        //create a time object to log the time
        Time currentTime = new Time(time);

        LinearLayout routesLayout = findViewById(R.id.searchPlanInfoRoutes);
        for (int i = 0; i < journey.getRoutes().size(); i++) {
            Route route = journey.getRoutes().get(i);

            if (!route.getTransport().getType().equals("walk")) {
                //if the route is by vehicle
                //get the next bus time
                //set it as current time
                DepartureTime departureTime = routeDepartureTimes.remove(0);
                currentTime.setTime(departureTime.getDepTime());
            }
            TextView textView = new TextView(SearchPlanInfoActivity.this);
            //set the bus stops' name
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            textView.setTextSize(15);
            routesLayout.addView(textView);
        }

        //give 2 hours to explore the castle
        currentTime.add(120);
        returnTime = currentTime.toString();
        //show the return routes
        LinearLayout returnRoutesLayout = findViewById(R.id.searchPlanInfoReturnRoutes);

        for (int i = 0; i < journey.getReturnRoutes().size(); i++) {
            Route route = journey.getReturnRoutes().get(i);

            //the first walk time will base on the first bus time
            if(i == 0 && route.getTransport().getType().equals("walk")){
                Time firstBus = new Time(returnRouteDepartureTimes.get(0).getDepTime());
                currentTime.setTime(firstBus.reduce(route.getDuration()));
                returnTime = currentTime.toString();
            }

            if (!route.getTransport().getType().equals("walk")) {
                //if the route is by vehicle
                //get the next bus time
                //set it as current time
                DepartureTime departureTime = returnRouteDepartureTimes.remove(0);
                currentTime.setTime(departureTime.getDepTime());
            }

            TextView textView = new TextView(SearchPlanInfoActivity.this);

            //set the bus stops' name
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            textView.setTextSize(15);
            returnRoutesLayout.addView(textView);
        }

    }

    public void timeNotAppropriate(){
        //if there is no bus or train at this time at this stop
        //make the route views disappear
        //show a signal to the user
        TextView textView = new TextView(SearchPlanInfoActivity.this);
        textView.setText("your depart time is too late\n ");
        Button button = new Button(SearchPlanInfoActivity.this);
        button.setText("return");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout searchPlanInfoLateSignal = findViewById(R.id.searchPlanInfoLateSignal);
        searchPlanInfoLateSignal.addView(textView);
        searchPlanInfoLateSignal.addView(button);
        findViewById(R.id.searchPlanInfoDisappear).setVisibility(View.GONE);
    }

    //save the journey as a ticket into the database
    public void saveJourney() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Ticket ticket = currentJourney.toTicket("root", date, time, returnTime, inputAdultNum);
                TicketDao ticketDao = new TicketDao();
                ticketDao.addTicket(ticket);
            }
        }).start();
    }

    //after drop the kid ticket functions
    //it has been canceled
    //get all the information the user input
//    public void getInput() {
//        Spinner adultNum = findViewById(R.id.adultNum);
//        Spinner kidsNum = findViewById(R.id.kidNum);
//        inputAdultNum = Integer.parseInt(adultNum.getSelectedItem().toString());
//        inputKidsNum = Integer.parseInt(kidsNum.getSelectedItem().toString());
//    }

    //alert a friendly message to the user
    //when he save the plan successfully
    public void alertSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("succeed to save")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(SearchPlanInfoActivity.this, MainActivity.class));
                    }
                })
                .create()
                .show();
    }

    //transfer dd/MM/yyyy to yyyy/MM/dd
    public String transferTimeFormat(String strDate) {
        String res = null;
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format1.parse(strDate);
            res = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}