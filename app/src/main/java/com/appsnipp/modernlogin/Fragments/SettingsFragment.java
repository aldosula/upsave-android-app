package com.appsnipp.modernlogin.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsnipp.modernlogin.Activities.LoginActivity;
import com.appsnipp.modernlogin.PrefManagers.DarkModePrefManager;
import com.appsnipp.modernlogin.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {
    CircleImageView imageView;
    TextView name, email, id;
    Button signOut;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth fAuth;

    DatabaseReference databaseReference;
    private FrameLayout fragmentContainer;
    private Switch darkModeSwitch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.activity_settings, container,false);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        imageView = view.findViewById(R.id.profile);
        name= view.findViewById(R.id.usernameTextView);
        email = view.findViewById(R.id.email);
        id = view.findViewById(R.id.id);
        signOut = view.findViewById(R.id.button);
        fragmentContainer = view.findViewById(R.id.frame_container);


        fAuth = FirebaseAuth.getInstance();



        if(new DarkModePrefManager(SettingsFragment.this.getActivity()).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(new DarkModePrefManager(SettingsFragment.this.getActivity()).isNightMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(SettingsFragment.this.getActivity());
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SettingsFragment.this.getActivity().recreate();
            }
        });




        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.button:
                        signOut();
                        break;
                    // ...
                }

            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(view.getContext());
        if (acct != null) {

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

                        name.setText(personName);
                        email.setText(personEmail);
                       // id.setText(personId);
                        Glide.with(SettingsFragment.this.getActivity()).load(String.valueOf(personPhoto)).into(imageView);





        }
        else {
            try {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String personName = dataSnapshot.child("name").getValue().toString();
                        String personEmail = dataSnapshot.child("email").getValue().toString();

                        name.setText(personName);
                        email.setText(personEmail);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (NullPointerException e) {
                LoadNextActivity();

            }

        }

      return view;
    }



    private void LoadNextActivity(){

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(SettingsFragment.this.getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SettingsFragment.this.getActivity(), "Signed out!", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();

                    }
                });
        startActivity(new Intent(SettingsFragment.this.getActivity(), LoginActivity.class));
        getActivity().finish();

    }
}
