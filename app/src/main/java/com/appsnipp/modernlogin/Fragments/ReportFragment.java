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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReportFragment extends Fragment {

    ListView  lv;
    FirebaseListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_sales_report, container, false);

        lv = view.findViewById(R.id.listView);

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("expenses");
        FirebaseListOptions<Expense_Data> options = new FirebaseListOptions.Builder<Expense_Data>()
                .setLayout(R.layout.expense)
                .setLifecycleOwner(ReportFragment.this)
                .setQuery(query, Expense_Data.class)
                .build();

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
