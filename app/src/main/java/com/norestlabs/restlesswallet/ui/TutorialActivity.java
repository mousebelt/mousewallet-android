package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.ui.adapter.ViewPagerAdapter;
import com.norestlabs.restlesswallet.ui.fragment.Tutorial1Fragment_;
import com.norestlabs.restlesswallet.ui.fragment.Tutorial2Fragment_;
import com.norestlabs.restlesswallet.ui.fragment.Tutorial3Fragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_tutorial)
public class TutorialActivity extends AppCompatActivity {

    @ViewById
    ViewPager viewPager;

    @ViewById
    TabLayout tabDots;

    @AfterViews
    protected void init() {
        setupViewPager();
        tabDots.setupWithViewPager(viewPager, true);
        for (int i = 0; i < tabDots.getChildCount(); i ++) {
            tabDots.getChildAt(i).setOnTouchListener((v, event) -> true);
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment ft = new Tutorial1Fragment_();
        adapter.addFragment(ft, null);
        ft = new Tutorial2Fragment_();
        adapter.addFragment(ft, null);
        ft = new Tutorial3Fragment_();
        adapter.addFragment(ft, null);
        viewPager.setAdapter(adapter);
        viewPager.setOnTouchListener((v, event) -> true);
        viewPager.setCurrentItem(getIntent().getIntExtra("tabIndex", 0));
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnStart:
                viewPager.setCurrentItem(1);
                break;
            case R.id.btnRestore:
                break;
            case R.id.btnGo:
                viewPager.setCurrentItem(2);
                break;
            case R.id.btnSkip:
                break;
            case R.id.btnCreate:
                break;
            case R.id.btnRestoreMnemonic:
                intent = new Intent(this, Mnemonic2Activity_.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnBack:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;
            default:
                break;
        }
    }
}
