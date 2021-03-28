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
import com.appsnipp.modernlogin.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ReportFragment extends Fragment {

    ListView  lv;
    FirebaseListAdapter adapter;
    private TextView total_expense_tm, total_expense_tw;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_sales_report, container, false);

        lv = view.findViewById(R.id.listView);
        total_expense_tm = view.findViewById(R.id.total_expense_this_month);
        total_expense_tw = view.findViewById(R.id.total_expense_this_week);

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("expenses");
        FirebaseListOptions<Expense_Data> options = new FirebaseListOptions.Builder<Expense_Data>()
                .setLayout(R.layout.expense)
                .setLifecycleOwner(ReportFragment.this)
                .setQuery(query, Expense_Data.class)
                .build();


        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String te_tm = dataSnapshot.child("total expense").getValue().toString();

                    String text_format  = "$ "+te_tm;

                    total_expense_tm.setText(text_format);
                    total_expense_tw.setText(text_format);
                }catch (NullPointerException e){

                    String txt_format  = "0";

                    total_expense_tm.setText(txt_format);
                    total_expense_tw.setText(txt_format);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView category_name = v.findViewById(R.id.category_name);
                TextView expense_amount = v.findViewById(R.id.expense_amount);
                TextView expense_date = v.findViewById(R.id.expense_date);


                Expense_Data ed = (Expense_Data)model;
                category_name.setText(ed.getCategory());
                expense_amount.setText("- $ " +ed.getExpense());
                expense_date.setText(ed.getExpense_date());


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
