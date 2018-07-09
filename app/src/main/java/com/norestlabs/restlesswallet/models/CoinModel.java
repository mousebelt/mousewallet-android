package com.norestlabs.restlesswallet.models;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.io.Serializable;

public class CoinModel implements SortedListAdapter.ViewModel, Serializable {

    private String symbol;
    private String coin;
    private double balance;
    private int imgResId;

    public CoinModel(String symbol, String coin, double balance, int imgResId) {
        this.symbol = symbol;
        this.coin = coin;
        this.balance = balance;
        this.imgResId = imgResId;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCoin() {
        return coin;
    }

    public double getBalance() {
        return balance;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T model) {
        if (model instanceof CoinModel) {
            final CoinModel wordModel = (CoinModel) model;
            return wordModel.symbol.equals(symbol);
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T model) {
        if (model instanceof CoinModel) {
            final CoinModel wordModel = (CoinModel) model;
            return wordModel.balance == balance;
        }
        return false;
    }
}
