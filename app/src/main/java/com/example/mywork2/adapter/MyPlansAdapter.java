package com.example.mywork2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mywork2.MyPlansInfoActivity;
import com.example.mywork2.R;
import com.example.mywork2.domain.*;

import java.util.ArrayList;

public class MyPlansAdapter extends BaseAdapter {
    private ArrayList<Ticket> tickets;
    private Context context;

    public MyPlansAdapter(ArrayList<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int i) {
        return tickets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        //first time
        if(view == null){
            viewHolder = new ViewHolder();
            //set the list_item.xml as the view
            view = LayoutInflater.from(context).inflate(R.layout.my_plans_item, viewGroup, false);
            //put the textViews of list_item to the viewHolder
            viewHolder.departTime = view.findViewById(R.id.myPlanDepartTime);
            viewHolder.returnTime = view.findViewById(R.id.myPlanReturnTime);
            viewHolder.date = view.findViewById(R.id.myPlanDate);
            viewHolder.castle = view.findViewById(R.id.myPlanCastle);
            viewHolder.paidStatus = view.findViewById(R.id.myPlanPaidStatus);
            viewHolder.ticketCount = view.findViewById(R.id.the_ticket_count);
            viewHolder.viewDetailsButton = view.findViewById(R.id.myPlanViewDetailButton);
            //put the holder into the view's tag
            //in case to use next time
            view.setTag(viewHolder);
        }else{
            //next time
            viewHolder = (ViewHolder) view.getTag();
        }
        //set the content of the textView in the holder
        viewHolder.departTime.setText(tickets.get(i).getTime() + "");
        viewHolder.returnTime.setText(tickets.get(i).getReturnTime() + "");
        viewHolder.date.setText(tickets.get(i).getDate() + "");
        viewHolder.castle.setText(tickets.get(i).getCastleName() + "");
        viewHolder.ticketCount.setText(tickets.get(i).getAdultQuantity() + tickets.get(i).getKidsQuantity() + "");
        viewHolder.paidStatus.setText(tickets.get(i).isPaid() ? "paid":"notPaid");
        viewHolder.viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyPlansInfoActivity.class);
                intent.putExtra("ticketId", tickets.get(i).getTicketId());
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    //the container to hold each cell in the list
    private final class ViewHolder{
        TextView departTime;
        TextView returnTime;
        TextView date;
        TextView castle;
        TextView paidStatus;
        TextView ticketCount;
        TextView viewDetailsButton;
    }
}
