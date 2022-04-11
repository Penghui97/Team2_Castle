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
                CastleDetails.startCastleActivity(getActivity(),"alnwic");
                break;
            case R.id.castle2:
                CastleDetails.startCastleActivity(getActivity(),"auckland");
                break;
            case R.id.castle3:
                CastleDetails.startCastleActivity(getActivity(),"barnard");
                break;
            case R.id.castle4:
                CastleDetails.startCastleActivity(getActivity(),"bamburgh");
                break;
            default:
                break;
        }
    }

}