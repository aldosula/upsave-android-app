package com.appsnipp.modernlogin;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class RegisterActivity extends AppCompatActivity {
 RelativeLayout relativeLayout;
 AnimationDrawable animationDrawable;
 EditText name, email, password, phone, confrimpassword;
Button reg_button;
 FirebaseAuth fAuth;
    ProgressBar progressBar;
 DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        changeStatusBarColor();
        relativeLayout =(RelativeLayout) findViewById(R.id.registerlayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        name =(EditText) findViewById(R.id.editTextName);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        phone = (EditText) findViewById(R.id.editTextMobile);
        confrimpassword=(EditText) findViewById(R.id.editTextRePassword);
        reg_button = (Button) findViewById(R.id.cirRegisterButton);
        progressBar = findViewById(R.id.progressBar);
        fAuth =FirebaseAuth.getInstance();




        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remail = email.getText().toString().trim();
                String rpassword = password.getText().toString().trim();
                String rname = name.getText().toString().trim();
                String rconfirmpassword = confrimpassword.getText().toString().trim();
                String rphone = phone.getText().toString().trim();


                if(TextUtils.isEmpty(rconfirmpassword)){

                    confrimpassword.setError("Please confirm your password");
                }
                if(TextUtils.isEmpty(rname)){
                    name.setError("Name is required.");
                }
                if(TextUtils.isEmpty(rphone)){
                    name.setError("Phone number is required.");
                }
                if(TextUtils.isEmpty(remail)){
                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(rpassword)){
                    password.setError("Password is Required.");
                    return;
                }

                if(rpassword.length() < 8){
                    password.setError("Password must have 8 or more characters");
                    return;
                }



                // register the user in firebase

                if(rconfirmpassword.equals(rpassword)) {


                    progressBar.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(remail, rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                User_Data user = new User_Data(
                                        rname,
                                        remail,
                                        rphone,
                                        0.00f
                                );

                                FirebaseDatabase.getInstance().getReference()
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                });


                            } else {
                                Toast.makeText(RegisterActivity.this, "Error:  " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    confrimpassword.setText("");
                    confrimpassword.setError("Password doesn't match");

                }
            }
        });



    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }




}
