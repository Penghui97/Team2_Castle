package com.example.mywork2.CastleFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author Jiacheng
 * function the tab adapter for the tab layout used in the main castle detail actitivity
 * modification date and description can be found in github repository history
 */

public class CastleViewPageAdapter extends FragmentPagerAdapter {
    // the fragments in this adapter
    private final ArrayList<Fragment> fragments = new ArrayList<>();
    // the title of the fragment added
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public CastleViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment,String title){
        fragments.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return fragmentTitle.get(position);
    }
}
