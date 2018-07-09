package com.norestlabs.restlesswallet.models.response;

import com.google.gson.annotations.SerializedName;
import com.norestlabs.restlesswallet.models.CoinMarketCap;

public class ConversionResponse {

    private MarketData data;
    private MetaData metadata;

    public CoinMarketCap getBTC() {
        return data.bitcoin;
    }

    public CoinMarketCap getETH() {
        return data.ethereum;
    }

    public CoinMarketCap getLTC() {
        return data.litecoin;
    }

    public CoinMarketCap getNEO() {
        return data.neo;
    }

    public CoinMarketCap getSTL() {
        return data.stellar;
    }

    private class MarketData {
        @SerializedName("1")
        private CoinMarketCap bitcoin;

        @SerializedName("1027")
        CoinMarketCap ethereum;

        @SerializedName("2")
        CoinMarketCap litecoin;

        @SerializedName("1376")
        CoinMarketCap neo;

        @SerializedName("512")
        CoinMarketCap stellar;
    }

    private class MetaData {
        private long timestamp;
        private int num_cryptocurrencies;
        private String error;
    }
}
