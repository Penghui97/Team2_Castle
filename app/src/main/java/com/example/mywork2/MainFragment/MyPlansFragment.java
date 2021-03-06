package com.example.mywork2.MainFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mywork2.Dialog.MyPlansDialoge;
import com.example.mywork2.MainActivity;
import com.example.mywork2.MyPlansInfoActivity;
import com.example.mywork2.MyPlansInfoNearbyActivity;
import com.example.mywork2.R;
import com.example.mywork2.Util.PayUtil;
import com.example.mywork2.Util.UserThreadLocal;
import com.example.mywork2.adapter.MyPlansAdapter;
import com.example.mywork2.dao.DepartureTimeDao;
import com.example.mywork2.dao.JourneyDao;
import com.example.mywork2.dao.TicketDao;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;
import com.example.mywork2.domain.Route;
import com.example.mywork2.domain.Ticket;
import com.example.mywork2.domain.Time;
import com.example.mywork2.domain.User;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;
/**
 * @author Jiacheng(UI behavior), Jing(Back-end)
 * function: the fragment for the user saved plans
 * modification date and description can be found in github repository history
 */
public class MyPlansFragment extends Fragment{
    // the background view of this fragment
    private View view;
    private User user;
    private LinearLayout myPlansNoPlansLayout;
    private TabLayout myPlanInfoRemoveNum;
    private Ticket currentTicket;
    private Journey currentJourney;
    private ArrayList<DepartureTime> routeDepartureTimes;
    private ArrayList<DepartureTime> returnRouteDepartureTimes;
    // the layout for departure plan
    public LinearLayout myPlanInfoLayout;
    // the layout for return plan
    public LinearLayout myPlanInfoRemoveNumLayout;
    // the warning dialog when remove ticket
    private MyPlansDialoge myPlansDialoge;
    public LinearLayout myPlanInfoLoading;
    public LinearLayout myPlanInfoLoadingAfter;

    public MyPlansFragment() {
        // Required empty public constructor
    }

