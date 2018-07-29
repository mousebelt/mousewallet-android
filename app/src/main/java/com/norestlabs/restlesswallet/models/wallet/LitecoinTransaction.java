package com.norestlabs.restlesswallet.models.wallet;

public class LitecoinTransaction extends Transaction {

    private boolean received;

    @Override
    public double getValue() {
        return super.getValue() / Math.pow(10, 8);
    }

    public boolean isReceived() {
        return received;
    }
}
