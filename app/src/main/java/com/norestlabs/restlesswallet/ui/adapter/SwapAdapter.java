package com.norestlabs.restlesswallet.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.Coin;

import java.util.List;

public class SwapAdapter extends ArrayAdapter<Coin> {

    private Context context;

    public SwapAdapter(@NonNull Context context, int resource, List<Coin> swapCoins) {
        super(context, resource, swapCoins);

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        Coin rowItem = getItem(position);
        TextView txtItem = convertView.findViewById(R.id.txtItem);
        txtItem.setText(rowItem.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = getView(position, convertView, parent);

        Coin rowItem = getItem(position);
        TextView txtItem = view.findViewById(R.id.txtItem);
        txtItem.setText(rowItem.getName());

        return view;
    }
}
