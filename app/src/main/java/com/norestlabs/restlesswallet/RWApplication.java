package com.norestlabs.restlesswallet;

import android.app.Application;

import com.norestlabs.restlesswallet.utils.AppPreferences;

import module.nrlwallet.com.nrlwalletsdk.Coins.NRLBitcoin;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLEthereum;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLLite;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLNeo;
import module.nrlwallet.com.nrlwalletsdk.Coins.NRLStellar;

public class RWApplication extends Application {

    private static RWApplication mApp;
    private AppPreferences mPreferences;
    private String mSeed;
    private byte[] mBSeed;

    private NRLBitcoin mBitcoin;
    private NRLEthereum mEthereum;
    private NRLLite mLitecoin;
    private NRLNeo mNeo;
    private NRLStellar mStellar;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;

        mPreferences = new AppPreferences(this, AppPreferences.PREFERENCE_NAME);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        mApp = null;
    }

    public static RWApplication getApp() {
        return mApp;
    }

    public AppPreferences getPreferences() {
        return mPreferences;
    }

    public String getSeed() {
        return mSeed;
    }

    public void setSeed(String seed) {
        mSeed = seed;
    }

    public byte[] getBSeed() {
        return mBSeed;
    }

    public void setBSeed(byte[] mBSeed) {
        this.mBSeed = mBSeed;
    }

    public NRLBitcoin getBitcoin() {
        return mBitcoin;
    }

    public void setBitcoin(NRLBitcoin mBitcoin) {
        this.mBitcoin = mBitcoin;
    }

    public NRLEthereum getEthereum() {
        return mEthereum;
    }

    public void setEthereum(NRLEthereum mEthereum) {
        this.mEthereum = mEthereum;
    }

    public NRLLite getLitecoin() {
        return mLitecoin;
    }

    public void setLitecoin(NRLLite mLitecoin) {
        this.mLitecoin = mLitecoin;
    }

    public NRLNeo getNeo() {
        return mNeo;
    }

    public void setNeo(NRLNeo mNeo) {
        this.mNeo = mNeo;
    }

    public NRLStellar getStellar() {
        return mStellar;
    }

    public void setStellar(NRLStellar mStellar) {
        this.mStellar = mStellar;
    }
}
