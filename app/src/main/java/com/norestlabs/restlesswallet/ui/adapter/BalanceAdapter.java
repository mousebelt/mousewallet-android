package com.norestlabs.restlesswallet.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.wallet.EthereumBalance;
import com.norestlabs.restlesswallet.utils.Utils;

import java.util.List;

public class BalanceAdapter extends ArrayAdapter<EthereumBalance> {

    private Context context;
    private boolean isIconVisible;

    public BalanceAdapter(@NonNull Context context, int resource, List<EthereumBalance> balances, boolean isIconVisible) {
        super(context, resource, balances);

        this.context = context;
        this.isIconVisible = isIconVisible;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_balance, parent, false);
        }

        EthereumBalance rowItem = getItem(position);
        if (isIconVisible) {
            ImageView imgSymbol = convertView.findViewById(R.id.imgSymbol);
            imgSymbol.setImageResource(Utils.getResourceId(context, rowItem.getSymbol().toLowerCase()));
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = getView(position, convertView, parent);

        EthereumBalance rowItem = getItem(position);
        ImageView imgSymbol = view.findViewById(R.id.imgSymbol);
        TextView txtBalance = view.findViewById(R.id.txtBalance);
        imgSymbol.setImageResource(Utils.getResourceId(context, rowItem.getSymbol().toLowerCase()));
        txtBalance.setText(context.getString(R.string.token_balance, rowItem.getBalance(), rowItem.getSymbol()));

        return view;
    }
}
