package com.norestlabs.restlesswallet.utils;

import com.norestlabs.restlesswallet.models.Coin;
import com.norestlabs.restlesswallet.models.CoinMarketCap;
import com.norestlabs.restlesswallet.models.wallet.Transaction;

import java.util.List;

public class Global {

    public static List<CoinMarketCap> marketInfo;
    public static List<Coin> swapCoins;

    public static double btcBalance, ethBalance, ltcBalance, neoBalance, stlBalance;

    public static List<Transaction> btcTransactions, ethTransactions, ltcTransactions, neoTransactions, stlTransactions;
}
