package com.example.mywork2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mywork2.R;
import com.example.mywork2.SearchPlanDetailsActivity;
import com.example.mywork2.SearchPlanInfoActivity;
import com.example.mywork2.Util.*;
import com.example.mywork2.domain.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PlanDetailAdapter extends BaseAdapter {
    private ArrayList<Journey> journeys;
    private SearchPlanDetailsActivity context;

    public PlanDetailAdapter(ArrayList<Journey> journeys, SearchPlanDetailsActivity context) {
        this.journeys = journeys;
        this.context = context;
    }

    @Override
    public int getCount() {
        return journeys.size();
    }

    @Override
    public Object getItem(int i) {
        return journeys.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.search_plan_item, viewGroup, false);
            //put the textView of list_item to the viewHolder
            viewHolder.start = view.findViewById(R.id.searchPlanStart);
            viewHolder.transportType = view.findViewById(R.id.searchPlanTransportType);
            viewHolder.time = view.findViewById(R.id.planDetailTime);
            viewHolder.price = view.findViewById(R.id.planDetailPrice);
            viewHolder.viewDetails = view.findViewById(R.id.searchPlanViewDetailsButton);
            /**
             * comment by J.Cheng
             * the transferNum is better in detials part
             * viewHolder.transferNum = view.findViewById(R.id.planDetailTransferNum);
             */

            //put the holder into the view's tag
            //in case to use next time
            view.setTag(viewHolder);
        }else{
            //next time
            viewHolder = (ViewHolder) view.getTag();
        }
        //set the content of the textView in the holder
        viewHolder.start.setText(journeys.get(i).getDeparture());
        viewHolder.transportType.setText(journeys.get(i).getRoutes().get(0).getTransport().getType());
        viewHolder.time.setText(journeys.get(i).getSingleDuration() + " min ");
        viewHolder.price.setText(" ￡ " + journeys.get(i).getTotalPrice() + " pp ");
//        viewHolder.transferNum.setText(journeys.get(i).getRoutes().size() + "");
        viewHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.toSearchPlanInfo(i);
//                //show the loading page until info has been loaded
                context.searchPlanInfoLoading.setVisibility(View.VISIBLE);
                context.getSearchPlanInfoLoadingAfter.setVisibility(View.GONE);
                //show the info page
                //and update the content
                context.getJourneyById(journeys.get(i).getJourneyId());
                //context.searchPlanInfoAllContent.setVisibility(View.VISIBLE);
                context.bottomSheetDialog.show();
            }
        });
        return view;
    }

    //the container to hold each cell in the list
    private final class ViewHolder{
        TextView start;
        TextView transportType;
        TextView time;
        TextView price;
//        TextView transferNum;
        TextView viewDetails;
    }
}
