package com.norestlabs.restlesswallet.models.response;

public class MarketInfoResponse {

    private String pair;
    private double rate;
    private double minerFee;
    private double limit;
    private double minimum;
    private double maxLimit;

    private String error;

    public String getPair() {
        return pair;
    }

    public double getRate() {
        return rate;
    }

    public double getMinerFee() {
        return minerFee;
    }

    public double getLimit() {
        return limit;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public String getError() {
        return error;
    }
}
