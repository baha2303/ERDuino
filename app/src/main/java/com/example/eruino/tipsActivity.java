package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.eruino.ui.home.HomeFragment;

public class tipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        getSupportActionBar().setTitle("Tips");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tips = findViewById(R.id.tipsView);
        tips.setText("Here are some tips for your safety: \n\n\n " +
                "\t\u25CF\tUse fire resistant building materials.\n\n " +
                "\t\u25CF\tDouse cigarette and cigar butts with water before \n\t\t throwing away.\n\n" +
                "\t\u25CF\tNever leave the stove or other fire dangers\n \t\tunattended.\n\n " +
                "\t\u25CF\tClear and dispose of dry or dead vegetation.\n\n " +
                "\t\u25CF\tKeep fire hazards stored away.\n\n " +
                "\t\u25CF\tMake a fire escape plan for your family.\n\n " +
                "\t\u25CF\tBe sure your street address is visibly posted so \n\t\t that firefighters can identify your home in the\n\t\t event of an emergency.\n\n" +
                "\t\u25CF\tDisplay your address on your mailbox and home\n\t\t to help first responders find you quickly.\n\n ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}