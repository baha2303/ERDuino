package com.example.eruino.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.eruino.R;
import com.example.eruino.SensorsActivity;
import com.example.eruino.tipsActivity;
import com.example.eruino.informations_activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference windowRef = myRef.child("Windows");
    DatabaseReference travelRef = myRef.child("travelMode");
    DatabaseReference ventsRef  =  myRef.child("ventsMode");



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });

        final Switch travelMode = root.findViewById(R.id.travelMode);
        final Switch ventsMode = root.findViewById(R.id.ventsMode);
        final Switch windowsMode = root.findViewById(R.id.windowSwitch);
        Button tipButton = root.findViewById(R.id.tipsButton);
        Button infoButton = root.findViewById(R.id.infoButton);
        Button sensorsButton = root.findViewById(R.id.sensorsButton);


        travelMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference childRef = myRef.child("travelMode");
                if (isChecked){
                    childRef.setValue("ON");
                }
                else {
                    childRef.setValue("OFF");
                }
            }
        });

        ventsMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference childRef = myRef.child("ventsMode");
                if (isChecked){
                    childRef.setValue("ON");
                }
                else {
                    childRef.setValue("OFF");
                }
            }
        });

        windowsMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference childRef = myRef.child("Windows");
                if (isChecked){
                    childRef.setValue("OPEN");
                }
                else {
                    childRef.setValue("CLOSED");
                }
            }
        });


        sensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SensorsActivity.class);
                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), informations_activity.class);
                startActivity(intent);
            }
        });

        tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), tipsActivity.class);
                startActivity(intent);
            }
        });

        windowRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String windows_str =  dataSnapshot.getValue().toString();
                if(windows_str.equals("OPEN"))
                    windowsMode.setChecked(true);
                else {
                    windowsMode.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });

        ventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str =  dataSnapshot.getValue().toString();
                if(str.equals("ON"))
                    ventsMode.setChecked(true);
                else {
                    ventsMode.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        travelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str =  dataSnapshot.getValue().toString();
                if(str.equals("ON"))
                    travelMode.setChecked(true);
                else {
                    travelMode.setChecked(false);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }


}
