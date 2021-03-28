package com.appsnipp.modernlogin.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsnipp.modernlogin.R;

public class FragmentSetBusget extends Fragment {

    private EditText budget  ;
    private Button submit_budget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_set_budget, container, false);
        budget = view.findViewById(R.id.budget);
        submit_budget= view.findViewById(R.id.submit_value);

        budget.addTextChangedListener(budgetTextWatcher);

        return  view;
    }

    private TextWatcher budgetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String budgetInput = budget.getText().toString().trim();

           submit_budget.setEnabled(!budgetInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
