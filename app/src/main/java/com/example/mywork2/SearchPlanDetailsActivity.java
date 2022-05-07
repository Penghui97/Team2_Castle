package com.example.mywork2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mywork2.MyAccount.AccountEditActivity;
import com.example.mywork2.adapter.PlanDetailAdapter;
import com.example.mywork2.dao.DepartureTimeDao;
import com.example.mywork2.dao.JourneyDao;
import com.example.mywork2.dao.TicketDao;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;
import com.example.mywork2.domain.Route;
import com.example.mywork2.domain.Ticket;
import com.example.mywork2.domain.Time;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * this class is for showing the sorted plans based on the users' requirements
 */
public class SearchPlanDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout searchPlanDetailsLayout;
    private Button save;
    public LinearLayout searchPlanInfoAllContent;
    public LinearLayout searchPlanInfoLoading;
    public LinearLayout getSearchPlanInfoLoadingAfter;
    public BottomSheetDialog bottomSheetDialog;
    private View bottomView;
    private ArrayList<Journey> journeys;
    private String date;
    private String time;
    private String username;
    private int ticketNum;
//    private String departure;
    private String destination;

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
                    break;
                case 0x44:
                    //the ticket is saved successfully
                    alertSuccess();
                    break;
                case 0x98:
                    //check the time of the journeys
                    journeys = (ArrayList<Journey>) msg.obj;
                    checkJourneysTime();
                    break;
                case 0x99:
                    //receive the searched journeys
                    showJourneys(journeys);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_plan_details);

        //set the actionbar
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});
        //get the user from the

        //not show the info page until one particular plan is clicked
        searchPlanDetailsLayout = findViewById(R.id.searchPlanDetailsLayout);
        searchPlanInfoAllContent = findViewById(R.id.searchPlanInfoAllContent);
        searchPlanInfoLoading = findViewById(R.id.searchPlanLoading);
        getSearchPlanInfoLoadingAfter = findViewById(R.id.searchPlanLoadingAfter);
        searchPlanInfoAllContent.setVisibility(View.GONE);

        //get the input of the user
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //canceled this function
//        departure = (String) extras.get("departure");
        destination = (String) extras.get("destination");
        time = (String) extras.get("time");
        date = (String) extras.get("date");
        username = (String) extras.get("username");
        ticketNum = (Integer) extras.get("ticketNum");

        //sort the journeys
        getJourneys(destination);

        /**@J Cheng
         * the destination and the departure place
         */
        TextView departurePlace = findViewById(R.id.journey_from);
        //canceled this function
//        departurePlace.setText(departure);
        TextView destinationPlace = findViewById(R.id.journey_to);
        destinationPlace.setText(destination);

        //get bottom sheet view
        bottomSheetDialog = new BottomSheetDialog(SearchPlanDetailsActivity.this,R.style.BottomSheetDialogTheme);
        bottomView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_search,
                findViewById(R.id.bottom_sheet_search)
        );
        save = bottomView.findViewById(R.id.searchPlanInfoSave);
        save.setOnClickListener(this);
        bottomSheetDialog.setContentView(bottomView);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /*case R.id.searchPlanInfoReturn:
                searchPlanInfoAllContent.setVisibility(View.GONE);
                break;*/
            case R.id.searchPlanInfoSave:
//                getInput();
                saveJourney();
                bottomSheetDialog.dismiss();
                break;
        }
    }


    //get the journeys by departure and destination
    public void getJourneys(String destination) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //sort the destination
                JourneyDao journeyDao = new JourneyDao();
                ArrayList<Journey> journeys = journeyDao.getJourneysByCastleName(destination);
                //canceled this function
                //sort the departure