    //the handler to receive the data from the dao thread
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
                    //receive the departTimes
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
                case 0x99:
                    //receive all the tickets fit the requirements
                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) message.obj;
                    initTickets();
                    showTickets(tickets);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_plans, container, false);
        //get the Linearlayout which holds the plans
        myPlansNoPlansLayout = this.view.findViewById(R.id.myPlansNoPlansLayout);
        myPlanInfoLayout = this.view.findViewById(R.id.myPlanInfoAllContentLayout);
        myPlanInfoRemoveNumLayout = this.view.findViewById(R.id.myPlanInfoRemoveNumLayout);
        myPlanInfoRemoveNum = this.view.findViewById(R.id.myPlanInfoRemoveNum);
        myPlanInfoLoading = this.view.findViewById(R.id.myPlanInfoLoading);
        myPlanInfoLoadingAfter = this.view.findViewById(R.id.myPlanInfoLoadingAfter);
        myPlanInfoLayout.setVisibility(View.GONE);
        myPlanInfoRemoveNumLayout.setVisibility(View.GONE);

        //get the user from the ThreadLocal
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            user = mainActivity.user;
        }
        //start to get the users information from the database
        getTickets();

        myPlansDialoge = new MyPlansDialoge();
        myPlansDialoge.setMyFragment(this);
        //set the click listeners in the info page
        //the return button
        this.view.findViewById(R.id.myPlanInfoReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                myPlanInfoLayout.setVisibility(View.GONE);
            }
        });
        //the by button
        this.view.findViewById(R.id.myPlanInfoBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horsePay(currentTicket.getUsername());
            }
        });
        //the remove button
        this.view.findViewById(R.id.myPlanInfoRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (currentTicket.isPaid()) {
//                    horseRefund(currentTicket.getUsername(), "+" + currentTicket.getTotalPrice());
//                } else {
//                    removeAllTicketsById(currentTicket.getTicketId());
//                    alertMessage("remove success");
//                }
                //if there is only 1 ticket
                //remove all ticket"s" directly
                if(currentTicket.getQuantity() == 1){
                    if (currentTicket.isPaid()) {
                        horseRefundAllTickets(currentTicket.getUsername());
                    }else{
                        removeAllTicketsById(currentTicket.getTicketId());
                        alertMessage("remove success");
                    }
                }else{
                    //give the number user can choose
                    showRemoveNumLayout();
                    myPlansDialoge.setTicketNums(myPlanInfoRemoveNum.getTabCount());
                    myPlansDialoge.show(getParentFragmentManager(),"dialog");
                }
            }
        });
        //the nearby button
        this.view.findViewById(R.id.myPlanInfoNearby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNearby();
            }
        });
        //the remove button in the removeNum layout
        this.view.findViewById(R.id.myPlanInfoRemoveNumButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int removeNum = myPlanInfoRemoveNum.getSelectedTabPosition() + 1;
                int boughtNum = currentTicket.getQuantity();
                //something wrong with the code
                if(removeNum > boughtNum) return;
                //the user remove all tickets
                if(removeNum == boughtNum){
                    if (currentTicket.isPaid()) {
                        horseRefundAllTickets(currentTicket.getUsername());
                    } else {
                        removeAllTicketsById(currentTicket.getTicketId());
                        alertMessage("remove success");
                    }
                }
                //the user remove not all tickets
                if(removeNum < boughtNum){
                    if (currentTicket.isPaid()) {
                        horseRefundTickets(currentTicket.getUsername(), removeNum);
                    } else {
                        removeTicketsById(currentTicket.getTicketId(), removeNum);
                        alertMessage("remove success");
                    }
                }
                myPlanInfoRemoveNumLayout.setVisibility(View.GONE);
            }
        });



        return view;
    }

    public void returnTicketsInDialog(int removeNum){
        int boughtNum = currentTicket.getQuantity();
        if(removeNum > boughtNum) return;
        //the user remove all tickets
        if(removeNum == boughtNum){
            if (currentTicket.isPaid()) {
                horseRefundAllTickets(currentTicket.getUsername());
            } else {
                removeAllTicketsById(currentTicket.getTicketId());
                myPlansDialoge.dismiss();
                alertMessage("remove success");
            }
        }
        //the user remove not all tickets
        if(removeNum < boughtNum){
            if (currentTicket.isPaid()) {
                horseRefundTickets(currentTicket.getUsername(), removeNum);
            } else {
                removeTicketsById(currentTicket.getTicketId(), removeNum);
                myPlansDialoge.dismiss();
                alertMessage("remove success");
            }
        }

    }

    //flush my plan list
    @Override
    public void onResume() {
        super.onResume();
        getTickets();
    }

    //get the user's tickets from the database
    public void getTickets(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TicketDao ticketDao = new TicketDao();
                ArrayList<Ticket> tickets;
                if(user == null) tickets = ticketDao.getTicketsByUsername("root");//if customer login
                else tickets = ticketDao.getTicketsByUsername(user.getUsername());//user login
                Message message = handler.obtainMessage();
                message.what = 0x99;
                message.obj = tickets;
                handler.sendMessage(message);
            }
        }).start();
    }

    //show the tickets
    public void showTickets(ArrayList<Ticket> tickets){
        //if there is no plans
        if(tickets == null || tickets.size() == 0){
            this.view.findViewById(R.id.myPlansNoPlansLayout).setVisibility(View.VISIBLE);
        }else{
            //set the my plan list
            ListView listView = this.view.findViewById(R.id.myPlansListView);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new MyPlansAdapter(tickets, this.view.findViewById(R.id.myPlansLayout1).getContext(), this));
            /**
             * (Jing)
             * the click listener is set on the button in adapter now
             */
            //set the item onclick listener
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    getTicketById(tickets.get(i).getTicketId());
//                }
//            });
        }
    }

    //remove the tickets
    public void initTickets(){
        myPlansNoPlansLayout.setVisibility(View.GONE);
        ListView listView = this.view.findViewById(R.id.myPlansListView);
        listView.setVisibility(View.GONE);
    }

    //not used now
    //jump to the detailed information of this plan
    public void toMyPlanInfo(String ticketId){
        Intent intent = new Intent(getActivity(), MyPlansInfoActivity.class);
        intent.putExtra("ticketId", ticketId);
        startActivity(intent);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                JourneyDao journeyDao = new JourneyDao();
                Journey journey = journeyDao.getJourneyById(journeyId);
                Message message = handler.obtainMessage();
                message.what = 0x22;
                message.obj = journey;
                handler.sendMessage(message);
            }
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
    @SuppressLint("SetTextI18n")
    public void showInfo() {
        //init the status of the info page
        initInfo();
        //show the detailed ticket information
        TextView castle = this.view.findViewById(R.id.myPlanInfoCastle);
        TextView date = this.view.findViewById(R.id.myPlanInfoDate);
        TextView departTime = this.view.findViewById(R.id.myPlanInfoDepartTime);
        TextView returnTime = this.view.findViewById(R.id.myPlanInfoReturnTime);
        TextView adultNum = this.view.findViewById(R.id.myPlanInfoTicketCount);
//        TextView kidNum = this.view.findViewById(R.id.myPlanInfoKidsNum);
        TextView totalPrice = this.view.findViewById(R.id.myPlanInfoTotalPrice);

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
            TextView textView = new TextView(getContext());
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            LinearLayout routesLayout = this.view.findViewById(R.id.myPlanInfoRoutes);
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
            TextView textView = new TextView(getContext());
            textView.setText("from: " + route.getStart() + " (" + currentTime + ")" + "\n");
            textView.append("to: " + route.getStop() + " (" + currentTime.add(route.getDuration()) + ")" + "\n");
            textView.append(route.getTransport().getType() + " (" + route.getTransport().getTransportId() + ")   ");
            textView.append(route.getDuration() + " min\n ");
            LinearLayout returnRoutesLayout = this.view.findViewById(R.id.myPlanInfoReturnRoutes);
            returnRoutesLayout.addView(textView);
        }

        //the layout not visible until the loading is done
        this.view.findViewById(R.id.myPlanInfoLoading).setVisibility(View.GONE);
        this.view.findViewById(R.id.myPlanInfoLoadingAfter).setVisibility(View.VISIBLE);

    }

    //delete all tickets by its id or refund it
    public void removeAllTicketsById(String ticketId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TicketDao ticketDao = new TicketDao();
                ticketDao.removeAllTicketsById(ticketId);
                currentTicket = null;
                getTickets();
            }
        }).start();
    }

    //delete particular number ticket
    public void removeTicketsById(String ticketId, int num){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TicketDao ticketDao = new TicketDao();
                ticketDao.removeTicketsById(ticketId, num);
                updateCurrentTicket(num);
                getTickets();
            }
        }).start();
    }

    //update the currentTicket after the remove
    public void updateCurrentTicket(int removeNum){
        int boughtNum = currentTicket.getQuantity();
        int leftNum = boughtNum - removeNum;
        int totalPrice = currentTicket.getTotalPrice();
        int singlePrice = totalPrice / boughtNum;
        currentTicket.setQuantity(leftNum);
        currentTicket.setTotalPrice(leftNum * singlePrice);
    }

    //user can choose the num to remove or refund
    public void showRemoveNumLayout(){
        //myPlanInfoRemoveNumLayout.setVisibility(View.VISIBLE);
        int labelNum = myPlanInfoRemoveNum.getTabCount();
        //show all the labels
        for(int i = labelNum; i < 5; i++){
            TabLayout.Tab tab = myPlanInfoRemoveNum.newTab().setText((i + 1) + "");
            myPlanInfoRemoveNum.addTab(tab, i, false);
        }
        //remove unnecessary labels
        int num = currentTicket.getQuantity();
        for(int i = 4; i >= num; i--){
            myPlanInfoRemoveNum.removeTabAt(i);
        }
    }

    //alert a success message
    public void alertMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
                        myPlanInfoLayout.setVisibility(View.GONE);
                    }

                })
                .create()
                .show();
    }

    //use the horsePay api to pay
    public void horsePay(String customerId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean paidSuccess = PayUtil.pay(customerId, "-" + currentTicket.getTotalPrice());
                String resultMessage = "";
                if (paidSuccess) {
                    resultMessage = "Total ?? " + currentTicket.getTotalPrice() + "\nPayment success";
                    //change isPaid status in the database
                    payTicket(currentTicket.getTicketId());
                    getTickets();
                } else {
                    resultMessage = "Sorry, payment failed";
                }
                Message message = handler.obtainMessage();
                message.what = 0x33;
                message.obj = resultMessage;
                handler.sendMessage(message);
            }
        }).start();
    }

    //use the horsePay to refund
    public void horseRefundAllTickets(String customerId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean paidSuccess = PayUtil.pay(customerId, "+" + currentTicket.getTotalPrice());
                String resultMessage = "";
                if (paidSuccess) {
                    resultMessage = "Total ?? " + currentTicket.getTotalPrice() + "\nRefund success";
                    //remove the ticket in the database
                    removeAllTicketsById(currentTicket.getTicketId());
                } else {
                    resultMessage = "Sorry, refund failed";
                }
                Message message = handler.obtainMessage();
                message.what = 0x44;
                message.obj = resultMessage;
                handler.sendMessage(message);
            }
        }).start();
    }

    //use the horsePay to refund particular ticket num
    public void horseRefundTickets(String customerId, int ticketNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean paidSuccess = PayUtil.pay(customerId, "+" + (currentTicket.getSinglePrice() * ticketNum));
                String resultMessage = "";
                if (paidSuccess) {
                    resultMessage = "?? " + (currentTicket.getSinglePrice() * ticketNum) + "\nRefund success";
                    //remove the ticket in the database
                    removeTicketsById(currentTicket.getTicketId(), ticketNum);
                    getTicketById(currentTicket.getTicketId());
                } else {
                    resultMessage = "Sorry, refund failed";
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
                getTickets();
            }
        }).start();
    }

    //jump to the nearby page
    public void toNearby(){
        Intent intent = new Intent(getActivity(), MyPlansInfoNearbyActivity.class);
        intent.putExtra("nearbyPOIs", currentJourney.getCastle().getNearbyPOIs());
        startActivity(intent);
    }

    //init the info page
    public void initInfo(){
        TextView castle = this.view.findViewById(R.id.myPlanInfoCastle);
        TextView date = this.view.findViewById(R.id.myPlanInfoDate);
        TextView departTime = this.view.findViewById(R.id.myPlanInfoDepartTime);
        TextView returnTime = this.view.findViewById(R.id.myPlanInfoReturnTime);
        TextView ticketCount = this.view.findViewById(R.id.myPlanInfoTicketCount);
//        TextView kidNum = this.view.findViewById(R.id.myPlanInfoKidsNum);
        TextView totalPrice = this.view.findViewById(R.id.myPlanInfoTotalPrice);
        LinearLayout myPlanInfoRoutes = this.view.findViewById(R.id.myPlanInfoRoutes);
        LinearLayout myPlanInfoReturnRoutes = this.view.findViewById(R.id.myPlanInfoReturnRoutes);

        castle.setText("");
        date.setText("");
        departTime.setText("");
        returnTime.setText("");
        ticketCount.setText("");
//        kidNum.setText("");
        totalPrice.setText("");
        myPlanInfoRoutes.removeAllViews();
        myPlanInfoReturnRoutes.removeAllViews();

        //change the status of the buy and remove buttons
        if (currentTicket.isPaid()) {
            this.view.findViewById(R.id.myPlanInfoBuy).setVisibility(View.GONE);
            Button removeButton = this.view.findViewById(R.id.myPlanInfoRemove);
            Button removeButton2 = this.view.findViewById(R.id.myPlanInfoRemoveNumButton);
            removeButton.setText("refund");
            removeButton2.setText("refund");
            myPlansDialoge.setDialogTitle("How many tickets are you going to delete ?");
            myPlansDialoge.setRemoveOrRefund("refund");

        }else{
            this.view.findViewById(R.id.myPlanInfoBuy).setVisibility(View.VISIBLE);
            Button removeButton = this.view.findViewById(R.id.myPlanInfoRemove);
            Button removeButton2 = this.view.findViewById(R.id.myPlanInfoRemoveNumButton);
            removeButton.setText("remove");
            removeButton2.setText("remove");
            myPlansDialoge.setDialogTitle("How many plans are you going to remove ?");
            myPlansDialoge.setRemoveOrRefund("remove");
        }

//        //the layout not visible until the loading is done
//        this.view.findViewById(R.id.myPlanInfoLoading).setVisibility(View.VISIBLE);
//        this.view.findViewById(R.id.myPlanInfoLoadingAfter).setVisibility(View.GONE);
    }

}