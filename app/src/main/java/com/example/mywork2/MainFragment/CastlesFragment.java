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

public class CastlesFragment extends Fragment implements View.OnClickListener {
    private View view;


    public CastlesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_castles, container, false);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.castle1:
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","alnwic")
                        .putExtra("links","https://www.alnwickcastle.com/"));
                break;
            case R.id.castle2:
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","auckland")
                        .putExtra("links","https://aucklandproject.org/"));
                break;
            case R.id.castle3:
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","barnard")
                        .putExtra("links","https://www.bamburghcastle.com/"));
                break;
            case R.id.castle4:
                startActivity(new Intent(getActivity(),CastleDetails.class)
                        .putExtra("castle name","bamburgh")
                        .putExtra("links","https://www.english-heritage.org.uk/visit/places/barnard-castle/?utm_source=Trip%20Advisor&utm_campaign=Local%20Listings&utm_medium=Trip%20Advisor%20Profiles&utm_content=barnard%20castle&utm_source=Trip%20Advisor&utm_campaign=Local%20Listings&utm_medium=Trip%20Advisor%20Profiles&utm_content=barnard%20castle"));
                break;
            default:
                break;
        }
    }

}