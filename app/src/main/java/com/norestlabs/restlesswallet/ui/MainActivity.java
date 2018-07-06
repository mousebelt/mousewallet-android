package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.models.wallet.EthereumTransaction;
import com.norestlabs.restlesswallet.models.wallet.LitecoinTransaction;
import com.norestlabs.restlesswallet.models.wallet.NeoTransaction;
import com.norestlabs.restlesswallet.models.wallet.StellarTransaction;
import com.norestlabs.restlesswallet.models.wallet.Transaction;
import com.norestlabs.restlesswallet.ui.fragment.HomeFragment;
import com.norestlabs.restlesswallet.ui.fragment.HomeFragment_;
import com.norestlabs.restlesswallet.utils.Constants;
import com.norestlabs.restlesswallet.utils.Global;
import com.norestlabs.restlesswallet.utils.Utils;
import com.norestlabs.restlesswallet.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.util.List;

import module.nrlwallet.com.nrlwalletsdk.abstracts.NRLCallback;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    @ViewById
    Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById
    public SearchView searchView;

    @ViewById
    ProgressBar progressBar;

    private HomeFragment homeFragment;
    private int selectedFragmentIndex = 0;

    private class GenerateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            generateWallet();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);

        searchView.setOnQueryTextListener(this);

        homeFragment = new HomeFragment_.FragmentBuilder_().build();
        loadFragment(selectedFragmentIndex);

        if (RWApplication.getApp().getBitcoin() == null) {
            new GenerateTask().execute();
//            new Handler().postDelayed(() -> generateWallet(), Constants.REQUEST_DURATION);
        }
    }

    private void generateWallet() {
        final byte[] bSeed = Utils.stringToBytes(RWApplication.getApp().getSeed());

        //ETH
        WalletUtils.getEthereumWallet(bSeed, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {
                Global.ethBalance = Double.valueOf(response);
                homeFragment.onBalanceChange(Global.ethBalance, 1);
            }
            @Override
            public void onResponseArray(JSONArray jsonArray) {

            }
        }, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {
                Global.ethTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<EthereumTransaction>>(){}.getType());
            }
        });

        //LTC
        WalletUtils.getLitecoinWallet(bSeed, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {
                Global.ltcBalance = Double.valueOf(response);
                homeFragment.onBalanceChange(Global.ltcBalance, 2);
            }
            @Override
            public void onResponseArray(JSONArray jsonArray) {

            }
        }, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {
                Global.ltcTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<LitecoinTransaction>>(){}.getType());
            }
        });

        //NEO
        WalletUtils.getNeoWallet(bSeed, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {
                Global.neoBalance = Double.valueOf(response);
                homeFragment.onBalanceChange(Global.neoBalance, 3);
            }
            @Override
            public void onResponseArray(JSONArray jsonArray) {

            }
        }, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {
                Global.neoTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NeoTransaction>>(){}.getType());
            }
        });

        //STL
        WalletUtils.getStellarWallet(bSeed, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {
                Global.stlBalance = Double.valueOf(response);
                homeFragment.onBalanceChange(Global.stlBalance, 4);
            }
            @Override
            public void onResponseArray(JSONArray jsonArray) {

            }
        }, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {
                Global.stlTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<StellarTransaction>>(){}.getType());
            }
        });

        //BTC
        final String balance = WalletUtils.getBitcoinWallet(bSeed, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {
                Global.btcTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Transaction>>(){}.getType());
            }
        });
        Global.btcBalance = Double.valueOf(balance);
        homeFragment.onBalanceChange(Global.btcBalance, 0);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            loadFragment(0);
        } else if (id == R.id.nav_about) {
            intent = new Intent(this, AboutActivity_.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            RWApplication.getApp().getPreferences().setPin(null);
            RWApplication.getApp().getPreferences().setMnemonic(null);
            intent = new Intent(this, TutorialActivity_.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(int index) {
        selectedFragmentIndex = index;
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 0:
                ft.replace(R.id.container, homeFragment);
                break;
            default:
                return;
        }
        ft.commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (selectedFragmentIndex == 0) {
            homeFragment.onQueryTextChange(query);
        }
        return true;
    }
}
