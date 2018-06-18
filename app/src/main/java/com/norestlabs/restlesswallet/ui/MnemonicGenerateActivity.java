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
import module.nrlwallet.com.nrlwalletsdk.Language.English;
import module.nrlwallet.com.nrlwalletsdk.Utils.GenerateMnemonic;

@EActivity(R.layout.activity_mnemonic)
public class MnemonicActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtTitle;

    @ViewById
    TagContainerLayout tagContainerLayout;

    String[] tags;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);
        txtTitle.setText(R.string.mnemonic);

        final StringBuilder sb = new StringBuilder();
        new GenerateMnemonic(English.INSTANCE).createMnemonic(sb::append);
        tags = sb.toString().split(" ");
        tagContainerLayout.setTags(tags);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                Intent intent = new Intent(this, Mnemonic2Activity_.class);
                intent.putExtra("tags", tags);
                startActivity(intent);
                finish();
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
        intent.putExtra("tabIndex", 2);
        startActivity(intent);
        finish();
    }
}
