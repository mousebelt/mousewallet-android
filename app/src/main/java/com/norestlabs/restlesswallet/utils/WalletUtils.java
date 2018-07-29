package com.norestlabs.restlesswallet.utils;

import android.content.Context;

import com.norestlabs.restlesswallet.RWApplication;

import module.nrlwallet.com.nrlwalletsdk.Coins.NRLBitcoin;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLEthereum;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLLite;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLNeo;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLStellar;
import module.nrlwallet.com.nrlwalletsdk.abstracts.LTCCallback;
import module.nrlwallet.com.nrlwalletsdk.abstracts.NRLCallback;

public class WalletUtils {

    public static String getBitcoinWallet(byte[] bSeed, String mnemonic, boolean exist, NRLCallback tCallback) {
        final NRLBitcoin nrlBitcoin = new NRLBitcoin(bSeed, mnemonic);
        nrlBitcoin.getTransactions(tCallback);
        RWApplication.getApp().setBitcoin(nrlBitcoin);
        return nrlBitcoin.getBalance();
    }

    public static void getEthereumWallet(byte[] bSeed, String mnemonic, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLEthereum nrlEthereum = new NRLEthereum(bSeed, mnemonic);
        nrlEthereum.getBalance(bCallback);
        nrlEthereum.getTransactions(tCallback);
        RWApplication.getApp().setEthereum(nrlEthereum);
    }

    public static void getLitecoinWallet(byte[] bSeed, String mnemonic, boolean isExist, Context context, LTCCallback callback) {
        final NRLLite nrlLite = new NRLLite(bSeed, mnemonic, context, callback, isExist);
        RWApplication.getApp().setLitecoin(nrlLite);
    }

    public static void getNeoWallet(byte[] bSeed, String mnemonic, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLNeo nrlNeo = new NRLNeo(bSeed, mnemonic);
        nrlNeo.getBalance(bCallback);
        nrlNeo.getTransactions(tCallback);
        RWApplication.getApp().setNeo(nrlNeo);
    }

    public static void getStellarWallet(byte[] bSeed, NRLCallback bCallback, NRLCallback tCallback) {
        final NRLStellar nrlStellar = new NRLStellar(bSeed, RWApplication.getApp().getSeed());
        nrlStellar.getBalance(bCallback);
        nrlStellar.getTransactions(tCallback);
        RWApplication.getApp().setStellar(nrlStellar);
    }
}
