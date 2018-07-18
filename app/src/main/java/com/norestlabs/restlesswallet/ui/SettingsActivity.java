package com.norestlabs.restlesswallet.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.norestlabs.restlesswallet.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {

    @AfterViews
    protected void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_logout_title)
                        .setMessage(R.string.alert_logout_message)
                        .setPositiveButton(android.R.string.yes, (DialogInterface dialog, int which) -> {
                            logout();
                        })
                        .setNegativeButton(android.R.string.no, (DialogInterface dialog, int which) -> {
                            // do nothing
                        })
                        .setIcon(R.mipmap.ic_launcher)
                        .show();
                break;
            default:
                break;
        }
    }

    private void logout() {
        Intent intent = new Intent();
        intent.putExtra("logout", true);
        setResult(RESULT_OK, intent);
        finish();
    }
}
