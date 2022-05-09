package com.example.mywork2.CastleFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mywork2.R;

import java.util.ArrayList;

public class CastleGalleryFragment extends Fragment {
    private ArrayList<Integer> imageId;

    public CastleGalleryFragment(ArrayList<Integer> imageId) {
        this.imageId=imageId;
        Log.d("sizec",""+imageId.size());
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_castle_gallery, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linearLayout = view.findViewById(R.id.gallery_linear);
        linearLayout.removeAllViews();
        Log.d("ad",""+imageId.size());
        for(int i :imageId){
            ImageView imageView = new ImageView(linearLayout.getContext());
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(i);
            linearLayout.addView(imageView);
        }

    }
}