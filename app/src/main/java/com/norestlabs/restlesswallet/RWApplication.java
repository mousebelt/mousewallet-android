package com.norestlabs.restlesswallet;

import android.app.Application;

import com.norestlabs.restlesswallet.utils.AppPreferences;

public class RWApplication extends Application {

    private static RWApplication mApp;
    private AppPreferences mPreferences;
    private String mSeed;

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
}
