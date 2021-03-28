package com.appsnipp.modernlogin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.modernlogin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivitySetBudget extends AppCompatActivity {

    Button setBudget;
    EditText budget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_budget);

        setBudget = findViewById(R.id.getStarted);
        budget = findViewById(R.id.setBudget);


        budget.addTextChangedListener(budgetTextWatcher);

        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                String budget_str = budget.getText().toString().trim();

                db.child("budget").setValue(budget_str);

            }
        });

    }

    private TextWatcher budgetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String budget_str = budget.getText().toString().trim();

            setBudget.setEnabled(!budget_str.isEmpty());
            if(!budget_str.isEmpty()){
                setBudget.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void LoadNextActivity(){

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
