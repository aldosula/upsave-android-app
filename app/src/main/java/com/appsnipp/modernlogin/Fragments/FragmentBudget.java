package com.appsnipp.modernlogin.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsnipp.modernlogin.Activities.MainActivity;
import com.appsnipp.modernlogin.Database.Budget_Data;
import com.appsnipp.modernlogin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentBudget extends Fragment {
    private EditText value;
    private Button submit_button, date, save, categoies;
    private String get_value_to_string="";
    private String [] listItems;
    private boolean [] checkedItems;
    private
    ArrayList<Integer> mUserItems = new ArrayList<>();
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        value = view.findViewById(R.id.budget);
        submit_button = view.findViewById(R.id.submit_value);
        date = view.findViewById(R.id.date);
        save = view.findViewById(R.id.save);
      //  categoies = view.findViewById(R.id.categories);
        listItems = getResources().getStringArray(R.array.expense_categoires);
        checkedItems = new boolean[listItems.length];


        db = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());





        //start category button
        /*categoies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Budgets available");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                        if(isChecked){
                            if(!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }else{
                                mUserItems.remove(position);
                            }
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for(int i = 0; i<mUserItems.size();i++){

                            item = item + listItems[mUserItems.get(i)];
                            if(i !=mUserItems.size() -1){
                                item = item+ ",";

                            }
                        }
                        categoies.setText(item);
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                   for (int i=0; i<checkedItems.length;i++){
                       checkedItems[i]= false;
                       mUserItems.clear();
                       categoies.setText("No budgets selected");
                   }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();


            }
        });
*/
        //end category button

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



       // String cat = categoies.getText().toString().toLowerCase().trim();
       String date_str = date.getText().toString();


       DatabaseReference db = FirebaseDatabase.getInstance().getReference()
               .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        get_value_to_string = value.getText().toString();


                        if (!get_value_to_string.isEmpty() ||!date_str.equals("Set Date") ) {

                            double value_d = Double.parseDouble(get_value_to_string);
                                        Budget_Data bd = new Budget_Data();

                                        bd.setNeeds(value_d*0.5);
                                        bd.setWants(value_d*0.3);
                                        bd.setSavings(value_d*0.2);
                                        db.child("budgets").child("savings").child("Name").setValue("Savings");
                                        db.child("budgets").child("savings").child("initial_budget").setValue( bd.getSavings());
                                        db.child("budgets").child("savings").child("actual_budget").setValue( bd.getSavings());
                                        db.child("budgets").child("savings").child("end_date").setValue(date_str);


                            db.child("budgets").child("needs").child("Name").setValue("Needs");
                            db.child("budgets").child("needs").child("initial_budget").setValue( bd.getNeeds());
                            db.child("budgets").child("needs").child("actual_budget").setValue( bd.getNeeds());
                            db.child("budgets").child("needs").child("end_date").setValue(date_str);


                            db.child("budgets").child("wants").child("Name").setValue("Wants");
                            db.child("budgets").child("wants").child("initial_budget").setValue( bd.getWants());
                            db.child("budgets").child("wants").child("actual_budget").setValue( bd.getWants());
                            db.child("budgets").child("wants").child("end_date").setValue(date_str);



                            db.child("budget").setValue(get_value_to_string);
                            db.child("budget progress").setValue(get_value_to_string);

                            LoadNextActivity();
                        }
                        else {
                            Toast.makeText(getActivity(), "Error: A field is not filled properly", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });



        return view;
    }


    private void LoadNextActivity(){

        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
