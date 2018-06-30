package com.norestlabs.restlesswallet.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.norestlabs.restlesswallet.BuildConfig;
import com.norestlabs.restlesswallet.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_about)
public class AboutActivity extends AppCompatActivity {

    @ViewById
    TextView txtVersion1, txtVersion2, txtVersion3;

    @AfterViews
    protected void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_about);

        txtVersion1.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        txtVersion2.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        txtVersion3.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
