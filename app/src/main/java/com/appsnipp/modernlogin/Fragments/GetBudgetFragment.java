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

    private EditText value;
    private EditText category;
    private EditText category_income;
    private EditText income;
    private Calendar calendar = Calendar.getInstance();
    private String expense_date =  DateFormat.getDateInstance().format(calendar.getTime());;
    private Button submit_button;
    private Button submit_expense;
    private Button submit_income;
    private EditText expense;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth fAuth;
    private String get_value_to_string;
    private  float get_value_to_float;
    float total;

    Expense_Data ed;
    Income_Data id;

    DatabaseReference databaseReference;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_send_values, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        value = view.findViewById(R.id.budget);
        expense = view.findViewById(R.id.expense);
        category = view.findViewById(R.id.category);
        category_income = view.findViewById(R.id.category_income);
        income = view.findViewById(R.id.income);
        submit_button = view.findViewById(R.id.submit_value);
        submit_expense = view.findViewById(R.id.submit_expense);
        submit_income = view.findViewById(R.id.submit_income);



         //get_value_to_string =value.getText().toString();

         //get_value_to_float = Float.parseFloat(get_value_to_string);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                submit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_value_to_string =value.getText().toString();

                        get_value_to_float = Float.parseFloat(get_value_to_string);

                        Fragment fragment = null;



                        DatabaseReference db =  FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        db.child("budget").setValue(get_value_to_string);
                        db.child("budget progress").setValue(get_value_to_string);
                        Toast.makeText(getActivity(), "Budget updated", Toast.LENGTH_SHORT).show();



                        fragment = new HomeFragment();
                        replaceFragment(fragment);

                        // ...
                    }


                });

                submit_income.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            String str_cat_income = category_income.getText().toString();
                            String str_income = income.getText().toString();

                            long income_nr = (dataSnapshot.child("incomes").getChildrenCount()) + 1;

                            id = new Income_Data();
                            id.setIncome_cat(str_cat_income);
                            id.setIncome(str_income);
                            id.setIncome_nr(Long.toString(income_nr));
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            db.child("incomes").child(Long.toString(income_nr)).setValue(id);

                        }catch (NullPointerException e) {

                            String str_cat_income = category_income.getText().toString();
                            String str_income = income.getText().toString();

                            long income_nr = 1;

                            id = new Income_Data();
                            id.setIncome_cat(str_cat_income);
                            id.setIncome(str_income);
                            id.setIncome_nr(Long.toString(income_nr));
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            db.child("incomes").child(Long.toString(income_nr)).setValue(id);

                        }



                    }
                });



                submit_expense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            String str_cat = category.getText().toString();
                            String str_expense = expense.getText().toString();

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            long expense_nr = (dataSnapshot.child("expenses").getChildrenCount()) + 1;

                            ed = new Expense_Data();
                            ed.setCategory(str_cat);
                            ed.setExpense(str_expense);
                            ed.setExpense_nr(Long.toString(expense_nr));
                            ed.setExpense_date(expense_date);


                            db.child("expenses").child(Long.toString(expense_nr)).setValue(ed);
                            String budget = dataSnapshot.child("budget progress").getValue().toString();
                            double budget_double = Double.parseDouble(budget);
                            double expense_double = Double.parseDouble(str_expense);

                            budget_double = budget_double - expense_double;

                            db.child("budget progress").setValue(Double.toString(budget_double));


                        } catch (NullPointerException e){

                            String str_cat = category.getText().toString();
                            String str_expense = expense.getText().toString();

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            long expense_nr =  1;

                            ed = new Expense_Data();
                            ed.setCategory(str_cat);
                            ed.setExpense(str_expense);
                            ed.setExpense_nr(Long.toString(expense_nr));
                            ed.setExpense_date(expense_date);


                            db.child("expenses").child(Long.toString(expense_nr)).setValue(ed);
                            String budget = dataSnapshot.child("budget progress").getValue().toString();
                            double budget_double = Double.parseDouble(budget);
                            double expense_double = Double.parseDouble(str_expense);

                            budget_double = budget_double - expense_double;

                            db.child("budget progress").setValue(Double.toString(budget_double));



                        }


                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
