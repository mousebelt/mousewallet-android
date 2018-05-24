package com.norestlabs.restlesswallet;

import android.app.Application;

public class RWApplication extends Application {

    private static RWApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        mApp = null;
    }

    public static RWApplication getApp() {
        return mApp;
    }
}
