package com.norestlabs.restlesswallet.models.wallet;

public class Transaction {

    private String txid;

    private double value;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
