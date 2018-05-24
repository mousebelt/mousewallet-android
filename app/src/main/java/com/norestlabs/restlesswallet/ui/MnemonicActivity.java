package com.norestlabs.restlesswallet.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.norestlabs.restlesswallet.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_tutorial)
public class MnemonicActivity extends AppCompatActivity {

    @AfterViews
    protected void init() {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
