package com.appsnipp.modernlogin.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsnipp.modernlogin.Activities.MainActivity;
import com.appsnipp.modernlogin.Database.Expense_Data;
import com.appsnipp.modernlogin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class FragmentExpense extends Fragment implements AdapterView.OnItemSelectedListener {


    private EditText category;

    private Calendar calendar = Calendar.getInstance();
    private String expense_date =  DateFormat.getDateInstance().format(calendar.getTime());
    private Button submit_expense, date, save;
    private EditText expense;
    FirebaseAuth fAuth;
    Expense_Data ed;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        Spinner spinner = view.findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_categoires, android.R.layout.simple_spinner_item );
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        Spinner spinner_b = view.findViewById(R.id.budget);
        ArrayAdapter<CharSequence> adapter_b = ArrayAdapter.createFromResource(getContext(), R.array.budgets, android.R.layout.simple_spinner_item );
        adapter_b.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_b.setAdapter(adapter_b);
        spinner_b.setOnItemSelectedListener(this);

       expense = view.findViewById(R.id.expense);
       date = view.findViewById(R.id.date);
       save= view.findViewById(R.id.save);
       date.setText(expense_date);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Calendar c = Calendar.getInstance();
               int year = c.get(Calendar.YEAR);
               int month = c.get(Calendar.MONTH);
               int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
               DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                       new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    date.setText(dayOfMonth +"/"+ (month+1)+"/"+year);
                   }
               },year,month,dayOfMonth);
               dpd.show();
            }
        });

        submit_expense = view.findViewById(R.id.submit_expense);

        db = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submit_expense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = null;
                        try {
                                try{
                            String str_budget = spinner_b.getSelectedItem().toString();
                            String str_cat = spinner.getSelectedItem().toString();
                            String str_expense = expense.getText().toString();


                            long expense_nr = (dataSnapshot.child("expenses").getChildrenCount()) + 1;
                            String date_str = date.getText().toString().trim();
                            ed = new Expense_Data();
                            ed.setCategory(str_cat);
                            ed.setExpense(str_expense);
                            ed.setExpense_nr(Long.toString(expense_nr));
                            ed.setExpense_date(date_str);

                                    double expense_double = Double.parseDouble(str_expense);
                            db.child("expenses").child(Long.toString(expense_nr)).setValue(ed);
                            String budget = dataSnapshot.child("budget progress").getValue().toString();
                            double budget_double = Double.parseDouble(budget);

                            String total_expense_str  = dataSnapshot.child("total expense").getValue().toString();
                            double total_expense = Double.parseDouble(total_expense_str) + expense_double;

                            budget_double = budget_double - expense_double;

                            db.child("budget progress").setValue(Double.toString(budget_double));
                            db.child("total expense").setValue(Double.toString(total_expense));
                            if(str_budget.equals("Needs")){

                                String str_actual_budget = dataSnapshot.child("budgets").child("needs").child("actual_budget").getValue().toString();
                                    float actual_budget = Float.parseFloat(str_actual_budget) - ((float) expense_double);
                                    db.child("budgets").child("needs").child("actual_budget").setValue(actual_budget);

                            }
                            else if (str_budget.equals("Wants")){

                                        String str_actual_budget = dataSnapshot.child("budgets").child("wants").child("actual_budget").getValue().toString();
                                        float actual_budget = Float.parseFloat(str_actual_budget) - ((float) expense_double);
                                        db.child("budgets").child("wants").child("actual_budget").setValue(actual_budget);

                                    }
                            else if (str_budget.equals("Savings")){

                                String str_actual_budget = dataSnapshot.child("budgets").child("savings").child("actual_budget").getValue().toString();
                                float actual_budget = Float.parseFloat(str_actual_budget) - ((float) expense_double);
                                db.child("budgets").child("savings").child("actual_budget").setValue(actual_budget);

                            }


                                    LoadNextActivity();
                                }catch (NumberFormatException a){
                                    Toast.makeText(getActivity(), "Please enter  the expense amount", Toast.LENGTH_SHORT).show();

                                }
                        } catch (NullPointerException e){

                            String str_cat = spinner.getSelectedItem().toString();
                            String str_expense = expense.getText().toString();

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            long expense_nr =  1;
                            String date_str = date.getText().toString().trim();
                            ed = new Expense_Data();
                            ed.setCategory(str_cat);
                            ed.setExpense(str_expense);
                            ed.setExpense_nr(Long.toString(expense_nr));
                            ed.setExpense_date(date_str);
                            double expense_double = Double.parseDouble(str_expense);

                            db.child("expenses").child(Long.toString(expense_nr)).setValue(ed);
                            String budget = dataSnapshot.child("budget progress").getValue().toString();
                            double budget_double = Double.parseDouble(budget);


                            budget_double = budget_double - expense_double;

                            db.child("budget progress").setValue(Double.toString(budget_double));


                            db.child("total expense").setValue(Double.toString(expense_double));
                            LoadNextActivity();



                        }


                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = null;
                        try {

                            try {
                                String str_cat = spinner.getSelectedItem().toString();
                                String str_expense = expense.getText().toString();


                                    long expense_nr = (dataSnapshot.child("expenses").getChildrenCount()) + 1;
                                    String date_str = date.getText().toString().trim();
                                    ed = new Expense_Data();
                                    ed.setCategory(str_cat);
                                    ed.setExpense(str_expense);
                                    ed.setExpense_nr(Long.toString(expense_nr));
                                    ed.setExpense_date(date_str);
                                    double expense_double = Double.parseDouble(str_expense);

                                    db.child("expenses").child(Long.toString(expense_nr)).setValue(ed);
                                    String budget = dataSnapshot.child("budget progress").getValue().toString();
                                    double budget_double = Double.parseDouble(budget);

                                    String total_expense_str = dataSnapshot.child("total expense").getValue().toString();
                                    double total_expense = Double.parseDouble(total_expense_str) + expense_double;

                                    budget_double = budget_double - expense_double;

                                    db.child("budget progress").setValue(Double.toString(budget_double));
                                    db.child("total expense").setValue(Double.toString(total_expense));
                                LoadNextActivity();

                            } catch (NumberFormatException a){
                                Toast.makeText(getActivity(), "Please enter  the expense amount", Toast.LENGTH_SHORT).show();
                            }
                        }

                            catch (NullPointerException e){

                           String str_cat = spinner.getSelectedItem().toString();
                            String str_expense = expense.getText().toString();

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            long expense_nr =  1;
                            String date_str = date.getText().toString().trim();
                            ed = new Expense_Data();
                            ed.setCategory(str_cat);
                            ed.setExpense(str_expense);
                            ed.setExpense_nr(Long.toString(expense_nr));
                            ed.setExpense_date(date_str);


                            db.child("expenses").child(Long.toString(expense_nr)).setValue(ed);
                            String budget = dataSnapshot.child("budget progress").getValue().toString();
                            double budget_double = Double.parseDouble(budget);
                            double expense_double = Double.parseDouble(str_expense);

                            budget_double = budget_double - expense_double;

                            db.child("budget progress").setValue(Double.toString(budget_double));
                            Toast.makeText(getActivity(), "Please enter  the expense amount", Toast.LENGTH_SHORT).show();

                         db.child("total expense").setValue(Double.toString(expense_double));

                                LoadNextActivity();

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


    private void LoadNextActivity(){

        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
