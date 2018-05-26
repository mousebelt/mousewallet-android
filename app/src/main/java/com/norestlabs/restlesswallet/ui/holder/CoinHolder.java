package com.norestlabs.restlesswallet.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;

public class CoinHolder extends SortedListAdapter.ViewHolder<CoinModel> {

//    private final TextView mValueView;

    public CoinHolder(@NonNull View itemView) {
        super(itemView);

//        mValueView = itemView.findViewById(R.id.value);
    }

    @Override
    protected void performBind(@NonNull CoinModel item) {
//        mValueView.setText(item.getValue());
    }
}