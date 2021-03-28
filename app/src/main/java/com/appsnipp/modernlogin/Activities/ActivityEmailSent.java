package com.appsnipp.modernlogin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.modernlogin.R;

public class ActivityEmailSent extends AppCompatActivity {

    Button goToLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sent);


        goToLogin = findViewById(R.id.back_to_login);


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoadNextActivity();



            }
        });


    }

    private void LoadNextActivity(){

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
