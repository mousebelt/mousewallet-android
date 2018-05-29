package com.norestlabs.restlesswallet.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.ui.MainActivity;
import com.norestlabs.restlesswallet.ui.QueryListener;
import com.norestlabs.restlesswallet.ui.adapter.CoinAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment implements QueryListener {

    @ViewById
    RecyclerView recyclerView;

    CoinAdapter mAdapter;
    List<CoinModel> mModels;

    private static final Comparator<CoinModel> COMPARATOR = new SortedListAdapter.ComparatorBuilder<CoinModel>()
            .setOrderForModel(CoinModel.class, (a, b) -> a.getSymbol().compareTo(b.getSymbol()))
            .build();

    @AfterViews
    void init() {
        mAdapter = new CoinAdapter(getContext(), CoinModel.class, COMPARATOR);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        setDummyData();
    }

    private void setDummyData() {
        mModels = new ArrayList<>();
        mModels.add(new CoinModel("BTC", "Bitcoin", 0.134, 450, R.mipmap.btc));
        mModels.add(new CoinModel("ETH", "Ethereum", 1.134, 6450, R.mipmap.eth));
        mModels.add(new CoinModel("OMG", "OmiseGo", 0.134, 250, R.mipmap.omg));
        mAdapter.edit()
                .replaceAll(filter(mModels, ((MainActivity)getActivity()).searchView.getQuery().toString()))
                .commit();
    }

    private List<CoinModel> filter(List<CoinModel> models, String query) {
        final List<CoinModel> filteredModelList = new ArrayList<>();
        for (CoinModel model : models) {
            final String symbol = model.getSymbol().toLowerCase();
            final String coin = model.getCoin().toLowerCase();
            if (symbol.contains(query.toLowerCase()) || coin.contains(query.toLowerCase())) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onQueryTextChange(String query) {
        mAdapter.edit()
                .replaceAll(filter(mModels, query))
                .commit();
    }
}
