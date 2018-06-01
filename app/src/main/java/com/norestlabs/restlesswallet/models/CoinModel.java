package com.norestlabs.restlesswallet.models;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.io.Serializable;

public class CoinModel implements SortedListAdapter.ViewModel, Serializable {

    private String symbol;
    private String coin;
    private double balance;
    private double price;
    private int imgResId;

    public CoinModel(String symbol, String coin, double balance, double price, int imgResId) {
        this.symbol = symbol;
        this.coin = coin;
        this.balance = balance;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public int getImgResId() {
        return imgResId;
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
