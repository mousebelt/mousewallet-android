package com.norestlabs.restlesswallet.models.wallet;

public class EthereumToken {

    private String name;
    private String symbol;
    private String address;
    private int decimal;

    public EthereumToken(String name, String symbol, String address, int decimal) {
        this.name = name;
        this.symbol = symbol;
        this.address = address;
        this.decimal = decimal;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAddress() {
        return address;
    }

    public int getDecimal() {
        return decimal;
    }
}
