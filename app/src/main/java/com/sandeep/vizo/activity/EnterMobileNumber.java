package com.sandeep.vizo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.sandeep.vizo.R;
import com.sandeep.vizo.utility.VizoApplication;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class EnterMobileNumber extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CountryCodePicker countryCodePicker;
    private EditText inputNumber;

    public VizoApplication getVizoApplication() {
        if (vizoApplication == null) {
            vizoApplication = new VizoApplication();
        }
        return vizoApplication;
    }

    private VizoApplication vizoApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);
        Log.d(TAG, "activity_enter_mobile_number: ");
        initViews();
        FirebaseUser firebaseUser = getVizoApplication().getFirebaseAuth().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userMobileNumber", firebaseUser.getPhoneNumber());
            startActivity(intent);
        }

    }

    private void initViews() {
        Button registerBtn = findViewById(R.id.register_number_btn);
        countryCodePicker = findViewById(R.id.country_code);
        inputNumber = findViewById(R.id.mobile_number);
        countryCodePicker.registerCarrierNumberEditText(inputNumber);

        registerBtn.setOnClickListener(v -> {
            if (countryCodePicker.isValidFullNumber()) {
                Intent intent = new Intent(EnterMobileNumber.this, EnterOtpActivity.class);
                intent.putExtra("phoneNumber", countryCodePicker.getFullNumberWithPlus());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Phone Number Invalid Please Check!!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}