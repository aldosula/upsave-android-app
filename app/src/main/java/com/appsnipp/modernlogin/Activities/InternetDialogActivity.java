package com.appsnipp.modernlogin.Activities;

import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.modernlogin.OtherJavaFiles.InternetDialog;
import com.appsnipp.modernlogin.R;

public class InternetDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_dialog);

        // CALL getInternetStatus() function to check for internet and display error dialog
        if(new InternetDialog(this).getInternetStatus()){
            Toast.makeText(this, "Online", Toast.LENGTH_SHORT).show();
        }
    }
}
