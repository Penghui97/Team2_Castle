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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mywork2.Util.PayUtil;
import com.example.mywork2.dao.DepartureTimeDao;
import com.example.mywork2.dao.JourneyDao;
import com.example.mywork2.dao.TicketDao;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;
import com.example.mywork2.domain.Route;
import com.example.mywork2.domain.Ticket;
import com.example.mywork2.domain.Time;

import java.util.ArrayList;

/**
 * @author  Jiacheng(UI behavior), Jing(Back-end code)
 * function: this activity is for dealing with the layout of one particular plan's information
 * modification date and description can be found in github repository history
 */
public class MyPlansInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Ticket currentTicket;
    private Journey currentJourney;
    private ArrayList<DepartureTime> routeDepartureTimes;
    private ArrayList<DepartureTime> returnRouteDepartureTimes;

    //the handler to get the data
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0x11:
                    //receive the got ticket
                    currentTicket = (Ticket) message.obj;
                    getJourneyById(currentTicket.getJourneyId());
                    break;
                case 0x22:
                    //receive the got journey
                    currentJourney = (Journey) message.obj;
                    getDepartureTimes();
                    break;
                case 0x23:
                    ArrayList both = (ArrayList) message.obj;
                    routeDepartureTimes = (ArrayList<DepartureTime>) both.get(0);
                    returnRouteDepartureTimes = (ArrayList<DepartureTime>) both.get(1);
                    showInfo();
                    break;
                case 0x33:
                    //receive the paid result
                    String resultMessage = (String) message.obj;
                    alertMessage(resultMessage);
                    break;
                case 0x44:
                    //receive the refund result
                    String resultMessage2 = (String) message.obj;
                    alertMessage(resultMessage2);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plans_info);

        Intent intent = getIntent();
        String ticketId = (String) intent.getExtras().get("ticketId");
        getTicketById(ticketId);
    }

    //the onclick listener
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myPlanInfoReturn:
                finish();
                break;
            case R.id.myPlanInfoBuy:
                String transactionAmount = "-" + currentTicket.getTotalPrice() + "";
                horsePay(currentTicket.getUsername(), transactionAmount);
                break;
            case R.id.myPlanInfoRemove:
                if (currentTicket.isPaid()) {
                    horseRefund(currentTicket.getUsername(), "+" + currentTicket.getTotalPrice());
                } else {
                    removeTicketById(currentTicket.getTicketId());
                    alertMessage("remove success");
                }
                break;
            case R.id.myPlanInfoNearby:
                toNearby();
                break;

        }
    }

    //get the ticket from database and put it into currentTicket
    public void getTicketById(String ticketId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TicketDao ticketDao = new TicketDao();
                Ticket ticket = ticketDao.getTicketById(ticketId);
                Message message = handler.obtainMessage();
                message.what = 0x11;
                message.obj = ticket;
                handler.sendMessage(message);
            }
        }).start();
    }

    //get the journey information from the database
    public void getJourneyById(String journeyId) {
        new Thread(() -> {
            JourneyDao journeyDao = new JourneyDao();
            Journey journey = journeyDao.getJourneyById(journeyId);
            Message message = handler.obtainMessage();
            message.what = 0x22;
            message.obj = journey;
            handler.sendMessage(message);
        }).start();
    }

    //get the time table of the vehicles
    public void getDepartureTimes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<DepartureTime> tempRouteDepartureTimes = new ArrayList<>();
                ArrayList<DepartureTime> tempReturnRouteDepartureTimes = new ArrayList<>();
                DepartureTimeDao departureTimeDao = new DepartureTimeDao();
                Time currentTime = new Time(currentTicket.getTime());
                for (Route route : currentJourney.getRoutes()) {
                    //if this route is not by walk
                    //check the time of the vehicle
                    if (!route.getTransport().getType().equals("walk")) {
                        DepartureTime departureTime = departureTimeDao.getDepartureTimeByRouteId(route.getRouteId(), currentTime.toString());
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
                        tempReturnRouteDepartureTimes.add(departureTime);
                        currentTime.setTime(departureTime.getDepTime());

                    }
                    currentTime.add(route.getDuration());
                }
                Message message = handler.obtainMessage();
                message.what = 0x23;
                ArrayList both = new ArrayList();
                both.add(tempRouteDepartureTimes);
                both.add(tempReturnRouteDepartureTimes);
                message.obj = both;
                handler.sendMessage(message);
            }
        }).start();
    }

    //show the detailed information
    public void showInfo() {
        //show the detailed ticket information
        TextView castle = findViewById(R.id.myPlanInfoCastle);
        TextView date = findViewById(R.id.myPlanInfoDate);
        TextView departTime = findViewById(R.id.myPlanInfoDepartTime);
        TextView returnTime = findViewById(R.id.myPlanInfoReturnTime);
        TextView adultNum = findViewById(R.id.myPlanInfoTicketCount);
//        TextView kidNum = findViewById(R.id.myPlanInfoKidsNum);
        TextView totalPrice = findViewById(R.id.myPlanInfoTotalPrice);

        castle.append(currentTicket.getCastleName());
        date.append(currentTicket.getDate());
        departTime.append(currentTicket.getTime());
        returnTime.append(currentTicket.getReturnTime());
        adultNum.append(Integer.toString(currentTicket.getQuantity()));
//        kidNum.append(Integer.toString(currentTicket.getKidsQuantity()));
        totalPrice.append(Integer.toString(currentTicket.getTotalPrice()));
        //show the detailed routes
        ArrayList<Route> routes = currentJourney.getRoutes();
        ArrayList<Route> returnRoutes = currentJourney.getReturnRoutes();
        //create a time object to log the time
        Time currentTime = new Time(currentTicket.getTime());
        for (Route route : routes) {
            if (!route.getTransport().getType().equals("walk")) {
                //if the route is by vehicle
                //get the next bus time
                //set it as current time
                DepartureTime departureTime = routeDepartureTimes.remove(0);
                currentTime.setTime(departureTime.getDepTime());
            }
            TextView textView = new TextView(this);
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            LinearLayout routesLayout = findViewById(R.id.myPlanInfoRoutes);
            routesLayout.addView(textView);
        }
        //give 2 hours to explore the castle
        currentTime.add(120);
        for (int i = 0; i < currentJourney.getReturnRoutes().size(); i++) {
            Route route = currentJourney.getReturnRoutes().get(i);
            //the first walk time will base on the first bus time
            if(i == 0 && route.getTransport().getType().equals("walk")){
                Time firstBus = new Time(returnRouteDepartureTimes.get(0).getDepTime());
                currentTime.setTime(firstBus.reduce(route.getDuration()));
            }

            if (!route.getTransport().getType().equals("walk")) {
                //if the route is by vehicle
                //get the next bus time
                //set it as current time
                DepartureTime departureTime = returnRouteDepartureTimes.remove(0);
                currentTime.setTime(departureTime.getDepTime());
            }
            TextView textView = new TextView(this);
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            LinearLayout returnRoutesLayout = findViewById(R.id.myPlanInfoReturnRoutes);
            returnRoutesLayout.addView(textView);
        }
        //change the status of the by button
        if (currentTicket.isPaid()) {
            findViewById(R.id.myPlanInfoBuy).setVisibility(View.GONE);
            Button removeButton = findViewById(R.id.myPlanInfoRemove);
            removeButton.setText("refund");
        }
    }

    //delete a ticket by its id or refund it
    public void removeTicketById(String ticketId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TicketDao ticketDao = new TicketDao();
                ticketDao.removeAllTicketsById(ticketId);
            }
        }).start();
    }

    //alert a success message
    public void alertMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    //use the horsePay api to pay
    public void horsePay(String customerId, String transactionAmount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean paidSuccess = PayUtil.pay(customerId, transactionAmount);
                String resultMessage = "";
                if (paidSuccess) {
                    resultMessage = "total £ " + currentTicket.getTotalPrice() + "\npayment success";
                    //change isPaid status in the database
                    payTicket(currentTicket.getTicketId());
                } else {
                    resultMessage = "sorry, payment failed";
                }
                Message message = handler.obtainMessage();
                message.what = 0x33;
                message.obj = resultMessage;
                handler.sendMessage(message);
            }
        }).start();
    }

    //use the horsePay to refund
    public void horseRefund(String customerId, String transactionAmount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean paidSuccess = PayUtil.pay(customerId, transactionAmount);
                String resultMessage = "";
                if (paidSuccess) {
                    resultMessage = "total £ " + currentTicket.getTotalPrice() + "\nrefund success";
                    //remove the ticket in the database
                    removeTicketById(currentTicket.getTicketId());
                } else {
                    resultMessage = "sorry, refund failed";
                }
                Message message = handler.obtainMessage();
                message.what = 0x44;
                message.obj = resultMessage;
                handler.sendMessage(message);
            }
        }).start();
    }

    //change the paid status of this ticket in the database
    public void payTicket(String ticketId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TicketDao ticketDao = new TicketDao();
                ticketDao.payTicketById(ticketId);
            }
        }).start();
    }

    //jump to the nearby page
    public void toNearby(){
        Intent intent = new Intent(this, MyPlansInfoNearbyActivity.class);
        intent.putExtra("nearbyPOIs", currentJourney.getCastle().getNearbyPOIs());
        startActivity(intent);
    }
}