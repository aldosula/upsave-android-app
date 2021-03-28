package com.appsnipp.modernlogin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsnipp.modernlogin.Database.Expense_Data;
import com.appsnipp.modernlogin.Database.Income_Data;
import com.appsnipp.modernlogin.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class IncomeReportFragment extends Fragment {

    ListView lv;
    FirebaseListAdapter adapter;
    private TextView total_income_tm, total_income_tw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_incomes_report, container, false);

        lv = view.findViewById(R.id.listView);
        total_income_tm = view.findViewById(R.id.total_income_this_month);
        total_income_tw = view.findViewById(R.id.total_income_this_week);

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("incomes");
        FirebaseListOptions< Income_Data> options = new FirebaseListOptions.Builder< Income_Data>()
                .setLayout(R.layout.income)
                .setLifecycleOwner(IncomeReportFragment.this)
                .setQuery(query, Income_Data.class)
                .build();


        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String te_tm = dataSnapshot.child("total income").getValue().toString();

                    String text_format  = "$ "+te_tm;

                    total_income_tm.setText(text_format);
                    total_income_tw.setText(text_format);
                }catch (NullPointerException e){

                    String txt_format  = "$ 0.00";

                    total_income_tm.setText(txt_format);
                    total_income_tw.setText(txt_format);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView category_name = v.findViewById(R.id.category_income);
                TextView income_amount = v.findViewById(R.id.income_amount);
                TextView income_date = v.findViewById(R.id.income_date);


                Income_Data in = (Income_Data) model;
                category_name.setText(in.getIncome_cat());
                income_amount.setText("+ $ " +in.getIncome());
                income_date.setText(in.getIncome_date());


            }
        };

        lv.setAdapter(adapter);





        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
