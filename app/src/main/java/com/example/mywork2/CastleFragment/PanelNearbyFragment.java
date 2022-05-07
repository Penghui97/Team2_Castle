package com.example.mywork2.CastleFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mywork2.R;
import com.example.mywork2.adapter.NearbyPOIsAdapter;
import com.example.mywork2.dao.POIDao;
import com.example.mywork2.domain.NearbyPOI;

import java.util.ArrayList;

public class PanelNearbyFragment extends Fragment {
    private String castleName;
    private ArrayList<NearbyPOI> pois;
    private View view;

    //the handler to receive the data from the dao thread
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0x11:
                    showPOIS();
                    break;
            }
        }
    };

    public PanelNearbyFragment(String castleName) {
        switch (castleName){
            case "alnwic":
                this.castleName = "Alnwick Castle";
                break;
            case "auckland":
                this.castleName = "Auckland Castle";
                break;
            case "barnard":
                this.castleName = "Barnard Castle";
                break;
            case "bamburgh":
                this.castleName = "Bamburgh Castle";
                break;
        }
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getPOIs();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_panel_nearby, container, false);
        return view;
    }

    //get all the POIs by castle name
    public void getPOIs(){
        new Thread(() ->{
            POIDao poiDao = new POIDao();
            pois = poiDao.getPOIsByCastleName(castleName);
            Message message = handler.obtainMessage();
            message.what = 0x11;
            handler.sendMessage(message);
        }).start();
    }
    //show the POIS
    public void showPOIS(){
        ListView listView = this.view.findViewById(R.id.castlePOIsListView);
        listView.setAdapter(new NearbyPOIsAdapter(pois, view.findViewById(R.id.castlePOIsLayout).getContext()));
    }
}