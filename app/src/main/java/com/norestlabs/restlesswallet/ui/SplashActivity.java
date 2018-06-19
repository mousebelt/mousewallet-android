package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    String pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Intent intent;
            if (pincode == null) {
                intent = new Intent(this, TutorialActivity_.class);
            } else {
                intent = new Intent(this, PINVerificationActivity_.class);
                intent.putExtra("pincode", pincode);
            }
            startActivity(intent);
            finish();
        }, Constants.SPLASH_DURATION);

        pincode = RWApplication.getApp().getPreferences().getPin();
    }
}
