package com.appsnipp.modernlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    private String username;
    private  String currency= "$";
    private String welcome_name;
    CustomProgressBar cpb;
    TextView text;
    private int ammount  = 80000;
    Button open_rtf,ammount_txt;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        cpb = view.findViewById(R.id.pb);
        username = "aldo";
        welcome_name = "Welcome "+ username + "!";
        String its = Integer.toString(ammount) + currency;

        open_rtf = view.findViewById(R.id.rtf);
        ammount_txt = view.findViewById(R.id.ammount);
        ammount_txt.setText(its);
        text = view.findViewById(R.id.textViewUser);
        text.setText(welcome_name);
        cpb.setMax(100000);
        cpb.setProgress(ammount);

        open_rtf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                switch (v.getId()) {
                    // ...
                    case R.id.rtf:
                       fragment = new ReportFragment();
                       replaceFragment(fragment);

                        break;
                    // ...
                }
            }
        });

        return  view;
        // return inflater.inflate(R.layout.content_main, container, false);


    }



    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}
