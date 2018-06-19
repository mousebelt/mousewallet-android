package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.utils.AppPreferences;
import com.norestlabs.restlesswallet.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pin_verification)
public class PINVerificationActivity extends AppCompatActivity implements NumberKeyboardListener {

    private String mPinCode = "", mPinCodeAgain = "";
    private ImageView pinArray[];
    private Status mStatus = Status.ENTER;

    enum Status {ENTER, VERIFY, ERROR}

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtTitle, txtPIN;

    @ViewById
    View llError;

    @ViewById
    NumberKeyboard numberKeyboard;

    @ViewById
    ImageView imgPin1, imgPin2, imgPin3, imgPin4, imgPin5, imgPin6;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);
        txtTitle.setText(R.string.verification);

        numberKeyboard.setListener(this);

        pinArray = new ImageView[]{imgPin1, imgPin2, imgPin3, imgPin4, imgPin5, imgPin6};
    }

    private void emptyPINView() {
        switch (mStatus) {
            case ENTER:
                llError.setVisibility(View.GONE);
                txtPIN.setText(R.string.enter_a_pin);
                break;
            case VERIFY:
                llError.setVisibility(View.GONE);
                txtPIN.setText(R.string.verify_your_pin);
                break;
            case ERROR:
                llError.setVisibility(View.VISIBLE);
                txtPIN.setText("");
                break;
        }
        for (int i = 0; i < Constants.PIN_LENGTH; i ++) {
            pinArray[i].setImageResource(R.drawable.pin_empty);
        }
    }

    private void onSuccess() {
        final AppPreferences appPreferences = RWApplication.getApp().getPreferences();
        if (!appPreferences.setPin(mPinCode)) return;

        final String mnemonic = getIntent().getStringExtra("mnemonic");
        if (!appPreferences.setMnemonic(mnemonic)) return;

        final String seed = getIntent().getStringExtra("seed");
        RWApplication.getApp().setSeed(seed);

        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNumberClicked(int number) {
        if (mStatus == Status.ENTER) {
            if (mPinCode.length() == Constants.PIN_LENGTH) return;
            mPinCode = mPinCode + number;
            pinArray[mPinCode.length() - 1].setImageResource(R.drawable.pin_full);
            if (mPinCode.length() == Constants.PIN_LENGTH) {
                mStatus = Status.VERIFY;
                emptyPINView();
            }
        } else {
            if (mPinCodeAgain.length() == Constants.PIN_LENGTH) return;
            mPinCodeAgain = mPinCodeAgain + number;
            pinArray[mPinCodeAgain.length() - 1].setImageResource(R.drawable.pin_full);
            if (mPinCodeAgain.length() == Constants.PIN_LENGTH) {
                if (mPinCode.equals(mPinCodeAgain)) {
                    onSuccess();
                } else {
                    mStatus = Status.ERROR;
                    mPinCodeAgain = "";
                    emptyPINView();
                }
            }
        }
    }

    @Override
    public void onLeftAuxButtonClicked() {
        mStatus = Status.ENTER;
        mPinCode = "";
        mPinCodeAgain = "";
        emptyPINView();
    }

    @Override
    public void onRightAuxButtonClicked() {
        if (mStatus == Status.ENTER) {
            if (mPinCode.isEmpty()) return;
            mPinCode = mPinCode.substring(0, mPinCode.length() - 1);
            pinArray[mPinCode.length()].setImageResource(R.drawable.pin_empty);
        } else {
            if (mPinCodeAgain.isEmpty()) return;
            mPinCodeAgain = mPinCodeAgain.substring(0, mPinCodeAgain.length() - 1);
            pinArray[mPinCodeAgain.length()].setImageResource(R.drawable.pin_empty);
        }
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
        Intent intent = new Intent(this, MnemonicGenerateActivity_.class);
        startActivity(intent);
        finish();
    }
}
