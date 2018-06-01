package com.norestlabs.restlesswallet.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.ui.holder.CoinHolder;

import java.util.Comparator;

public class CoinAdapter extends SortedListAdapter<CoinModel> {

    public interface OnItemClickListener {
        void onClick(CoinModel model);
    }

    private final OnItemClickListener mListener;

    public CoinAdapter(@NonNull Context context, @NonNull Class<CoinModel> itemClass, @NonNull Comparator<CoinModel> comparator, OnItemClickListener listener) {
        super(context, itemClass, comparator);
        mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder<? extends CoinModel> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.item_coin, parent, false);
        return new CoinHolder(itemView, mListener);
    }
}
