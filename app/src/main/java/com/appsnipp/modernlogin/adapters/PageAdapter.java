package com.appsnipp.modernlogin.adapters;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> mFragmentList;
    ArrayList<String>  mFragmentListTitle;

    private int numOfTabs;
    public PageAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentListTitle = new ArrayList<>();

    }

    @Override
    public Fragment getItem(int position) {



       return mFragmentList.get(position);


    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){

        mFragmentList.add(fragment);
        mFragmentListTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentListTitle.get(position);
    }


}
