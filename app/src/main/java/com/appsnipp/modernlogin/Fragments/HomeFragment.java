package com.appsnipp.modernlogin.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.appsnipp.modernlogin.Activities.ActivityEmailSent;
import com.appsnipp.modernlogin.Activities.ActivityRecord;
import com.appsnipp.modernlogin.Activities.LoginActivity;
import com.appsnipp.modernlogin.Activities.StartActivity;
import com.appsnipp.modernlogin.Database.Budget_Data;
import com.appsnipp.modernlogin.Database.Income_Data;
import com.appsnipp.modernlogin.OtherJavaFiles.CustomProgressBar;
import com.appsnipp.modernlogin.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
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
import com.google.firebase.database.Query;
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
    private TextView text;
    private TextView money_left;
    private TextView label;
    private TextView expense_amount;
    private  TextView category_name;
    private TextView savings,wants,needs,saving_amount,wants_amount,needs_amount;
    private RoundCornerProgressBar p1,p2,p3;

    private  CircleImageView profile;
    private int ammount = 1000 ;
    LinearLayout layout;
    Button open_rtf;
    private DecoView mDecoView;
    private int mBackIndex;
    private int mSeries1Index;
    private final float mSeriesMax = 50f;
    FirebaseAuth fAuth;
    ListView lv;
    FirebaseListAdapter adapter;

    
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
        savings = view.findViewById(R.id.savings);
        saving_amount = view.findViewById(R.id.savings_amount);
        wants = view.findViewById(R.id.wants);
        wants_amount= view.findViewById(R.id.wants_amount);
        needs = view.findViewById(R.id.needs);
        needs_amount = view.findViewById(R.id.needs_amount);
        layout = view.findViewById(R.id.layout_budgets);
        p1 = view.findViewById(R.id.progress_1);
        p2 = view.findViewById(R.id.progress_2);
        p3 = view.findViewById(R.id.progress_3);


        createBackSeries();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();



try {
    DatabaseReference db1 = FirebaseDatabase.getInstance().getReference()
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    db1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            try {
                String name = dataSnapshot.child("budgets").child("savings").child("Name").getValue().toString();
                String i_b = dataSnapshot.child("budgets").child("savings").child("initial_budget").getValue().toString();
                String a_b = dataSnapshot.child("budgets").child("savings").child("actual_budget").getValue().toString();
                String end_date = dataSnapshot.child("budgets").child("savings").child("end_date").getValue().toString();


                String name2 = dataSnapshot.child("budgets").child("needs").child("Name").getValue().toString();
                String i_b2 = dataSnapshot.child("budgets").child("needs").child("initial_budget").getValue().toString();
                String a_b2 = dataSnapshot.child("budgets").child("needs").child("actual_budget").getValue().toString();
                String end_date2 = dataSnapshot.child("budgets").child("needs").child("end_date").getValue().toString();

                String name3 = dataSnapshot.child("budgets").child("wants").child("Name").getValue().toString();
                String i_b3 = dataSnapshot.child("budgets").child("wants").child("initial_budget").getValue().toString();
                String a_b3 = dataSnapshot.child("budgets").child("wants").child("actual_budget").getValue().toString();
                String end_date3 = dataSnapshot.child("budgets").child("wants").child("end_date").getValue().toString();


                needs.setText(name2);
                needs_amount.setText("$ " + a_b2);
                float progress_max2 = Float.parseFloat(i_b2);
                float proggress_actual2 = Float.parseFloat(a_b2);
                p1.setMax(progress_max2);
                p1.setProgress(proggress_actual2);


                wants.setText(name3);
                wants_amount.setText("$ " + a_b3);
                float progress_max3 = Float.parseFloat(i_b3);
                float proggress_actual3 = Float.parseFloat(a_b3);
                p2.setMax(progress_max3);
                p2.setProgress(proggress_actual3);

                savings.setText(name);
                saving_amount.setText("$ " + a_b);
                float progress_max = Float.parseFloat(i_b);
                float proggress_actual = Float.parseFloat(a_b);
                p3.setMax(progress_max);
                p3.setProgress(proggress_actual);


            } catch (NullPointerException e) {


            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



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

        else{
            DatabaseReference db =  FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name  = dataSnapshot.child("name").getValue().toString();
                    text.setText("Welcome "+name + "!");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

}catch (NullPointerException e){

    LoadNextActivity();
}



        FloatingActionButton expense_action_button = view.findViewById(R.id.add_expense);

        expense_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(HomeFragment.this.getActivity(), ActivityRecord.class));
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




try {

    DatabaseReference db = FirebaseDatabase.getInstance().getReference()
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    TextView mv = view.findViewById(R.id.max_value);

    db.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




            String max_value = "0.00001";
            try {
                max_value = dataSnapshot.child("budget").getValue().toString();
                mv.setText(max_value);
            } catch (NullPointerException e) {
                max_value = "0";
                mv.setText(max_value);

            }


            try {
                long count = dataSnapshot.child("expenses").getChildrenCount();
                String expense_nr = Long.toString(count);
                String category = dataSnapshot.child("expenses").child(expense_nr).child("category").getValue().toString();
                String expense = dataSnapshot.child("expenses").child(expense_nr).child("expense").getValue().toString();
                category_name.setText(category);
                String expense_format = "- $ " + expense + ".00";
                expense_amount.setText(expense_format);

            } catch (NullPointerException e) {
                long count = dataSnapshot.child("expenses").getChildrenCount();
                String expense_nr = Long.toString(count);
                String category = "N/A";
                String expense = "0";
                category_name.setText(category);
                String expense_format = "- $ " + expense + ".00";
                expense_amount.setText(expense_format);


            }


            Float max_value_to_float = Float.parseFloat(max_value);
            final SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#8e5aeb"))
                    .setRange(0, max_value_to_float, 0)
                    .setInitialVisibility(false)
                    .build();

            final TextView textPercentage = view.findViewById(R.id.textPercentage);
            seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                    float percentFilled = ((currentPosition - seriesItem2.getMinValue()) / (seriesItem2.getMaxValue() - seriesItem2.getMinValue()));
                    textPercentage.setText(String.format("%.0f%%", percentFilled * 100.0f));

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

            final TextView textActivity1 = view.findViewById(R.id.textActivity1);
            seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {


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

                    } catch (NullPointerException e) {
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


}catch (NullPointerException e){

    LoadNextActivity();
}







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
    private void LoadNextActivity(){

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
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
