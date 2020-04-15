package com.example.eruino.ui.mainlogout;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.eruino.MainActivity;
import com.example.eruino.R;
import com.example.eruino.navigationDrawer;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainLogoutFragment extends Fragment {

    private MainLogoutViewModel mViewModel;

    public static MainLogoutFragment newInstance() {
        return new MainLogoutFragment();
    }
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.main_logout_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        Button mButtonOut = view.findViewById(R.id.buttonOut);
        mButtonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

            }
        });

        return view;
    }






    public void signOut() {
        //
        AuthUI.getInstance().signOut(getContext());
        startActivity(new Intent(getContext(),MainActivity.class));

       // getContext();
    }

}
