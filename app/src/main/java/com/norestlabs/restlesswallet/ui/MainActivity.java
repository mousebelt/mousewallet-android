package com.norestlabs.restlesswallet.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.ui.adapter.CoinAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SortedListAdapter.Callback {

    @ViewById
    Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById
    SearchView searchView;

    @ViewById
    RecyclerView recyclerView;

    CoinAdapter mAdapter;
    List<CoinModel> mModels;

    private static final Comparator<CoinModel> COMPARATOR = new SortedListAdapter.ComparatorBuilder<CoinModel>()
            .setOrderForModel(CoinModel.class, (a, b) -> a.getCoin().compareTo(b.getCoin()))
            .build();

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

        mAdapter = new CoinAdapter(this, CoinModel.class, COMPARATOR);
        mAdapter.addCallback(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        setDummyData();
    }

    private void setDummyData() {
        mModels = new ArrayList<>();
        mModels.add(new CoinModel("BTC", "Bitcoin", 0.134, 450, R.mipmap.btc));
        mModels.add(new CoinModel("ETH", "Ethereum", 1.134, 6450, R.mipmap.eth));
        mModels.add(new CoinModel("OMG", "OmiseGo", 0.134, 250, R.mipmap.omg));
        mAdapter.edit()
                .replaceAll(mModels)
                .commit();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_receive) {

        } else if (id == R.id.nav_swap) {

        } else if (id == R.id.nav_mnemonic) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, VerificationActivity_.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditFinished() {

    }
}
