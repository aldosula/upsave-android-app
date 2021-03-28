package com.appsnipp.modernlogin.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.appsnipp.modernlogin.PrefManagers.PrefManager;
import com.appsnipp.modernlogin.R;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    private Button getStarted;
    private Button loginBtn;
    private Button nextBtn;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
          // launchHomeScreen();
           //finish();
       }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_start);

        viewPager =  findViewById(R.id.view_pager);
        dotsLayout =  findViewById(R.id.layoutDots);
        //btnSkip =  findViewById(R.id.btn_skip);
        loginBtn = findViewById(R.id.loginBtn);
        nextBtn = findViewById(R.id.nextBtn);
        getStarted = findViewById(R.id.getStarted);



        // layouts of all welcome sliders
        // last layout is dummy for handling exit on slide
        layouts = new int[]{
                R.layout.walkthrogh_one,
                R.layout.walkthrogh_two,
                R.layout.walkthrogh_three,
                };

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(layouts.length -1);
               // launchHomeScreen();
            }
        });
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(layouts.length -1);
                hideButtons();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               // checking for last page
                // if last page home screen will be launched
               int current = getItem( +1);
               if (current < layouts.length) {
                   // move to next screen
                  viewPager.setCurrentItem(current);
                } else {
                   viewPager.setCurrentItem(layouts.length -1);
                   hideButtons();

                  // launchHomeScreen();
              }
           }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(5, 0, 0, 0);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setWidth(30);
            dots[i].setHeight(5);
            dots[i].setLayoutParams(textLayoutParams);
            dots[i].setBackground(getResources().getDrawable(R.drawable.onboarding_dotunselected));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }

        //hiding the last dot , as the last layout is dummy for handling exit on last page
       // dots[layouts.length-1].setVisibility(View.GONE);

        if (dots.length > 0){
            dots[currentPage].setWidth(70);
            dots[currentPage].setBackground(getResources().getDrawable(R.drawable.onboarding_dot_selected));
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void hideButtons(){

        loginBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        getStarted.setVisibility(View.VISIBLE);

    }

    private void startLoginActivity(){
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
//                btnNext.setText(getString(R.string.start));
              //  btnSkip.setText("SKIP INTRO");
                //launchHomeScreen();
//                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
//                btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
