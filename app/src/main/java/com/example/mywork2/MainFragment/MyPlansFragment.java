package com.example.mywork2.MainFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mywork2.MainActivity;
import com.example.mywork2.MyPlansInfoActivity;
import com.example.mywork2.R;
import com.example.mywork2.adapter.MyPlansAdapter;
import com.example.mywork2.dao.TicketDao;
import com.example.mywork2.domain.Ticket;

import java.util.ArrayList;

public class MyPlansFragment extends Fragment {
    private View view;

    public MyPlansFragment() {
        // Required empty public constructor
    }


    //the handler to receive the data from the dao thread
    private LinearLayout myPlansNoPlansLayout;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:

                    break;
                case 0x22:
                    ArrayList<Ticket> tickets = (ArrayList<Ticket>) msg.obj;
                    removeTickets();
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
        getTickets();
        return view;
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
                ArrayList<Ticket> tickets = ticketDao.getTicketsByUsername("root");
                Message message = handler.obtainMessage();
                message.what = 0x22;
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
            return;
        }else{
            //set the my plan list
            ListView listView = this.view.findViewById(R.id.myPlansListView);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new MyPlansAdapter(tickets, this.view.findViewById(R.id.myPlansLayout1).getContext()));
            //set the item onclick listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    toMyPlanInfo(tickets.get(i).getTicketId());
                }
            });
        }
    }

    //remove the tickets
    public void removeTickets(){
        myPlansNoPlansLayout.setVisibility(View.GONE);
        ListView listView = this.view.findViewById(R.id.myPlansListView);
        listView.setVisibility(View.GONE);
    }

    //jump to the detailed information of this plan
    public void toMyPlanInfo(String ticketId){
        Intent intent = new Intent(getActivity(), MyPlansInfoActivity.class);
        intent.putExtra("ticketId", ticketId);
        startActivity(intent);
    }

}