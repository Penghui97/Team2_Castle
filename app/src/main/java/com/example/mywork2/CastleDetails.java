package com.example.mywork2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mywork2.CastleFragment.CastleMapFragment;
import com.example.mywork2.CastleFragment.CastleViewPageAdapter;
import com.example.mywork2.CastleFragment.PanelOverCastleFragment;
import com.example.mywork2.CastleFragment.PanelTicketsFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class CastleDetails extends AppCompatActivity implements View.OnClickListener{
    private String castleName;
    private String linkAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_castle_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        castleName = (String) bundle.get("castle name");
        linkAbout = (String)bundle.get("links");

        TextView header = findViewById(R.id.header_title);
        header.setText(castleName);

        ImageView header_back = findViewById(R.id.back_button);
        header_back.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.castle_tabLayout);
        ViewPager viewPager = findViewById(R.id.castleViewPager);
        tabLayout.setupWithViewPager(viewPager);

        CastleViewPageAdapter castleViewPageAdapter = new CastleViewPageAdapter(getSupportFragmentManager()
                , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        castleViewPageAdapter.addFragment(new PanelOverCastleFragment(castleName,linkAbout),"castle");
        castleViewPageAdapter.addFragment(new PanelTicketsFragment(castleName),"tickets");
        castleViewPageAdapter.addFragment(new CastleMapFragment(),"map");
        viewPager.setAdapter(castleViewPageAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_castle_58);
        tabLayout.getTabAt(1).setIcon(R.drawable.icons8_two_tickets_32);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_map_24);
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