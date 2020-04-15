package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eruino.ui.mainhelp.MainHelpFragment;

public class mainHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_help_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainHelpFragment.newInstance())
                    .commitNow();
        }
    }
}
