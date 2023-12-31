package com.sandeep.vizo.utility;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

public class VizoApplication extends Application {

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);
        }
        return firebaseAuth;
    }
}
