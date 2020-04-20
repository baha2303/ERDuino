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
        information.setText("Please, read the information below to use our app: \n");
    }
}