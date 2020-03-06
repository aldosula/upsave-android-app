package com.appsnipp.modernlogin.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsnipp.modernlogin.Activities.LoginActivity;
import com.appsnipp.modernlogin.OtherJavaFiles.CustomProgressBar;
import com.appsnipp.modernlogin.R;
import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
    TextView money_left;
    TextView label;
    TextView expense_amount;
    TextView category_name;
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
        label = view.findViewById(R.id.label);
        category_name = view.findViewById(R.id.category_name);
        expense_amount = view.findViewById(R.id.expense_amount);

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



        FloatingActionButton expense_action_button = view.findViewById(R.id.add_expense);

        expense_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Expense fragment", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton income_action_button = view.findViewById(R.id.add_income);

        income_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Income fragment", Toast.LENGTH_SHORT).show();
            }
        });




        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
         TextView mv = view.findViewById(R.id.max_value);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             String max_value ="0";
             try {
                   max_value = dataSnapshot.child("budget").getValue().toString();
                 mv.setText(max_value);
             }catch (NullPointerException e){
                   max_value = "0";
                 mv.setText(max_value);

             }


             try {
                 long count = dataSnapshot.child("expenses").getChildrenCount();
                 String expense_nr = Long.toString(count);
                 String category = dataSnapshot.child("expenses").child(expense_nr).child("category").getValue().toString();
                 String expense = dataSnapshot.child("expenses").child(expense_nr).child("expense").getValue().toString();
                 category_name.setText(category);
                 String expense_format ="- $ "+expense+".00";
                 expense_amount.setText(expense_format);

             }catch (NullPointerException e){
                 long count = dataSnapshot.child("expenses").getChildrenCount();
                 String expense_nr = Long.toString(count);
                 String category ="N/A";
                 String expense = "0";
                 category_name.setText(category);
                 String expense_format ="- $ "+expense+".00";
                 expense_amount.setText(expense_format);


             }




                Float max_value_to_float = Float.parseFloat(max_value);
                final SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#8e5aeb"))
                        .setRange(0,max_value_to_float , 0)
                        .setInitialVisibility(false)
                        .build();

                final TextView textPercentage =  view.findViewById(R.id.textPercentage);
                seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                    @Override
                    public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                        float percentFilled = ((currentPosition - seriesItem2.getMinValue()) / (seriesItem2.getMaxValue() - seriesItem2.getMinValue()));
                        textPercentage.setText(String.format("%.0f%%", percentFilled* 100.0f  ));

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

                final TextView money_left = view.findViewById(R.id.money_left);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       try {



                       /*    double count = 0;
                           int index = 1;

                           for(DataSnapshot ds : dataSnapshot.child("incomes").getChildren()) {
                               String index_to_string = Integer.toString(index);
                               String sum_str = ds.child(index_to_string).child("income").getValue().toString();
                               double sum = Double.parseDouble(sum_str);
                               count = count + sum;
                               index++;


                           }

                        */
                           String ml = dataSnapshot.child("budget progress").getValue().toString();
                        //  String count_str = Double.toString(count);
                           String budget = dataSnapshot.child("budget").getValue().toString();
                           double budget_to_double = Double.parseDouble(budget);

                           money_left.setText(ml);
                           textActivity1.setText(String.format(ml + " $"));
                           createEvents(ml);

                       }catch (NullPointerException e){
                           String ml = "0.00";
                           money_left.setText(ml);
                           textActivity1.setText(String.format(ml + " $"));
                           createEvents(ml);

                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





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

        float money_left = Float.parseFloat(budget_to_float);
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(1000)
                .setDelay(100)
                .build());

        // change first parameter to set current value which is converted to percentage
        mDecoView.addEvent(new DecoEvent.Builder(money_left)
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
