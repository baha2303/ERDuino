package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TextView;

public class tipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        getSupportActionBar().setTitle("tipsActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tips = findViewById(R.id.tipsView);
        tips.setText("Here are some tips for your safety: \n");
    }
}