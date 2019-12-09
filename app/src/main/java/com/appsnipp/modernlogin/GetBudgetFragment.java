package com.appsnipp.modernlogin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetBudgetFragment extends Fragment {

    private EditText value;
    private Button submit_button;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth fAuth;
    private String get_value_to_string;
    private  float get_value_to_float;

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
        submit_button = view.findViewById(R.id.submit_value);

         //get_value_to_string =value.getText().toString();

         //get_value_to_float = Float.parseFloat(get_value_to_string);



        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_value_to_string =value.getText().toString();

                get_value_to_float = Float.parseFloat(get_value_to_string);

                Fragment fragment = null;

                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(view.getContext());
                        if (acct != null) {

                          DatabaseReference db =  FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                          db.child("budget").setValue(get_value_to_string);
                            Toast.makeText(getActivity(), "Budget updated", Toast.LENGTH_SHORT).show();



                        fragment = new HomeFragment();
                        replaceFragment(fragment);

                    // ...
                }

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
