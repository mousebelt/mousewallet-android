package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
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

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.ui.fragment.HomeFragment;
import com.norestlabs.restlesswallet.ui.fragment.HomeFragment_;
import com.norestlabs.restlesswallet.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import module.nrlwallet.com.nrlwalletsdk.Coins.NRLBitcoin;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLEthereum;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLLite;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLNeo;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLStellar;

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

//        generateWallet();
    }

    private void generateWallet() {
        byte[] bseed = Utils.stringToBytes(RWApplication.getApp().getSeed());

        //ETH
//        NRLEthereum nrlEthereum = new NRLEthereum(bseed);
//        String ethRootKey = nrlEthereum.getRootKey();
//        String ethPrivateKey = nrlEthereum.getPrivateKey();
//        String ethAddress = nrlEthereum.getAddress();

        //BTC
//        NRLBitcoin nrlBitcoin = new NRLBitcoin(bseed);
//        String btcPrivateKey = nrlBitcoin.getPublicKey();
//        String btcAddress = nrlBitcoin.getAddress();

        //LTC
        NRLLite nrlLite = new NRLLite(bseed);
        String nrlPrivateKey = nrlLite.getPublicKey();
        String nrlAddress = nrlLite.getAddress();

        //STL
//        NRLStellar nrlStellar = new NRLStellar(bseed);
//        String stlPrivateKey = nrlStellar.getPrivateKey();
//        String stlAddress = nrlStellar.getAddress();

        //NEO
        NRLNeo nrlNeo = new NRLNeo(bseed);
        String neoPrivateKey = nrlNeo.getPrivateKey();
        String neoAddress = nrlNeo.getAddress();
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
