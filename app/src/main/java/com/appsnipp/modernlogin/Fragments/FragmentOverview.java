package com.appsnipp.modernlogin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appsnipp.modernlogin.R;
import com.appsnipp.modernlogin.adapters.PageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class FragmentOverview extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);


        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        TabItem expense = view.findViewById(R.id.expense_tab);
        TabItem income = view.findViewById(R.id.income_tab);
        TabItem deadline_pay = view.findViewById(R.id.deadline_pay);
        TabItem reports = view.findViewById(R.id.reports);
        ViewPager viewPager = view.findViewById(R.id.viewPager);


        PageAdapter pageAdapter = new PageAdapter(getFragmentManager());

        pageAdapter.addFragment(new ReportFragment(),"Expense");
        pageAdapter.addFragment(new IncomeReportFragment(),"Income");
        pageAdapter.addFragment(new FragmentIncome(),"Deadline Payments");
        pageAdapter.addFragment(new FragmentBudget(),"Reports");

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }
}
