package com.sandeep.vizo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sandeep.vizo.R;
import com.sandeep.vizo.utility.VizoApplication;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Button logOutButton;

    public VizoApplication getVizoApplication() {
        if(vizoApplication == null) {
            vizoApplication = new VizoApplication();
        }
        return vizoApplication;
    }

    private VizoApplication vizoApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logOutButton = findViewById(R.id.logOutBtn);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVizoApplication().getFirebaseAuth().signOut();
                Intent intent = new Intent(getApplicationContext(),EnterMobileNumber.class);
                startActivity(intent);

            }
        });

        Log.d(TAG, "onCreate: " + getIntent().getStringExtra("userMobileNumber"));
    }
}