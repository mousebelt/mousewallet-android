package com.norestlabs.restlesswallet.models.response;

import com.google.gson.annotations.SerializedName;
import com.norestlabs.restlesswallet.models.Coin;

public class CoinResponse {

    @SerializedName("BTC")
    private Coin bitcoin;

    @SerializedName("ETH")
    private Coin ethereum;

    @SerializedName("LTC")
    private Coin litecoin;

    @SerializedName("NEO")
    private Coin neo;

    @SerializedName("STL")
    private Coin stellar;

    public Coin getBitcoin() {
        return bitcoin;
    }

    public Coin getEthereum() {
        return ethereum;
    }

    public Coin getLitecoin() {
        return litecoin;
    }

    public Coin getNeo() {
        return neo;
    }

    public Coin getStellar() {
        return stellar;
    }
}
