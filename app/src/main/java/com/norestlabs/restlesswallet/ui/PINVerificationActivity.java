package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.os.Handler;
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
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pin_verification)
public class PINVerificationActivity extends AppCompatActivity implements NumberKeyboardListener {

    private String mSavedPinCode, mPinCode = "", mPinCodeAgain = "";
    private ImageView pinArray[];
    private Status mStatus = Status.ENTER;

    enum Status {ENTER, VERIFY, ERROR}

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtTitle, txtPIN;

    @ViewById
    View btnBack, llError;

    @ViewById
    NumberKeyboard numberKeyboard;

    @ViewById
    ImageView imgPin1, imgPin2, imgPin3, imgPin4, imgPin5, imgPin6;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);
        txtTitle.setText(R.string.verification);

        mSavedPinCode = getIntent().getStringExtra("pincode");

        numberKeyboard.setListener(this);

        pinArray = new ImageView[]{imgPin1, imgPin2, imgPin3, imgPin4, imgPin5, imgPin6};
    }

    private void emptyPINView(Status status, boolean timeout) {
        new Handler().postDelayed(() -> {
            mStatus = status;
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
        }, timeout ? Constants.PIN_DURATION : 0);
    }

    private void onSuccess() {
        new Handler().postDelayed(() -> {
            final AppPreferences appPreferences = RWApplication.getApp().getPreferences();
            final String mnemonic, seed;
            final byte[] bSeed;

            if (mSavedPinCode == null) {
                if (!appPreferences.setPin(mPinCode)) return;

                mnemonic = getIntent().getStringExtra("mnemonic");
                if (!appPreferences.setMnemonic(mnemonic)) return;

                seed = getIntent().getStringExtra("seed");
                bSeed = getIntent().getByteArrayExtra("bseed");
            } else {
                mnemonic = appPreferences.getMnemonic();
                if (mnemonic == null) return;

                seed = Utils.generateSeed(mnemonic);
                bSeed = Utils.generateBSeed(mnemonic);
            }

            if (seed == null || seed.isEmpty()) return;
            RWApplication.getApp().setSeed(seed);
            RWApplication.getApp().setBSeed(bSeed);

            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("exist", mSavedPinCode != null);
            startActivity(intent);
            finish();
        }, 0);
    }

    @Override
    public void onNumberClicked(int number) {
        if (mStatus == Status.ENTER) {
            if (mPinCode.length() == Constants.PIN_LENGTH) return;
            mPinCode = mPinCode + number;
            pinArray[mPinCode.length() - 1].setImageResource(R.drawable.pin_full);
            if (mPinCode.length() == Constants.PIN_LENGTH) {
                if (mSavedPinCode == null) {
                    emptyPINView(Status.VERIFY, true);
                } else {
                    if (RWApplication.getApp().getPreferences().isPincodeMatch(mPinCode)) {
                        onSuccess();
                    } else {
                        emptyPINView(Status.ERROR, true);
                    }
                }
            }
        } else {
            if (mPinCodeAgain.length() == Constants.PIN_LENGTH) return;
            mPinCodeAgain = mPinCodeAgain + number;
            pinArray[mPinCodeAgain.length() - 1].setImageResource(R.drawable.pin_full);
            if (mPinCodeAgain.length() == Constants.PIN_LENGTH) {
                if ((mSavedPinCode == null && mPinCode.equals(mPinCodeAgain)) ||
                        (mSavedPinCode != null && RWApplication.getApp().getPreferences().isPincodeMatch(mPinCodeAgain))) {
                    onSuccess();
                } else {
                    mPinCodeAgain = "";
                    emptyPINView(Status.ERROR, true);
                }
            }
        }
    }

    @Override
    public void onLeftAuxButtonClicked() {
        mPinCode = "";
        mPinCodeAgain = "";
        emptyPINView(Status.ENTER, false);
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
