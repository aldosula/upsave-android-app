package com.appsnipp.modernlogin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.modernlogin.R;

public class ActivitySplashSc extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_sc);



    }
}
