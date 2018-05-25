package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.norestlabs.restlesswallet.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.lujun.androidtagview.TagContainerLayout;

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
        tagContainerLayout2.setTags(tags);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnContinue:
                break;
            case R.id.btnBack:
                intent = new Intent(this, TutorialActivity_.class);
                intent.putExtra("tabIndex", 2);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
