package com.appsnipp.modernlogin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsnipp.modernlogin.Database.Expense_Data;
import com.appsnipp.modernlogin.Database.Income_Data;
import com.appsnipp.modernlogin.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class GetBudgetFragment extends Fragment {
    int count = 0;
    Button dropdown, show;
    CardView calculator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_budget, container, false);

        dropdown = view.findViewById(R.id.dropdown);
        calculator = view.findViewById(R.id.cal);
        show = view.findViewById(R.id.show);
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    calculator.setVisibility(View.GONE);
                    dropdown.setVisibility(View.GONE);
                    show.setVisibility(View.VISIBLE);




            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculator.setVisibility(View.VISIBLE);
                dropdown.setVisibility(View.VISIBLE);
                show.setVisibility(View.GONE);
            }
        });





        return view;
    }




    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
