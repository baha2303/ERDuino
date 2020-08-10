package com.example.eruino.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.eruino.R;
import com.example.eruino.SensorsActivity;
import com.example.eruino.contactsActivity;
import com.example.eruino.tipsActivity;
import com.example.eruino.informations_activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String CHANNEL_ID = "777";
    private static final String CHANNEL_NAME = "WARNING";
    private static final String CHANNEL_DESCRIPTION = "Android Notification";
    private TextView textView;

    private static final int REQUEST_CALL = 1;

//    private void displayNotification() {
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(),CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_warning_black_24dp)
//                .setContentTitle("Warning")
//                .setContentText("GAS LEVEL IS TO HIGH IN YOUR HOUSE")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(getContext());
//        mNotificationMgr.notify(1,mBuilder.build());
//    }






    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference water = mRootRef.child("sensors/waterSensor");
    DatabaseReference gas = mRootRef.child("sensors/gasSensor");
    DatabaseReference flame  =  mRootRef.child("sensors/flameSensor");
    DatabaseReference motion  =  mRootRef.child("sensors/motionSensor");
    DatabaseReference windowRef = mRootRef.child("gear/windows");
    DatabaseReference liftRef = mRootRef.child("gear/lift");
    DatabaseReference travelRef = mRootRef.child("gear/travelMode");
    DatabaseReference ventsRef  =  mRootRef.child("gear/fan");


    boolean waterNotif = false;
    boolean gasNotif = false;
    boolean motionNotif = false;
    boolean fireNotif = false;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(getContext(),"Permission DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void makePhoneCall() {
        String number = "0522200237";
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;

            Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse(dial));
            startActivity(callIntent);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        createNotificationChannel();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        createNotificationChannel();
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        createNotificationChannel();
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
        final Switch liftMode = root.findViewById(R.id.liftSwitch);
        Button tipButton = root.findViewById(R.id.tipsButton);
        Button infoButton = root.findViewById(R.id.infoButton);
        Button sensorsButton = root.findViewById(R.id.sensorsButton);
        Button contacts = root.findViewById(R.id.contactsButton);
        Button erButton = root.findViewById(R.id.erButton);
        final TextView waterSensor  =  root.findViewById(R.id.textView3);
        final TextView flameSensor  =  root.findViewById(R.id.textView8);
        final TextView gasSensor    =  root.findViewById(R.id.textView7);
        final TextView motionSensor =  root.findViewById(R.id.textView9);



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription(CHANNEL_DESCRIPTION);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            //notificationManager.createNotificationChannel(channel);
//        }
//
//        textView=root.findViewById(R.id.textView2);
//
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if(task.isSuccessful()) {
//
//                            String token = task.getResult().getToken();
//                            textView.setText("Token "+token);
//
//                        } else  {
//                            textView.setText("Token not generated");
//
//                        }
//                    }
//                });





        travelMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference childRef = mRootRef.child("gear/travelMode");
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
                DatabaseReference childRef = mRootRef.child("gear/fan");
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
                DatabaseReference childRef = mRootRef.child("gear/windows");
                if (isChecked){
                    childRef.setValue("OPEN");
                }
                else {
                    childRef.setValue("CLOSED");
                }
            }
        });
        liftMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference childRef = mRootRef.child("gear/lift");
                if (isChecked){
                    childRef.setValue("UP");
                }
                else {
                    childRef.setValue("DOWN");
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

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), contactsActivity.class);
                startActivity(intent);
            }
        });

        erButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), contactsActivity.class);
                //startActivity(intent);



                makePhoneCall();


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
        liftRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String lift_str =  dataSnapshot.getValue().toString();
                if(lift_str.equals("UP")) {
                    liftMode.setChecked(true);
                    liftMode.setClickable(true);
                }
                else {
                    liftMode.setChecked(false);
                    liftMode.setClickable(false);
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

        water.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //createNotificationChannel();

                //waterSensor.setText(dataSnapshot.getValue().toString());
                int water_val = Integer.parseInt(dataSnapshot.getValue().toString());
                if (water_val < 2500) {
                    if(!waterNotif) {
                        waterNotif = true;
                        createNotificationChannel();
                        fullScreenNotification("WATER LEVEL GETTING HIGH");
                    }
                } else {
                    waterNotif=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        flame.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                createNotificationChannel();
                //flameSensor.setText(dataSnapshot.getValue().toString());
                int flame_val = Integer.parseInt(dataSnapshot.getValue().toString());
                if(flame_val<2000) {
                    if (!fireNotif) {
                        fireNotif = true;
                        createNotificationChannel();
                        fullScreenNotification("FIRE IN THE HOUSE");
                    }
                } else {
                        fireNotif=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String[] travelModeValue = new String[1];
        travelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                travelModeValue[0] = dataSnapshot.getValue().toString();
                Log.d("BAHAA",travelModeValue[0]);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        motion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                createNotificationChannel();
                //motionSensor.setText(dataSnapshot.getValue().toString());
                int motion_val = Integer.parseInt(dataSnapshot.getValue().toString());
                if(motion_val> 1000 && travelModeValue[0] =="ON") {
                    if (!motionNotif) {
                        motionNotif = true;
                        createNotificationChannel();
                        fullScreenNotification("MOTION DETECTED AT YOUR HOUSE");
                    }
                } else {
                        motionNotif=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                createNotificationChannel();
                //gasSensor.setText(dataSnapshot.getValue().toString());
                int gas_val = Integer.parseInt(dataSnapshot.getValue().toString());
                if(gas_val> 900) {
                    if (!gasNotif) {
                        gasNotif = true;
                        createNotificationChannel();
                        fullScreenNotification("GAS LEVEL IS HIGH");
                    }
                } else {
                    gasNotif=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }


    private void fullScreenNotification(String msg) {
        if (getActivity()!=null) {
            Intent fullScreenIntent = new Intent(getActivity().getApplicationContext(), tipsActivity.class);
            fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getContext(), 0,
                    fullScreenIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_warning_black_24dp)
                    .setContentTitle("WARNING")
                    .setContentText(msg)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setContentIntent(fullScreenPendingIntent);
            NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(getContext());
            mNotificationMgr.notify(115,builder.build());
        }




    }
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager;
            if (getActivity()!=null) {
                notificationManager = getContext().getSystemService(NotificationManager.class);
                //NotificationManager notificationManager1 = getActivity().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

        }
    }



}
