package com.example.eruino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.eruino.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorsActivity extends AppCompatActivity {


        private HomeViewModel homeViewModel;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference water = myRef.child("sensors/waterSensor");
        DatabaseReference gas = myRef.child("sensors/gasSensor");
        DatabaseReference flame  =  myRef.child("sensors/flameSensor");
        DatabaseReference motion  =  myRef.child("sensors/motionSensor");

        private static final String CHANNEL_ID = "notif";
        private static final String CHANNEL_NAME = "notif";
        private static final String CHANNEL_DESCRIPTION = "Android Notification";
        private TextView textView;

//
//        private void displayNotification() {
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_warning_black_24dp)
//                        .setContentTitle("Warning")
//                        .setContentText("WATER LEVEL IS TO HIGH IN YOUR HOUSE")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//                NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(getApplicationContext());
//                mNotificationMgr.notify(1,mBuilder.build());
//        }
//
//        private void fullScreenNotification(String msg) {
//                Intent fullScreenIntent = new Intent(getApplicationContext(), tipsActivity.class);
//
//                PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getApplicationContext(), 113,
//                        fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_warning_black_24dp)
//                        .setContentTitle("WARNING")
//                        .setContentText(msg)
//                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setContentIntent(fullScreenPendingIntent);
//
//
//                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                manager.notify(115, builder.build());
//
//                //NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(getApplicationContext());
//               // mNotificationMgr.notify(115,builder.build());
//        }


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        TextView sensors_view = findViewById(R.id.SensorsView);
        sensors_view.setText("Sensors measurements below: ");

        final TextView waterSensor = findViewById(R.id.textView3);
        final TextView flameSensor = findViewById(R.id.textView8);
        final TextView gasSensor = findViewById(R.id.textView7);
        final TextView motionSensor = findViewById(R.id.textView9);

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mUser1 = myRef.child("users/"+currentuser+"/phone");
        mUser1.setValue("ASEEL");
        water.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        waterSensor.setText(dataSnapshot.getValue().toString());
                        int water_val = Integer.parseInt(dataSnapshot.getValue().toString());
                        if (water_val < 3500) {
                             //   fullScreenNotification("WATER LEVEL GETTING HIGH");
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });

       flame.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       flameSensor.setText(dataSnapshot.getValue().toString());
                       int flame_val = Integer.parseInt(dataSnapshot.getValue().toString());
                       if(flame_val<2000) {
                             //  fullScreenNotification("FIRE IN THE HOUSE");
                       }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
       });
        motion.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        motionSensor.setText(dataSnapshot.getValue().toString());
                        int motion_val = Integer.parseInt(dataSnapshot.getValue().toString());
                        if(motion_val> 1000) {
                               // fullScreenNotification("MOTION DETECTED AT YOUR HOUSE");
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });
        gas.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        gasSensor.setText(dataSnapshot.getValue().toString());
                        int gas_val = Integer.parseInt(dataSnapshot.getValue().toString());
                        if(gas_val> 2000) {
                             //   fullScreenNotification("GAS LEVEL IS HIGH");
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });


}
        }
