package com.example.eruino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusView;
    private FirebaseAuth mAuth;
    //get's us the root of the database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    // Write a message to the database
    public static final String EXTRA_MESSAGE = "com.example.eruino.MESSAGE";
    DatabaseReference mConditionRef = mRootRef.child("condition");
    DatabaseReference mMessageRef = mRootRef.child("message");
    DatabaseReference mUser1 = mRootRef.child("users/user:1/phone");
    DatabaseReference mUser2 = mRootRef.child("users/user:2/phone");
    //private Button mButton = (Button) findViewById(R.id.sendData);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusView = (TextView)findViewById(R.id.status);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
        //
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
           signOut();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            //signOut();
        }
       // updateUI(mAuth.getCurrentUser());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mAuth.getCurrentUser() != null) {
            signOut();
        }
        //updateUI(mAuth.getCurrentUser());

    }

    /**

    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
 startActivity(intent);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        mMessageRef.setValue(message);
        mUser1.setValue("0524301213");
        mUser2.setValue("0528378203");
        mRootRef.child("members").child("1").setValue("bahaa");
        mRootRef.child("members").child("2").setValue("kobi");
        mRootRef.child("members").child("3").setValue("aseel");
        mRootRef.child("members").child("4").setValue("jimmy");

        intent.putExtra(EXTRA_MESSAGE, message);

    }

    **/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign in succeeded
                updateUI(mAuth.getCurrentUser());
            } else {
                // Sign in failed
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }

    private void startSignIn() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        /*
        // Build FirebaseUI sign in intent. For documentation on this operation and all
        // possible customization see: https://github.com/firebase/firebaseui-android
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);

         */
        updateUI(mAuth.getCurrentUser());

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Signed in
            mStatusView.setText("login success");
            findViewById(R.id.buttonLogin).setVisibility(View.GONE);
            //findViewById(R.id.buttonLogout).setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, navigationDrawer.class);
            startActivity(intent);
        } else {
            // Signed out
            mStatusView.setText(R.string.signed_out);

            findViewById(R.id.buttonLogin).setVisibility(View.VISIBLE);
            //findViewById(R.id.buttonLogout).setVisibility(View.GONE);
        }
    }

    public void signOut() {
        AuthUI.getInstance().signOut(this);
        updateUI(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                startSignIn();
                break;
            //case R.id.buttonLogout:
              //  signOut();
                //break;
        }
    }
}

