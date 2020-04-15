package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eruino.ui.mainlogout.MainLogoutFragment;

public class mainLogout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_logout_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainLogoutFragment.newInstance())
                    .commitNow();
        }
    }
}
