package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.norestlabs.restlesswallet.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_verification)
public class VerificationActivity extends AppCompatActivity implements NumberKeyboardListener {

    private int pinCode;

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtTitle;

    @ViewById
    NumberKeyboard numberKeyboard;

    @AfterViews
    protected void init() {
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        txtTitle.setText(R.string.verification);

        numberKeyboard.setListener(this);
    }

    @Override
    public void onNumberClicked(int number) {

    }

    @Override
    public void onLeftAuxButtonClicked() {

    }

    @Override
    public void onRightAuxButtonClicked() {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MnemonicActivity_.class);
        startActivity(intent);
        finish();
    }
}
