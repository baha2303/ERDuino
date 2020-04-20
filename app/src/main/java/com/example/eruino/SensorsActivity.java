package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SensorsActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        TextView sensors_view = findViewById(R.id.SensorsView);
        sensors_view.setText("Sensors measurements below: ");
        }
        }
