package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mywork2.CastleFragment.CastleGalleryFragment;
import com.example.mywork2.CastleFragment.CastleViewPageAdapter;
import com.example.mywork2.CastleFragment.PanelOverCastleFragment;
import com.example.mywork2.CastleFragment.PanelNearbyFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CastleDetails extends AppCompatActivity implements View.OnClickListener{
    private String castleName;
    private String linkAbout;
    private ArrayList<Integer> imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_castle_details);

        //get the input parameters
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        castleName = (String) bundle.get("castle name");
        linkAbout = (String)bundle.get("links");
        imageId = (ArrayList<Integer>) bundle.get("imageId");

        //set header text
        TextView header = findViewById(R.id.header_title);
        header.setText(castleName);

        ImageView header_back = findViewById(R.id.back_button);
        header_back.setOnClickListener(this);

        //set the navi tab to navigate the fragement
        TabLayout tabLayout = findViewById(R.id.castle_tabLayout);
        ViewPager viewPager = findViewById(R.id.castleViewPager);
        tabLayout.setupWithViewPager(viewPager);

        // the page adapter for the tabLayout
        CastleViewPageAdapter castleViewPageAdapter = new CastleViewPageAdapter(getSupportFragmentManager()
                , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        castleViewPageAdapter.addFragment(new PanelOverCastleFragment(castleName,linkAbout),"castle");
        castleViewPageAdapter.addFragment(new PanelNearbyFragment(castleName),"nearby");
        castleViewPageAdapter.addFragment(new CastleGalleryFragment(imageId),"gallery");
        viewPager.setAdapter(castleViewPageAdapter);
        //set image  for the tab
        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_castle_58);
        tabLayout.getTabAt(1).setIcon(R.drawable.icons8_nearby_64);
        tabLayout.getTabAt(2).setIcon(R.drawable.icons8_picture_64);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button:
                this.finish();
                break;
        }
    }
}