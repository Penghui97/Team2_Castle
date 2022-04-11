package com.example.mywork2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mywork2.R;
import com.example.mywork2.domain.NearbyPOI;

import java.util.ArrayList;


public class NearbyPOIsAdapter extends BaseAdapter {
    private ArrayList<NearbyPOI> nearbyPOIS;
    private Context context;

    public NearbyPOIsAdapter(ArrayList<NearbyPOI> nearbyPOIS, Context context) {
        this.nearbyPOIS = nearbyPOIS;
        this.context = context;
    }

    @Override
    public int getCount() {
        return nearbyPOIS.size();
    }

    @Override
    public Object getItem(int i) {
        return nearbyPOIS.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.my_plans_info_nearby_item, viewGroup, false);
            //put the textViews of list_item to the viewHolder
            viewHolder.name = view.findViewById(R.id.poiName);
            viewHolder.category = view.findViewById(R.id.poiCategory);
            viewHolder.rating = view.findViewById(R.id.poiRating);
            viewHolder.postcode = view.findViewById(R.id.poiPostcode);
            //put the holder into the view's tag
            //in case to use next time
            view.setTag(viewHolder);
        }else{
            //next time
            viewHolder = (ViewHolder) view.getTag();
        }
        //set the content of the textView in the holder
        viewHolder.name.setText(nearbyPOIS.get(i).getPoiName() + "");
        viewHolder.category.setText(nearbyPOIS.get(i).getCategory() + "");
        viewHolder.rating.setText(nearbyPOIS.get(i).getRating() + "");
        viewHolder.postcode.setText(nearbyPOIS.get(i).getPostcode() + "");
        return view;
    }

    private final class ViewHolder{
        TextView name;
        TextView category;
        TextView rating;
        TextView postcode;
    }
}
