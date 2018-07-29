package com.norestlabs.restlesswallet.models.response;

import com.norestlabs.restlesswallet.models.wallet.LitecoinTransaction;

import java.util.List;

public class LitecoinResponse {

    private String address;
    private String balance;//satoshi
    private List<LitecoinTransaction> history;

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return Double.valueOf(balance) / Math.pow(10, 8);
    }

    public List<LitecoinTransaction> getTransactions() {
        return history;
    }
}
