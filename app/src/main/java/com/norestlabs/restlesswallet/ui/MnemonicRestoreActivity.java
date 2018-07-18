package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.mnemonics.English;
import com.norestlabs.restlesswallet.utils.Constants;
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

@EActivity(R.layout.activity_mnemonic_restore)
public class MnemonicRestoreActivity extends AppCompatActivity implements KeyboardView.OnKeyboardActionListener {

    @ViewById
    TagContainerLayout tagContainerLayout;

    @ViewById
    KeyboardView keyboardView;

    @ViewById
    TextView txtHint1, txtHint2, txtHint3, txtInput;

    private String keyword = "";
    private Keyboard keyboard;

    @AfterViews
    protected void init() {
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                tagContainerLayout.removeTag(position);
                tagContainerLayout.addTag(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        keyboard = new Keyboard(this, R.xml.keyboard);
        keyboardView.setKeyboard(keyboard);;
        keyboardView.setOnKeyboardActionListener(this);
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {
        keyword += (char)primaryCode;
        setRecommends();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void setRecommends() {
        if (keyword.isEmpty()) {
            txtInput.setText(keyword);
            return;
        }
        clearInput();
        txtInput.setText(keyword);
        int count = 0;
        for (String word : English.words) {
            if (word.toLowerCase().startsWith(keyword.toLowerCase()) && !tagContainerLayout.getTags().contains(word)) {
                count ++;
                switch (count) {
                    case 1:
                        txtHint1.setText(word);
                        break;
                    case 2:
                        txtHint2.setText(word);
                        break;
                    case 3:
                        txtHint3.setText(word);
                        break;
                    default:
                        return;
                }
            }
        }
        if (count == 1) {
            addTag(txtHint1.getText().toString());
        }
    }

    private void addTag(String tag) {
        if (!tag.isEmpty() && tagContainerLayout.getTags().size() < Constants.MNEMONIC_SIZE) {
            tagContainerLayout.addTag(tag);
            keyword = "";
            clearInput();
        }
    }

    private void clearInput() {
        txtInput.setText("");
        txtHint1.setText("");
        txtHint2.setText("");
        txtHint3.setText("");
    }

    private void verifyTags() {
        if (tagContainerLayout.getTags().size() < Constants.MNEMONIC_SIZE) {
            showFailedError(R.string.invalid_fill_mnemonic);
            return;
        }

        final String mnemonic = Utils.arrayListToString(tagContainerLayout.getTags());
        final String seed = Utils.generateSeed(mnemonic);
        final byte[] bSeed = Utils.generateBSeed(mnemonic);
        Log.d("MNEMONIC", mnemonic);
        Log.d("SEED", seed);

        if (seed == null || seed.isEmpty()) {
            showFailedError(R.string.invalid_mnemonic);
        } else {
            Intent intent = new Intent(this, PINVerificationActivity_.class);
            intent.putExtra("mnemonic", mnemonic);
            intent.putExtra("seed", seed);
            intent.putExtra("bseed", bSeed);
            startActivity(intent);
            finish();
        }
    }

    private void showFailedError(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHint1:
                addTag(txtHint1.getText().toString());
                break;
            case R.id.txtHint2:
                addTag(txtHint2.getText().toString());
                break;
            case R.id.txtHint3:
                addTag(txtHint3.getText().toString());
                break;
            case R.id.btnDelete:
                if (keyword.length() > 0) {
                    keyword = keyword.substring(0, keyword.length() - 1);
                    setRecommends();
                    break;
                }
            case R.id.btnSpace:
                final int size = tagContainerLayout.getTags().size();
                if (size > 0) {
                    tagContainerLayout.removeTag(size - 1);
                }
                break;
            case R.id.btnNext:
                verifyTags();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TutorialActivity_.class);
        startActivity(intent);
        finish();
    }
}
