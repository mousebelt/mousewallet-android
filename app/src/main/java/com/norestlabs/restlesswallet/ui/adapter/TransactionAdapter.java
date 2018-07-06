package com.norestlabs.restlesswallet.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.norestlabs.restlesswallet.R;
import com.norestlabs.restlesswallet.models.wallet.Transaction;
import com.norestlabs.restlesswallet.ui.holder.TransactionHolder;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder> {

    private List<Transaction> transactions = new ArrayList<>();

    public TransactionAdapter(List<Transaction> transactions) {
        if (transactions != null) {
            this.transactions = transactions;
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
        final Transaction transaction = transactions.get(position);
        viewHolder.updateView(transaction.getTxid(), String.valueOf(transaction.getValue()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
