package com.appsnipp.modernlogin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.modernlogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPass  extends AppCompatActivity {

    Button send_reset_link;
    EditText email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        send_reset_link = findViewById(R.id.reset_btn);
        email = findViewById(R.id.email_reset);


            firebaseAuth = FirebaseAuth.getInstance();


        send_reset_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()) {
                             LoadNextActivity();

                         }

                         else{
                             Toast.makeText(ActivityForgotPass.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                         }
                    }
                });


            }
        });


    }

    private void LoadNextActivity(){

        startActivity(new Intent(this, ActivityEmailSent.class));
        finish();
    }
}
