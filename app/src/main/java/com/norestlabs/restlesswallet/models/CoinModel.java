package com.norestlabs.restlesswallet.models;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

public class CoinModel implements SortedListAdapter.ViewModel {

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
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T model) {
        return false;
    }
}
