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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsnipp.modernlogin.Activities.ActivityEmailSent;
import com.appsnipp.modernlogin.Activities.MainActivity;
import com.appsnipp.modernlogin.Database.Income_Data;
import com.appsnipp.modernlogin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class FragmentIncome extends Fragment implements AdapterView.OnItemSelectedListener{


    private Calendar calendar = Calendar.getInstance();
    private String date =  DateFormat.getDateInstance().format(calendar.getTime());

    private EditText income;
    private Button submit_income,income_date, save;
    Income_Data id;

    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);


        Spinner spinner = view.findViewById(R.id.category_income);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.income_categoires, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        income = view.findViewById(R.id.income);
        income_date= view.findViewById(R.id.date);
        save = view.findViewById(R.id.save);
        submit_income = view.findViewById(R.id.submit_income);
        income_date.setText(date);

        db = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        income_date.setOnClickListener(new View.OnClickListener() {
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
                                    income_date.setText(dayOfMonth +"/"+ (month+1)+"/"+year);
                                }
                            },year,month,dayOfMonth);
                    dpd.show();
            }
        });






            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    submit_income.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Fragment fragment = null;

                            try {
                                try {


                                    String str_cat_income = spinner.getSelectedItem().toString();
                                    String str_income = income.getText().toString();
                                        if(str_income.isEmpty()){
                                            Toast.makeText(getActivity(), "Please enter the income amount", Toast.LENGTH_SHORT).show();
                                        }else {
                                            long income_nr = (dataSnapshot.child("incomes").getChildrenCount()) + 1;
                                            String income_date_str = income_date.getText().toString().trim();

                                            id = new Income_Data();
                                            id.setIncome_cat(str_cat_income);
                                            id.setIncome(str_income);
                                            id.setIncome_nr(Long.toString(income_nr));
                                            id.setIncome_date(income_date_str);

                                            db.child("incomes").child(Long.toString(income_nr)).setValue(id);
                                            LoadNextActivity();
                                        }

                                }catch (NumberFormatException a){
                                    Toast.makeText(getActivity(), "Please enter the income amount", Toast.LENGTH_SHORT).show();

                                }
                            }
                            catch (NullPointerException e) {

                              String str_cat_income =  spinner.getSelectedItem().toString();
                                String str_income = income.getText().toString();

                                long income_nr = 1;
                                String income_date_str = income_date.getText().toString().trim();
                                id = new Income_Data();
                                id.setIncome_cat(str_cat_income);
                                id.setIncome(str_income);
                                id.setIncome_nr(Long.toString(income_nr));
                                id.setIncome_date(income_date_str);

                                db.child("incomes").child(Long.toString(income_nr)).setValue(id);
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

                                        String str_cat_income = spinner.getSelectedItem().toString();
                                        String str_income = income.getText().toString();
                                        if(str_income.isEmpty()){
                                            Toast.makeText(getActivity(), "Please enter the income amount", Toast.LENGTH_SHORT).show();
                                        }else {
                                            long income_nr = (dataSnapshot.child("incomes").getChildrenCount()) + 1;
                                            String income_date_str = income_date.getText().toString().trim();

                                            id = new Income_Data();
                                            id.setIncome_cat(str_cat_income);
                                            id.setIncome(str_income);
                                            id.setIncome_nr(Long.toString(income_nr));
                                            id.setIncome_date(income_date_str);

                                            db.child("incomes").child(Long.toString(income_nr)).setValue(id);
                                            LoadNextActivity();
                                        }
                                    } catch (NumberFormatException a){
                                        Toast.makeText(getActivity(), "Please enter the income amount", Toast.LENGTH_SHORT).show();

                                    }
                            }catch (NullPointerException e) {

                                String str_cat_income =  spinner.getSelectedItem().toString();
                                String str_income = income.getText().toString();

                                long income_nr = 1;
                                String income_date_str = income_date.getText().toString().trim();
                                id = new Income_Data();
                                id.setIncome_cat(str_cat_income);
                                id.setIncome(str_income);
                                id.setIncome_nr(Long.toString(income_nr));
                                id.setIncome_date(income_date_str);

                                db.child("incomes").child(Long.toString(income_nr)).setValue(id);
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
