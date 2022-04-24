package com.example.mywork2.CastleFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mywork2.R;

public class PanelOverCastleFragment extends Fragment {
    private String castleName;

    public PanelOverCastleFragment(String castleName) {
        this.castleName=castleName;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel_over_castle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.castle_picture);
        switch (castleName){
            case "alnwic":
                imageView.setImageResource(R.drawable.alnwic1);
                break;
            case "auckland":
                imageView.setImageResource(R.drawable.auckland1);
                break;
            case "barnard":
                imageView.setImageResource(R.drawable.barnard);
                break;
            case "bamburgh":
                imageView.setImageResource(R.drawable.bamburgh);
                break;
            default:
                break;
        }


    }
}