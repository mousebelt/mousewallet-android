package com.norestlabs.restlesswallet.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.api.ApiClient;
import com.norestlabs.restlesswallet.models.CoinMarketCap;
import com.norestlabs.restlesswallet.models.response.ConversionResponse;
import com.norestlabs.restlesswallet.models.response.LitecoinResponse;
import com.norestlabs.restlesswallet.models.wallet.EthereumBalance;
import com.norestlabs.restlesswallet.models.wallet.LitecoinTransaction;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import module.nrlwallet.com.nrlwalletsdk.abstracts.LTCCallback;
import module.nrlwallet.com.nrlwalletsdk.abstracts.NRLCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private HomeFragment homeFragment;
    private int selectedFragmentIndex = 0;
    private boolean isSyncing, isLTCSyncing;

    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            isSyncing = true;
            if (homeFragment.refreshLayout != null) homeFragment.refreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            generateWallet();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            isSyncing = false;
            if (/*!isLTCSyncing &&*/ homeFragment.refreshLayout != null) homeFragment.refreshLayout.setRefreshing(false);
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

        getUSDConversionRate();
    }

    public void sync() {
        new SyncTask().execute();
    }

    private void getUSDConversionRate() {
        Call<ConversionResponse> call = ApiClient.getInterface(Constants.COINMARKET_URL).getCoinMarketCap("USD");
        call.enqueue(new Callback<ConversionResponse>() {
            @Override
            public void onResponse(Call<ConversionResponse> call, Response<ConversionResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    final ConversionResponse data = response.body();
                    if (data != null) {
                        final List<CoinMarketCap> coinMarketCaps = new ArrayList<>();
                        coinMarketCaps.add(data.getBTC());
                        coinMarketCaps.add(data.getETH());
                        coinMarketCaps.add(data.getLTC());
                        coinMarketCaps.add(data.getNEO());
                        coinMarketCaps.add(data.getSTL());
                        Global.marketInfo = coinMarketCaps;

                        homeFragment.onMarketInfoChange();
                    }
                } else {
                    showToastMessage(Utils.getErrorStringFromBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ConversionResponse> call, Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void generateWallet() {
        final byte[] bSeed = RWApplication.getApp().getBSeed();
        final String mnemonic = RWApplication.getApp().getPreferences().getMnemonic();
        final boolean isExist = getIntent().getBooleanExtra("is_exist", false);

        syncETH(bSeed, mnemonic);
        syncSTL(bSeed);
        syncNEO(bSeed, mnemonic);
        syncLTC(bSeed, mnemonic, isExist);
//        syncBTC(bSeed, mnemonic, isExist);
    }

    private void syncETH(final byte[] bSeed, final String mnemonic) {
        WalletUtils.getEthereumWallet(bSeed, mnemonic, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {//ETH Balance
                Global.ethBalance = Double.valueOf(response);
                homeFragment.onBalanceChange(Global.ethBalance, 1);
            }
            @Override
            public void onResponseArray(JSONArray jsonArray) {
                Global.ethBalances = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<EthereumBalance>>(){}.getType());
                for (final EthereumBalance balance : Global.ethBalances) {
                    if (balance.getSymbol().equals("ETH")) {
                        Global.ethBalance = balance.getBalance();
                        homeFragment.onBalanceChange(balance.getBalance(), 1);
                        return;
                    }
                }
            }
        }, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {//ETH Transaction
                Global.ethTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Transaction>>(){}.getType());
            }
        });
    }

    private void syncSTL(final byte[] bSeed) {
        WalletUtils.getStellarWallet(bSeed, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {//STL Balance
                try {
                    Global.stlBalance = Double.valueOf(response);
                    homeFragment.onBalanceChange(Global.stlBalance, 4);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
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
            public void onResponseArray(JSONArray jsonArray) {//STL Transaction
                Global.stlTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Transaction>>(){}.getType());
            }
        });
    }

    private void syncNEO(final byte[] bSeed, final String mnemonic) {
        WalletUtils.getNeoWallet(Utils.stringToBytes(RWApplication.getApp().getSeed()), mnemonic, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }
            @Override
            public void onResponse(String response) {//NEO Balance
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
            public void onResponseArray(JSONArray jsonArray) {//NEO Transaction
                Global.neoTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Transaction>>(){}.getType());
            }
        });
    }

    private void syncLTC(final byte[] bSeed, final String mnemonic, final boolean isExist) {
        isLTCSyncing = true;
        runOnUiThread(() -> {
            WalletUtils.getLitecoinWallet(bSeed, mnemonic, isExist, this, new LTCCallback() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    isLTCSyncing = false;
                    if (!isSyncing) runOnUiThread(() -> homeFragment.refreshLayout.setRefreshing(false));
                    final LitecoinResponse response = new Gson().fromJson(jsonObject.toString(), new TypeToken<LitecoinResponse>(){}.getType());
                    Global.ltcBalance = response.getBalance();
                    homeFragment.onBalanceChange(Global.ltcBalance, 2);

                    Global.ltcTransactions = new ArrayList<>();
                    for (LitecoinTransaction transaction : response.getTransactions()) {
                        Global.ltcTransactions.add(new Transaction() {{
                            setTxid(transaction.getTxid());
                            setValue(transaction.getValue() * (transaction.isReceived() ? 1 : -1));
                        }});
                    }
                }

                @Override
                public void onFailed(String response) {
                    isLTCSyncing = false;
                    if (!isSyncing) runOnUiThread(() -> homeFragment.refreshLayout.setRefreshing(false));
                }
            });
        });
    }

    private void syncBTC(final byte[] bSeed, final String mnemonic, final boolean isExist) {
        final String balance = WalletUtils.getBitcoinWallet(bSeed, mnemonic, isExist, new NRLCallback() {
            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onResponseArray(JSONArray jsonArray) {//BTC Transaction
                Global.btcTransactions = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Transaction>>(){}.getType());
            }
        });
        Global.btcBalance = Double.valueOf(balance) / Math.pow(10, 8);//balance unit: satoshi
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
        // sync action bar item clicks here. The action bar will
        // automatically sync clicks on the Home/Up button, so long
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
            intent = new Intent(this, SettingsActivity_.class);
            startActivityForResult(intent, 101);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            final boolean isLoggingOut = data.getBooleanExtra("logout", false);
            if (isLoggingOut) {
                logout();
            }
        }
    }

    private void logout() {
        if (Constants.USE_SYSTEM_SERVICE_TO_REMOVE_PREFERENCE) {
            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
        } else {
            RWApplication.getApp().getPreferences().setPin(null);
//            RWApplication.getApp().getPreferences().setMnemonic(null);//commenting to check if same mnemonic when login
        }
        Intent intent = new Intent(this, TutorialActivity_.class);
        startActivity(intent);
        finish();
    }
}