//                for (int i = journeys.size() - 1; i >= 0; i--) {
//                    if (!journeys.get(i).getDeparture().equals(departure))
//                        journeys.remove(i);
//                }
                Message message = handler.obtainMessage();
                message.what = 0x98;
                message.obj = journeys;
                handler.sendMessage(message);
            }
        }).start();
    }

    //check the times of the journeys
    public void checkJourneysTime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                a:for (int i = journeys.size() - 1; i >= 0; i--) {
                    ArrayList<DepartureTime> tempRouteDepartureTimes = new ArrayList<>();
                    ArrayList<DepartureTime> tempReturnRouteDepartureTimes = new ArrayList<>();
                    DepartureTimeDao departureTimeDao = new DepartureTimeDao();
                    Time currentTime = new Time(time);
                    for (Route route : journeys.get(i).getRoutes()) {
                        //if this route is not by walk
                        //check the time of the vehicle
                        if (!route.getTransport().getType().equals("walk")) {
                            DepartureTime departureTime = departureTimeDao.getDepartureTimeByRouteId(route.getRouteId(), currentTime.toString());
                            if (departureTime == null) {
                                //there is no appropriate bus
                                //check next journey
                                journeys.remove(i);
                                continue a;
                            }
                            tempRouteDepartureTimes.add(departureTime);
                            currentTime.setTime(departureTime.getDepTime());
                        }
                        currentTime.add(route.getDuration());
                    }
                    //give 2 hours to explore the castle
                    currentTime.add(120);
                    for (Route route : journeys.get(i).getReturnRoutes()) {
                        //if this route is not by walk
                        //check the time of the vehicle
                        if (!route.getTransport().getType().equals("walk")) {
                            DepartureTime departureTime = departureTimeDao.getDepartureTimeByRouteId(route.getRouteId(), currentTime.toString());
                            if (departureTime == null) {
                                //there is no appropriate bus
                                //check next journey
                                journeys.remove(i);
                                continue a;
                            }
                            tempReturnRouteDepartureTimes.add(departureTime);
                            currentTime.setTime(departureTime.getDepTime());
                        }
                        currentTime.add(route.getDuration());
                    }
                }
                Message message = handler.obtainMessage();
                message.what = 0x99;
                handler.sendMessage(message);
            }
        }).start();
    }

    //show the required journeys
    public void showJourneys(ArrayList<Journey> journeys) {
        if (journeys == null || journeys.size() == 0) {
//            alertTime();
            TextView textView = new TextView(this);
            textView.setText("Sorry, no results fit your requirements.");
            searchPlanDetailsLayout.addView(textView);
        } else {
            //put the journey list into the listView
            ListView listView = findViewById(R.id.planListView);
            listView.setAdapter(new PlanDetailAdapter(journeys, this));
            /**
             * (Jing)
             * the click listener is set on the button in the adapter
             */
            //set the items listener
            //jump to the info activity with the particular journey id
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent intent = new Intent(SearchPlanDetailsActivity.this, SearchPlanInfoActivity.class);
//                    intent.putExtra("journeyId", journeys.get(i).getJourneyId());
//                    intent.putExtra("date", date);
//                    intent.putExtra("time", time);
//                    startActivity(intent);
//                }
//            });
        }

    }

    //(not used now)
    //used by the adapter
    public void toSearchPlanInfo(int index) {
        Intent intent = new Intent(SearchPlanDetailsActivity.this, SearchPlanInfoActivity.class);
        intent.putExtra("journeyId", journeys.get(index).getJourneyId());
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        startActivity(intent);
    }

    //original content in SearchPlanInfoActivity class
    //-------------------------------------------------------------------

    //store the current journey
    private Journey currentJourney;
    private String returnTime;
