package com.appsnipp.modernlogin;

import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private String username;
    private  String currency = "$";
    private String welcome_name;
    CustomProgressBar cpb;
    TextView text;
    CircleImageView profile;
    private int ammount = 1000 ;
    Button open_rtf;
    private DecoView mDecoView;
    private int mBackIndex;
    private int mSeries1Index;
    private final float mSeriesMax = 50f;
    FirebaseAuth fAuth;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        mDecoView =  view.findViewById(R.id.dynamicArcView);
        open_rtf = view.findViewById(R.id.rtf);
        text = view.findViewById(R.id.textViewUser);
        profile = view.findViewById(R.id.profileCircleImageView);

        createBackSeries();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(view.getContext());
        if (acct != null) {

            String personName = acct.getGivenName();

            Uri personPhoto = acct.getPhotoUrl();

            text.setText( "Welcome "+personName + "!");

            Glide.with(HomeFragment.this.getActivity()).load(String.valueOf(personPhoto)).into(profile);





        }

// change amout to set max value of progressbar

        final SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#8e5aeb"))
                .setRange(0, ammount, 0)
                .setInitialVisibility(false)
                .build();

        final TextView textPercentage =  view.findViewById(R.id.textPercentage);
        seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem2.getMinValue()) / (seriesItem2.getMaxValue() - seriesItem2.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
 /*

        final TextView textToGo =  view.findViewById(R.id.textRemaining);
        seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textToGo.setText(String.format("%.1f Km to goal", seriesItem2.getMaxValue() - currentPosition));

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
*/

        final TextView textActivity1 =  view.findViewById(R.id.textActivity1);
        seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete,  float currentPosition) {


                textActivity1.setText(String.format("%.2f $", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        mSeries1Index = mDecoView.addSeries(seriesItem2);

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String budget = dataSnapshot.child("budget").getValue().toString();
                textActivity1.setText(String.format(budget + " $" ));
                createEvents(budget);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });














        open_rtf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                switch (v.getId()) {
                    // ...
                    case R.id.rtf:
                       fragment = new ReportFragment();
                       replaceFragment(fragment);

                    break;
                }
            }
        });


        return  view;
        // return inflater.inflate(R.layout.content_main, container, false);


    }


    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);

    }
    private void createDataSeries1() {

    }



    private void createEvents(String budget_to_float) {

        float budget = Float.parseFloat(budget_to_float);
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(1000)
                .setDelay(100)
                .build());

        // change first parameter to set current value which is converted to percentage
        mDecoView.addEvent(new DecoEvent.Builder(budget)
                .setIndex(mSeries1Index)
                .setDelay(2250)
                .build());
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}
