package com.appsnipp.modernlogin.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsnipp.modernlogin.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    CircleImageView profilepic;
    TextView name, email;
    Button backHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        name = view.findViewById(R.id.profilename);
        email = view.findViewById(R.id.profileemail);
        profilepic = view.findViewById(R.id.profileimage);
        fAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(view.getContext());
        if (acct != null) {


            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();



            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            name.setText(personName);
            email.setText(personEmail);
            Glide.with(ProfileFragment.this.getActivity()).load(String.valueOf(personPhoto)).into(profilepic);

        } else {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String PersonName = dataSnapshot.child("name").getValue().toString();
                    String PersonEmail = dataSnapshot.child("email").getValue().toString();

                    name.setText(PersonName);
                    email.setText(PersonEmail);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }





        return view;
    }



    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
