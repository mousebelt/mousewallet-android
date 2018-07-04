package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.models.wallet.Transaction;
import com.norestlabs.restlesswallet.ui.adapter.ViewPagerAdapter;
import com.norestlabs.restlesswallet.ui.fragment.ReceiveFragment_;
import com.norestlabs.restlesswallet.ui.fragment.SendFragment;
import com.norestlabs.restlesswallet.ui.fragment.SendFragment_;
import com.norestlabs.restlesswallet.ui.fragment.SwapFragment_;
import com.norestlabs.restlesswallet.utils.Global;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.nrlwallet.com.nrlwalletsdk.Coins.NRLBitcoin;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLEthereum;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLLite;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLNeo;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLStellar;

@EActivity(R.layout.activity_transaction)
public class TransactionActivity extends AppCompatActivity {

    @ViewById
    TabLayout tabLayout;

    @ViewById
    ViewPager viewPager;

    private Menu menu;
    private ViewPagerAdapter adapter;

    public CoinModel coinModel;
    public String selectedAddress;
    public double selectedBalance;
    public List<Transaction> selectedTransactions;

    @AfterViews
    protected void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Serializable serializable = getIntent().getSerializableExtra("data");
        if (serializable != null) {
            coinModel = (CoinModel)serializable;
            getSupportActionBar().setTitle(coinModel.getCoin());

            selectedTransactions = new ArrayList<>();
            switch (coinModel.getSymbol()) {
                case "BTC":
                    final NRLBitcoin nrlBitcoin = RWApplication.getApp().getBitcoin();
                    if (nrlBitcoin != null) {
                        selectedAddress = nrlBitcoin.getAddress();
                    }
                    selectedBalance = Global.btcBalance;
                    break;
                case "ETH":
                    final NRLEthereum nrlEthereum = RWApplication.getApp().getEthereum();
                    if (nrlEthereum != null) {
                        selectedAddress = nrlEthereum.getAddress();
                    }
                    selectedBalance = Global.ethBalance;
                    break;
                case "LTC":
                    final NRLLite nrlLite = RWApplication.getApp().getLitecoin();
                    if (nrlLite != null) {
                        selectedAddress = nrlLite.getAddress();
                    }
                    selectedBalance = Global.ltcBalance;
                    break;
                case "NEO":
                    final NRLNeo nrlNeo = RWApplication.getApp().getNeo();
                    if (nrlNeo != null) {
                        selectedAddress = nrlNeo.getAddress();
                    }
                    selectedBalance = Global.neoBalance;
                    if (Global.neoTransactions != null) {
                        selectedTransactions.addAll(Global.neoTransactions);
                    }
                    break;
                case "STL":
                    final NRLStellar nrlStellar = RWApplication.getApp().getStellar();
                    if (nrlStellar != null) {
                        selectedAddress = nrlStellar.getAddress();
                    }
                    selectedBalance = Global.stlBalance;
                    break;
                default:
                    break;
            }
        }

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment ft = new ReceiveFragment_();
        adapter.addFragment(ft, getString(R.string.receive));
        ft = new SendFragment_();
        adapter.addFragment(ft, getString(R.string.send));
        ft = new SwapFragment_();
        adapter.addFragment(ft, getString(R.string.swap));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    menu.getItem(0).setIcon(R.mipmap.ic_capture);
                } else {
                    menu.getItem(0).setIcon(R.drawable.star_empty);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if (viewPager.getCurrentItem() == 1) {
                ((SendFragment)adapter.getItem(1)).scan();
            } else {
                item.setIcon(R.drawable.star_empty);
            }
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
