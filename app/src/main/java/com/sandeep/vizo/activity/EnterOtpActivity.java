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
import com.sandeep.vizo.R;
import com.sandeep.vizo.utility.VizoApplication;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class EnterOtpActivity extends AppCompatActivity {

    private Button submitBtn;
    private String id = "";
    private static final String TAG = "EnterOtpActivity";

    private VizoApplication vizoApplication;
    private EditText otpText;
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Log.d(TAG, "onCreate: " + phoneNumber);
        initViews();
        generateOtp(phoneNumber);

    }

    private void checkIfUserAlreadyLoggedIn() {

        FirebaseUser firebaseUser = getVizoApplication().getFirebaseAuth().getCurrentUser();
        if (firebaseUser != null) {
            Log.d(TAG, "firebase user already logged in" + firebaseUser.getPhoneNumber());
            startActivity(new Intent(EnterOtpActivity.this, HomeActivity.class));

        }

    }

    private void initViews() {
        submitBtn = findViewById(R.id.submit_otp_btn);
        otpText = findViewById(R.id.enter_otp_text);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp(otpText.getText().toString());

            }
        });

    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(id, otp);
        signIn(phoneAuthCredential);

    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        getVizoApplication().getFirebaseAuth().signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                Log.d(TAG, "onComplete: " + firebaseUser.getPhoneNumber());
                Intent intent = new Intent(EnterOtpActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            } else {
                Log.d(TAG, task.getException().getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(vizoApplication, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public VizoApplication getVizoApplication() {
        if (vizoApplication == null) {
            vizoApplication = new VizoApplication();
        }
        return vizoApplication;
    }

    private void generateOtp(String mobileNumber) {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(getVizoApplication().getFirebaseAuth())
                .setPhoneNumber(mobileNumber)
                .setTimeout(30L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential.getSmsCode());
                        signIn(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Timber.e(e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.d(TAG, "onCodeSent: " + verificationId);
                        id = verificationId;
                        super.onCodeSent(verificationId, forceResendingToken);
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);


    }

}