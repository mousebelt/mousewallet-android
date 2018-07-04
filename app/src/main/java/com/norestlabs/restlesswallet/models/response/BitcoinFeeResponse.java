package com.norestlabs.restlesswallet.models.response;

public class BitcoinFeeResponse {

    private double fastestFee;
    private double halfHourFee;
    private double hourFee;

    public double getFastestFee() {
        return fastestFee;
    }

    public double getHalfHourFee() {
        return halfHourFee;
    }

    public double getHourFee() {
        return hourFee;
    }
}
