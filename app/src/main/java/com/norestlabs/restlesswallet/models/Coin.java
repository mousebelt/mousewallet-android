package com.norestlabs.restlesswallet.models;

public class Coin {

    private String name;
    private String symbol;
    private String image;
    private String imageSmall;
    private String status;
    private double minerFee;

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getImage() {
        return image;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public String getStatus() {
        return status;
    }

    public double getMinerFee() {
        return minerFee;
    }
}
