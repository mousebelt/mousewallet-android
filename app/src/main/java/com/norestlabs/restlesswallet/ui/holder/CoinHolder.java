package com.norestlabs.restlesswallet.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.CoinModel;
import com.norestlabs.restlesswallet.ui.adapter.CoinAdapter;

public class CoinHolder extends SortedListAdapter.ViewHolder<CoinModel> {

    private final View view;
    private final TextView txtSymbol;
    private final TextView txtCoin;
    private final TextView txtBalance;
    private final TextView txtPrice;
    private final ImageView imgSymbol;

    private final CoinAdapter.OnItemClickListener mListener;

    public CoinHolder(@NonNull View itemView, CoinAdapter.OnItemClickListener listener) {
        super(itemView);

        view = itemView;
        txtSymbol = itemView.findViewById(R.id.txtSymbol);
        txtCoin = itemView.findViewById(R.id.txtCoin);
        txtBalance = itemView.findViewById(R.id.txtBalance);
        txtPrice = itemView.findViewById(R.id.txtPrice);
        imgSymbol = itemView.findViewById(R.id.imgSymbol);

        mListener = listener;
    }

    @Override
    protected void performBind(@NonNull CoinModel item) {
        txtSymbol.setText(item.getSymbol());
        txtCoin.setText(item.getCoin());
        txtBalance.setText(String.valueOf(item.getBalance()));
        txtPrice.setText("$" + item.getPrice());
        imgSymbol.setImageResource(item.getImgResId());

        view.setOnClickListener(v -> {
            mListener.onClick(item);
        });
    }
}