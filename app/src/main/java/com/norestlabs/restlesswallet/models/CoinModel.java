package com.norestlabs.restlesswallet.models;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

public class CoinModel implements SortedListAdapter.ViewModel {

    @Override
    public <T> boolean isSameModelAs(@NonNull T model) {
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T model) {
        return false;
    }
}
