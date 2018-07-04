package com.norestlabs.restlesswallet.utils;

import com.norestlabs.restlesswallet.models.Coin;
import com.norestlabs.restlesswallet.models.wallet.NeoTransaction;

import java.util.List;

public class Global {

    public static List<Coin> swapCoins;

    public static double btcBalance, ethBalance, ltcBalance, neoBalance, stlBalance;

    public static List<NeoTransaction> neoTransactions;
}
