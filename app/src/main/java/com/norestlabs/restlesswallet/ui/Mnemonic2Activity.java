package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

@EActivity(R.layout.activity_mnemonic2)
public class Mnemonic2Activity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtTitle;

    @ViewById
    TagContainerLayout tagContainerLayout1, tagContainerLayout2;

    @AfterViews
    protected void init() {
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        txtTitle.setText(R.string.mnemonic);

        final String[] tags = getResources().getStringArray(R.array.tags);
        Utils.randomArray(tags);
        tagContainerLayout2.setTags(tags);

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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
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
        Intent intent = new Intent(this, MnemonicActivity_.class);
        startActivity(intent);
        finish();
    }
}
