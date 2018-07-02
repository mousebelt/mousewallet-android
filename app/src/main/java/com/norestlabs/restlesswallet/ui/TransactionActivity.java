package com.norestlabs.restlesswallet.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.ui.adapter.ViewPagerAdapter;
import com.norestlabs.restlesswallet.ui.fragment.ReceiveFragment_;
import com.norestlabs.restlesswallet.ui.fragment.SendFragment_;
import com.norestlabs.restlesswallet.ui.fragment.SwapFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;

@EActivity(R.layout.activity_transaction)
public class TransactionActivity extends AppCompatActivity {

    @ViewById
    TabLayout tabLayout;

    @ViewById
    ViewPager viewPager;

    public CoinModel coinModel;

    @AfterViews
    protected void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Serializable serializable = getIntent().getSerializableExtra("data");
        if (serializable != null) {
            coinModel = (CoinModel)serializable;
            getSupportActionBar().setTitle(coinModel.getCoin());
        }

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment ft = new ReceiveFragment_();
        adapter.addFragment(ft, getString(R.string.receive));
        ft = new SendFragment_();
        adapter.addFragment(ft, getString(R.string.send));
        ft = new SwapFragment_();
        adapter.addFragment(ft, getString(R.string.swap));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            item.setIcon(R.drawable.star_empty);
            return true;
        } else if (id == android.R.id.home) {
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
