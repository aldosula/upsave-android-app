package com.appsnipp.modernlogin.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.appsnipp.modernlogin.Fragments.FragmentExpense;
import com.appsnipp.modernlogin.Fragments.FragmentIncome;
import com.appsnipp.modernlogin.Fragments.FragmentBudget;
import com.appsnipp.modernlogin.R;
import com.appsnipp.modernlogin.adapters.PageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ActivityRecord extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        TabLayout tabLayout = findViewById(R.id.tablayout);
        TabItem expense = findViewById(R.id.expense);
        TabItem income = findViewById(R.id.income);
        TabItem transfer = findViewById(R.id.transfer);
        ViewPager viewPager = findViewById(R.id.viewPager);



        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());

        pageAdapter.addFragment(new FragmentExpense(),"Expense");
        pageAdapter.addFragment(new FragmentIncome(),"Income");
        pageAdapter.addFragment(new FragmentBudget(),"Transfer");

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

    }
}
