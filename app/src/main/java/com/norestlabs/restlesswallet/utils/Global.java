package com.norestlabs.restlesswallet.utils;

import com.norestlabs.restlesswallet.models.Coin;
import com.norestlabs.restlesswallet.models.wallet.EthereumTransaction;
import com.norestlabs.restlesswallet.models.wallet.LitecoinTransaction;
import com.norestlabs.restlesswallet.models.wallet.NeoTransaction;
import com.norestlabs.restlesswallet.models.wallet.StellarTransaction;
import com.norestlabs.restlesswallet.models.wallet.Transaction;

import java.util.List;

public class Global {

    public static List<Coin> swapCoins;

    public static double btcBalance, ethBalance, ltcBalance, neoBalance, stlBalance;

    public static List<Transaction> btcTransactions;
    public static List<EthereumTransaction> ethTransactions;
    public static List<LitecoinTransaction> ltcTransactions;
    public static List<NeoTransaction> neoTransactions;
    public static List<StellarTransaction> stlTransactions;
}
