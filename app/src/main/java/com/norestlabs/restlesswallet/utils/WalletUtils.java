package com.norestlabs.restlesswallet.utils;

import com.norestlabs.restlesswallet.RWApplication;

import module.nrlwallet.com.nrlwalletsdk.Coins.NRLBitcoin;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLEthereum;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLLite;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLNeo;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLStellar;
import module.nrlwallet.com.nrlwalletsdk.abstracts.NRLCallback;

public class WalletUtils {

    public static void getBitcoinWallet(byte[] bSeed, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLBitcoin nrlBitcoin = new NRLBitcoin(bSeed, null);
        nrlBitcoin.getBalance();
    }

    public static void getEthereumWallet(byte[] bSeed, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLEthereum nrlEthereum = new NRLEthereum(bSeed);
        nrlEthereum.getBalance(bCallback);
        nrlEthereum.getTransactions(tCallback);
        RWApplication.getApp().setEthereum(nrlEthereum);
    }

    public static void getLitecoinWallet(byte[] bSeed, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLLite nrlLitecoin = new NRLLite(bSeed);
//        nrlLitecoin.getBalance();
    }

    public static void getNeoWallet(byte[] bSeed, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLNeo nrlNeo = new NRLNeo(bSeed);
        nrlNeo.getBalance(bCallback);
        nrlNeo.getTransactions(tCallback);
        RWApplication.getApp().setNeo(nrlNeo);
    }

    public static void getStellarWallet(byte[] bSeed, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLStellar nrlStellar = new NRLStellar(bSeed);
        nrlStellar.getBalance(bCallback);
        nrlStellar.getTransactions(tCallback);
        RWApplication.getApp().setStellar(nrlStellar);
    }
}