//    private int inputAdultNum;
//    private int inputKidsNum;
    private ArrayList<DepartureTime> routeDepartureTimes;
    private ArrayList<DepartureTime> returnRouteDepartureTimes;

    //get a particular journey by its id
    public void getJourneyById(String journeyId) {

//        //show the loading page until info has been loaded
//        searchPlanInfoLoading.setVisibility(View.VISIBLE);
//        getSearchPlanInfoLoadingAfter.setVisibility(View.GONE);
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

    //clear all the content first
    //then fill it with particular content
    public void initJourneyInfo(){
        TextView fromView = bottomView.findViewById(R.id.searchPlanInfoFrom);
        TextView toView = bottomView.findViewById(R.id.searchPlanInfoTo);
        LinearLayout routesLayout = bottomView.findViewById(R.id.searchPlanInfoRoutes);
        LinearLayout returnRoutesLayout = bottomView.findViewById(R.id.searchPlanInfoReturnRoutes);
        LinearLayout castleDetails = bottomView.findViewById(R.id.castle_plan);
        LinearLayout totalCost = bottomView.findViewById(R.id.totoalCost_plan);

        fromView.setText("");
        toView.setText("");
        routesLayout.removeAllViews();
        returnRoutesLayout.removeAllViews();
        castleDetails.removeAllViews();
        totalCost.removeAllViews();


    }

    //show the journey information
    public void showJourneyInfo(Journey journey) {
        initJourneyInfo();

        TextView fromView = bottomView.findViewById(R.id.searchPlanInfoFrom);
        TextView toView = bottomView.findViewById(R.id.searchPlanInfoTo);
        fromView.append(journey.getDeparture());
        toView.append(journey.getCastle().getName());

//        if(routeDepartureTimes == null || returnRouteDepartureTimes == null){
//            //if there is no bus or train at this time at this stop
//            //make the route views disappear
//            //show a signal to the user
//            //and break this loop
//            timeNotAppropriate();
//            return;
//        }
        if(routeDepartureTimes == null || returnRouteDepartureTimes == null){
            //if there is no bus or train at this time at this stop
            //make the route views disappear
            //show a signal to the user
            //and break this loop
            alertTime();
            return;
        }
        //create a time object to log the time
        Time currentTime = new Time(time);

        LinearLayout routesLayout = bottomView.findViewById(R.id.searchPlanInfoRoutes);
        TextView goCost = new TextView(bottomView.getContext());
        goCost.setText("transportation cost: "+""+" ￡ " + currentJourney.getSinglePrice() + "\n");
        routesLayout.addView(goCost);
        for (int i = 0; i < journey.getRoutes().size(); i++) {
            Route route = journey.getRoutes().get(i);

            if (!route.getTransport().getType().equals("walk")) {
                //if the route is by vehicle
                //get the next bus time
                //set it as current time
                DepartureTime departureTime = routeDepartureTimes.remove(0);
                currentTime.setTime(departureTime.getDepTime());
            }
            /*
            TextView textView = new TextView(bottomView.getContext());
            //set the bus stops' name
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            textView.setTextSize(15);
            routesLayout.addView(textView);*/
            ImageView transIcon = new ImageView(bottomView.getContext());
            switch (route.getTransport().getType()){
                case "walk":
                    transIcon.setImageResource(R.drawable.icons8_walking_24);
                    break;
                case "Train":
                    transIcon.setImageResource(R.drawable.icons8_train_24);
                    break;
                case "Bus":
                    transIcon.setImageResource(R.drawable.icons8_bus_24);
                    break;
                default:
                    break;
            }
            //trans route details
            TextView routeId = new TextView(bottomView.getContext());
            routeId.setPadding(15,10,0,0);
            routeId.setText(route.getTransport().getType() + " ("
                    + route.getTransport().getTransportId() + ")                          "
                    +route.getDuration() + " min\n ");
            //linear layout for trans
            LinearLayout transportLayout = new LinearLayout(bottomView.getContext());
            transportLayout.setOrientation(LinearLayout.HORIZONTAL);
            transportLayout.addView(transIcon);
            transportLayout.addView(routeId);

            //set start point of trans
            TextView startPoint = new TextView(bottomView.getContext());
            startPoint.setText(route.getStart() + " (" + currentTime + ")" + "");
            startPoint.setTextSize(15);

            //arrow image
            ImageView downArrow = new ImageView(bottomView.getContext());
            downArrow.setPadding(0,20,0,20);
            downArrow.setImageResource(R.drawable.icons8_down_arrow_48);

            //arrow layout
            LinearLayout arrowLayout = new LinearLayout(bottomView.getContext());
            arrowLayout.addView(downArrow);
            //set end point of trans
            TextView endPoint = new TextView(bottomView.getContext());
            endPoint.setText(route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            endPoint.setTextSize(15);
            //textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            //textView.append(route.getDuration() + " min\n ");

            // structure the layout
            routesLayout.addView(transportLayout);
            routesLayout.addView(startPoint);
            routesLayout.addView(arrowLayout);
            routesLayout.addView(endPoint);

        }

        //show castle info
        LinearLayout castleDetails = bottomView.findViewById(R.id.castle_plan);
        LinearLayout castleView = new LinearLayout(bottomView.getContext());
        castleView.setPadding(20,20,20,20);
        ImageView castleImage = new ImageView(bottomView.getContext());
        castleImage.setImageResource(R.drawable.icons8_castle_32);
        TextView castleTitle = new TextView(bottomView.getContext());
        castleTitle.setText("   "+destination+"        ￡"+currentJourney.getCastle().getPrice());
        castleView.addView(castleImage);
        castleView.addView(castleTitle);
        castleDetails.addView(castleView);


        //give 2 hours to explore the castle
        currentTime.add(120);
        returnTime = currentTime.toString();
        //show the return routes
        LinearLayout returnRoutesLayout = bottomView.findViewById(R.id.searchPlanInfoReturnRoutes);
        TextView returnCost = new TextView(bottomView.getContext());
        returnCost.setText("transportation cost: " + " ￡ " + currentJourney.getSinglePrice() + "\n");
        returnRoutesLayout.addView(returnCost);

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

            /*TextView textView = new TextView(bottomView.getContext());

            //set the bus stops' name
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            textView.setTextSize(15);
            returnRoutesLayout.addView(textView);*/

            // trans type
            ImageView transIcon = new ImageView(bottomView.getContext());
            switch (route.getTransport().getType()){
                case "walk":
                    transIcon.setImageResource(R.drawable.icons8_walking_24);
                    break;
                case "Train":
                    transIcon.setImageResource(R.drawable.icons8_train_24);
                    break;
                case "Bus":
                    transIcon.setImageResource(R.drawable.icons8_bus_24);
                    break;
                default:
                    break;
            }
            //trans route details
            TextView routeId = new TextView(bottomView.getContext());
            routeId.setPadding(15,10,0,0);
            routeId.setText(route.getTransport().getType() + " ("
                    + route.getTransport().getTransportId() + ")                          "
                    +route.getDuration() + " min\n ");
            //linear layout for trans
            LinearLayout transportLayout = new LinearLayout(bottomView.getContext());
            transportLayout.setOrientation(LinearLayout.HORIZONTAL);
            transportLayout.addView(transIcon);
            transportLayout.addView(routeId);

            //set start point of trans
            TextView startPoint = new TextView(bottomView.getContext());
            startPoint.setText(route.getStart() + " (" + currentTime + ")" + "");
            startPoint.setTextSize(15);

            //arrow image
            ImageView downArrow = new ImageView(bottomView.getContext());
            downArrow.setPadding(0,20,0,20);
            downArrow.setImageResource(R.drawable.icons8_down_arrow_48);

            //arrow layout
            LinearLayout arrowLayout = new LinearLayout(bottomView.getContext());
            arrowLayout.addView(downArrow);
            //set end point of trans
            TextView endPoint = new TextView(bottomView.getContext());
            endPoint.setText(route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            endPoint.setTextSize(15);
            //textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            //textView.append(route.getDuration() + " min\n ");

            // structure the layout
            returnRoutesLayout.addView(transportLayout);
            returnRoutesLayout.addView(startPoint);
            returnRoutesLayout.addView(arrowLayout);
            returnRoutesLayout.addView(endPoint);
        }

        LinearLayout totalCost = bottomView.findViewById(R.id.totoalCost_plan);
        TextView totalCostText = new TextView(bottomView.getContext());
        totalCostText.setText("Total price: " + " ￡ " + currentJourney.getTotalPrice() * ticketNum + " = " + currentJourney.getTotalPrice() + " * " + ticketNum);
        totalCost.addView(totalCostText);

        //the price details
        //the go price
//        currentJourney.getSinglePrice();
        //the return price
//        currentJourney.getJourneyPrice() - currentJourney.getSinglePrice();
        //the castle price
//        currentJourney.getCastle().getPrice();
//        ticketNum;
//        currentJourney.getTotalPrice();

        //the info can't be showed until loading is done
        bottomView.findViewById(R.id.searchPlanLoading).setVisibility(View.GONE);
        bottomView.findViewById(R.id.searchPlanLoadingAfter).setVisibility(View.VISIBLE);
    }

    public void timeNotAppropriate(){
        //if there is no bus or train at this time at this stop
        //make the route views disappear
        //show a signal to the user
        TextView textView = new TextView(bottomView.getContext());
        textView.setText("your depart time is too late\n ");
        Button button = new Button(bottomView.getContext());
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

        bottomSheetDialog.dismiss();

    }

    //save the journey as a ticket into the database
    public void saveJourney() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(currentJourney != null){
//                    date = transferTimeFormat(date);
                    Ticket ticket = currentJourney.toTicket(username, date, time, returnTime, ticketNum);
                    TicketDao ticketDao = new TicketDao();
                    ticketDao.addTicket(ticket);
                    currentJourney = null;
                    Message message = handler.obtainMessage();
                    message.what = 0x44;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    //after drop the kid ticket functions
    //it has been canceled
    //get all the information the user input
//    public void getInput() {
//        Spinner adultNum = findViewById(R.id.adultNum);
//        Spinner kidsNum = findViewById(R.id.kidNum);
//    }

    //alert a friendly message to the user
    //when he save the plan successfully
    public void alertSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("succeed to save")
                .setPositiveButton("stay", (dialogInterface, i) -> {
//                        startActivity(new Intent(SearchPlanDetailsActivity.this, MainActivity.class));
                    //searchPlanInfoAllContent.setVisibility(View.GONE);
                })
                .setNegativeButton("myPlan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        MainActivity.toMyPlans();
                    }
                })
                .create()
                .show();
    }
    //when time is not suitable
    public void alertTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("your depart time is too late !!! please select another time")
                .setPositiveButton(R.string.return_page, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create().show();
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