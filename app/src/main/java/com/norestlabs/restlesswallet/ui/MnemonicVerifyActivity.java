package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

@EActivity(R.layout.activity_mnemonic_verify)
public class MnemonicVerifyActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtTitle;

    @ViewById
    TagContainerLayout tagContainerLayout1, tagContainerLayout2;

    private String[] tags;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);
        txtTitle.setText(R.string.mnemonic);

        tags = getIntent().getStringArrayExtra("tags");
        tagContainerLayout2.setTags(Utils.randomArray(tags));

        tagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                tagContainerLayout1.removeTag(position);
                tagContainerLayout2.addTag(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        tagContainerLayout2.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                tagContainerLayout2.removeTag(position);
                tagContainerLayout1.addTag(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }

    private void verifyTags() {
        if (tagContainerLayout1.getTags().size() != tags.length) {
            showFailedError(R.string.invalid_sort_mnemonic);
            return;
        }
        for (int i = 0; i < tags.length; i ++) {
            if (!tagContainerLayout1.getTags().get(i).equals(tags[i])) {
                showFailedError(R.string.invalid_mnemonic);
                return;
            }
        }
        Intent intent = new Intent(this, PINVerificationActivity_.class);
        startActivity(intent);
        finish();
    }

    private void showFailedError(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
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
        Intent intent = new Intent(this, MnemonicGenerateActivity_.class);
        startActivity(intent);
        finish();
    }
}
