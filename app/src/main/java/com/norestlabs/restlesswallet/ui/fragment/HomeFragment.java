package com.norestlabs.restlesswallet.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.RWApplication;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.ui.MainActivity;
import com.norestlabs.restlesswallet.ui.TransactionActivity_;
import com.norestlabs.restlesswallet.ui.adapter.CoinAdapter;
import com.norestlabs.restlesswallet.utils.Global;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    @ViewById
    public SwipeRefreshLayout refreshLayout;

    @ViewById
    RecyclerView recyclerView;

    CoinAdapter mAdapter;
    List<CoinModel> mModels;

    MainActivity activity;

    private static final Comparator<CoinModel> COMPARATOR = new SortedListAdapter.ComparatorBuilder<CoinModel>()
            .setOrderForModel(CoinModel.class, (a, b) -> a.getSymbol().compareTo(b.getSymbol()))
            .build();

    @AfterViews
    void init() {
        activity = (MainActivity)getActivity();
        refreshLayout.setOnRefreshListener(() -> {
            activity.sync();
        });

        mAdapter = new CoinAdapter(activity, CoinModel.class, COMPARATOR, model -> {
            Intent intent = new Intent(getContext(), TransactionActivity_.class);
            intent.putExtra("data", model);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        initData();

        if (RWApplication.getApp().getBitcoin() == null) {
            activity.sync();
        }
    }

    private void initData() {
        mModels = new ArrayList<>();
        mModels.add(new CoinModel("BTC", "Bitcoin", Global.btcBalance, R.mipmap.btc));
        mModels.add(new CoinModel("ETH", "Ethereum", 0/*Global.ethBalance*/, R.mipmap.eth));//ignoring because eth has tokens
        mModels.add(new CoinModel("LTC", "Litecoin", Global.ltcBalance, R.mipmap.ltc));
        mModels.add(new CoinModel("NEO", "Neo", Global.neoBalance, R.mipmap.neo));
        mModels.add(new CoinModel("STL", "Stellar", Global.stlBalance, R.mipmap.stl));
        mAdapter.edit()
                .replaceAll(filter(mModels, activity.searchView.getQuery().toString()))
                .commit();
    }

    private List<CoinModel> filter(List<CoinModel> models, String query) {
        final List<CoinModel> filteredModelList = new ArrayList<>();
        for (CoinModel model : models) {
            final String symbol = model.getSymbol().toLowerCase();
            final String coin = model.getCoin().toLowerCase();
            if (symbol.contains(query.toLowerCase().trim()) || coin.contains(query.toLowerCase().trim())) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void onQueryTextChange(String query) {
        mAdapter.edit()
                .replaceAll(filter(mModels, query))
                .commit();
    }

    public void onBalanceChange(double balance, int index) {
        mModels.get(index).setBalance(balance);
        if (mAdapter != null && getActivity() != null) {
            getActivity().runOnUiThread(() -> mAdapter.notifyDataSetChanged());
        }
    }

    public void onMarketInfoChange() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
