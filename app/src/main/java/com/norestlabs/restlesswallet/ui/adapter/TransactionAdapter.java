package com.norestlabs.restlesswallet.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.wallet.NeoTransaction;
import com.norestlabs.restlesswallet.models.wallet.Transaction;
import com.norestlabs.restlesswallet.ui.holder.TransactionHolder;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder> {

    private NeoTransaction transaction;

    public TransactionAdapter(List<Transaction> transactions) {
        if (transactions.size() > 0) {
            final Transaction transaction = transactions.get(0);
            if (transaction instanceof NeoTransaction) {
                this.transaction = (NeoTransaction)transaction;
            }
        }
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder viewHolder, int position) {
        String address;
        double value;
        if (position < transaction.vin.size()) {
            address = transaction.vin.get(position).address.address;
            value = transaction.vin.get(position).address.value;
            viewHolder.updateView(address, "+" + value);
        } else {
            address = transaction.vout.get(position - transaction.vin.size()).address;
            value = transaction.vout.get(position - transaction.vin.size()).value;
            viewHolder.updateView(address, "-" + value);
        }
    }

    @Override
    public int getItemCount() {
        return transaction == null ? 0 : transaction.vin.size() + transaction.vout.size();
    }
}
