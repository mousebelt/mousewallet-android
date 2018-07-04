package com.norestlabs.restlesswallet.models;

public class CoinMarketCap {

    private int id;
    private String name;
    private String symbol;
    private String website_slug;
    private int rank;
    private double circulating_supply;
    private double total_supply;
    private double max_supply;
    private long last_updated;

    private Quotes quotes;

    private class Quotes {
        private Quote USD;
    }

    private class Quote {
        private double price;
        private double volume_24h;
        private double market_cap;
        private double percent_change_1h;
        private double percent_change_24h;
        private double percent_change_7d;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getWebsiteSlug() {
        return website_slug;
    }

    public int getRank() {
        return rank;
    }

    public double getCirculatingSupply() {
        return circulating_supply;
    }

    public double getTotalSupply() {
        return total_supply;
    }

    public double getMaxSupply() {
        return max_supply;
    }

    public long getLastUpdated() {
        return last_updated;
    }

    public double getUSDPrice() {
        return quotes.USD.price;
    }
}
