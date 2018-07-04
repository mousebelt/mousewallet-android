package com.norestlabs.restlesswallet.ui.holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.norestlabs.restlesswallet.R;

public class TransactionHolder extends RecyclerView.ViewHolder {

    private TextView txtAddress;
    private TextView txtValue;

    public TransactionHolder(View itemView) {
        super(itemView);

        txtAddress = itemView.findViewById(R.id.txtAddress);
        txtValue = itemView.findViewById(R.id.txtValue);
    }

    public void updateView(String address, String value) {
        txtAddress.setText(address);
        txtValue.setText(value);
        txtValue.setTextColor(Color.parseColor(value.charAt(0) == '+' ? "#33cc71" : "#878585"));
    }
}