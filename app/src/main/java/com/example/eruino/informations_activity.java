package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class informations_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_activity);

        Objects.requireNonNull(getSupportActionBar()).setTitle("informationsActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView information = findViewById(R.id.InformationView);
        information.setText("Welcome to ERUINO Android App \n" +
                "This app controls and monitors your smart home\n" +
                "You will need to use our Arduino kit and place it following the instructions\n" +
                "We hope you enjoy using our app\n" +
                "For any questions please feel free to contact us\n"+
                "ERUINO TEAM\n" +
                "\n");




    }
}