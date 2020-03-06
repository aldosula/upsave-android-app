package com.appsnipp.modernlogin.Activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.appsnipp.modernlogin.PrefManagers.DarkModePrefManager;
import com.appsnipp.modernlogin.Fragments.GetBudgetFragment;
import com.appsnipp.modernlogin.Fragments.HomeFragment;
import com.appsnipp.modernlogin.OtherJavaFiles.InternetDialog;
import com.appsnipp.modernlogin.Fragments.ProfileFragment;
import com.appsnipp.modernlogin.PrefManagers.PrefManager;
import com.appsnipp.modernlogin.R;
import com.appsnipp.modernlogin.Fragments.ReportFragment;
import com.appsnipp.modernlogin.Fragments.SettingsFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth fAuth;

    DatabaseReference databaseReference;

    private BottomNavigationView bottomNavigationView;
    private static final int MODE_DARK = 0;
    private static final int MODE_LIGHT = 1;
    private PrefManager prefManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                  //  startActivity(new Intent(getApplicationContext(), Home.class ));
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                   //fragment = new SettingsFragment();
                    return true;
                case R.id.navigationMyCourses:
                    //fragment = new ReportFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReportFragment()).commit();
                    return true;
                case R.id.navigationHome:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                    return true;
                case R.id.navigationSearch:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GetBudgetFragment()).commit();
                    return true;
                case R.id.navigationMenu:
                   // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    //drawer.openDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                    return true;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            return true;

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(new InternetDialog(this).getInternetStatus()){


        setDarkMode(getWindow());

            prefManager = new PrefManager(this);
            if (!prefManager.isFirstTimeLaunch()) {
                sendGoogleSignInData();
            }
            else {
                firstTimeSignInGoogle();
            }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    //  bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        //handling floating action menu

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_dark_mode) {
            //code for setting dark mode
            //true for dark mode, false for day mode, currently toggling on each click
            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void sendGoogleSignInData(){


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            fAuth = FirebaseAuth.getInstance();
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();


                //  User_Data user = new User_Data(
                //         personName,
                //        personEmail
                // );

                databaseReference=  FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                //       .setValue(user);
                databaseReference.child("name").setValue(personName);
                databaseReference.child("email").setValue(personEmail);

        }
        }



        public  void firstTimeSignInGoogle() {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            fAuth = FirebaseAuth.getInstance();
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();


                //  User_Data user = new User_Data(
                //         personName,
                //        personEmail
                // );

                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                //       .setValue(user);
                databaseReference.child("name").setValue(personName);
                databaseReference.child("email").setValue(personEmail);
                databaseReference.child("budget").setValue(0);


            }
        }





  //dark mode
    public void setDarkMode(Window window){
        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            changeStatusBar(MODE_DARK,window);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            changeStatusBar(MODE_LIGHT,window);
        }
    }
    public void changeStatusBar(int mode, Window window){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.contentStatusBar));
            //Light mode
            if(mode==MODE_LIGHT){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
}
