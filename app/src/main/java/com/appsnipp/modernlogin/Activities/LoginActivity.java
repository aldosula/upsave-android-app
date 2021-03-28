package com.appsnipp.modernlogin.Activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.appsnipp.modernlogin.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {

    // start signin functions
    private static final int RC_SIGN_IN = 1;
    EditText email, password;
    Button login_button;
    FirebaseAuth fAuth;

    static final int GOOGLE_SIGN = 123;

    ImageButton google_login_btn;
    TextView text;
    ImageView image;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton mGoogleButton;
    ProgressBar progressBar;
    Button forgot_pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       //google login

        mGoogleButton =(SignInButton) findViewById(R.id.google_login_button);
        text = findViewById(R.id.name);
        image = findViewById(R.id.image);
        progressBar = findViewById(R.id.progressBar);
        forgot_pass = findViewById(R.id.forgot_pass);


        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();

            }
        });

        progressBar.setVisibility(View.INVISIBLE);
        mGoogleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_login_button:
                        progressBar.setVisibility(View.VISIBLE);
                        signIn();
                        break;

                }


            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);






        //firebase login

        email = (EditText) findViewById(R.id.LoginTextEmail);
        password = (EditText) findViewById(R.id.LoginTextPassword);
        login_button = (Button) findViewById(R.id.cirLoginButton);
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(this, MainActivity.class ));
            finish();
        }



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remail = email.getText().toString().trim();
                String rpassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(remail)) {
                    email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(rpassword)) {
                    password.setError("Password is Required.");
                    return;
                }

                if (rpassword.length() < 8) {
                    password.setError("Password must have 8 or more characters");
                    return;
                }

                //authentication with firebase
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(remail, rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Logged in successfuly", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void forgotPass(){
        startActivity(new Intent(this, ActivityForgotPass.class));
        finish();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }


    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

}
