package com.example.mywork2.MainFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mywork2.CastleDetails;
import com.example.mywork2.R;

import java.util.ArrayList;

public class CastlesFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ArrayList<Integer> imageId;


    public CastlesFragment() {
        //the image source id from the drawer
        imageId = new ArrayList<>();
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_castles, container, false);

        //get the linear layout for the castles
        LinearLayout alnwic = view.findViewById(R.id.castle1);
        alnwic.setOnClickListener(this);

        LinearLayout auckland = view.findViewById(R.id.castle2);
        auckland.setOnClickListener(this);

        LinearLayout barnard = view.findViewById(R.id.castle3);
        barnard.setOnClickListener(this);

        LinearLayout bamburgh = view.findViewById(R.id.castle4);
        bamburgh.setOnClickListener(this);

        return view;
    }
    //set the click listener for the castle
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.castle1:
                imageId.clear();
                //set the pictures display for ths castle alnwick
                imageId.add(R.drawable.alnwick_gallery_1);
                imageId.add(R.drawable.alnwick_gallery_2);
                imageId.add(R.drawable.alnwick_gallery_3);
                imageId.add(R.drawable.alnwick_gallery_4);
                imageId.add(R.drawable.alnwick_gallery_5);
                //start the new castle details activity
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","alnwic")
                        .putExtra("links","https://www.alnwickcastle.com/")
                        .putExtra("imageId",imageId));
                break;
            case R.id.castle2:
                imageId.clear();
                imageId.add(R.drawable.auckland_gallery_1);
                imageId.add(R.drawable.auckland_gallery_2);
                imageId.add(R.drawable.auckland_gallery_3);
                imageId.add(R.drawable.auckland_gallery_4);
                imageId.add(R.drawable.auckland_gallery_5);
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","auckland")
                        .putExtra("links","https://aucklandproject.org/")
                        .putExtra("imageId",imageId));
                break;
            case R.id.castle3:
                imageId.clear();
                imageId.add(R.drawable.barnard_gallery_1);
                imageId.add(R.drawable.barnard_gallery_2);
                imageId.add(R.drawable.barnard_gallery_3);
                imageId.add(R.drawable.barnard_gallery_4);
                imageId.add(R.drawable.barnard_gallery_5);
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","barnard")
                        .putExtra("links","https://www.bamburghcastle.com/")
                        .putExtra("imageId",imageId));
                break;
            case R.id.castle4:
                imageId.clear();
                imageId.add(R.drawable.bamburgh_gallery_1);
                imageId.add(R.drawable.bamburgh_gallery_2);
                imageId.add(R.drawable.bamburgh_gallery_3);
                imageId.add(R.drawable.bamburgh_gallery_4);
                imageId.add(R.drawable.bamburgh_gallery_5);
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","bamburgh")
                        .putExtra("links","https://www.english-heritage.org.uk/visit/places/barnard-castle/?utm_source=Trip%20Advisor&utm_campaign=Local%20Listings&utm_medium=Trip%20Advisor%20Profiles&utm_content=barnard%20castle&utm_source=Trip%20Advisor&utm_campaign=Local%20Listings&utm_medium=Trip%20Advisor%20Profiles&utm_content=barnard%20castle")
                        .putExtra("imageId",imageId));
                break;
            default:
                break;
        }
    }

}