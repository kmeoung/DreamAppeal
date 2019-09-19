package com.truevalue.dreamappeal.base;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.truevalue.dreamappeal.http.DAHttpClient;

import io.fabric.sdk.android.Fabric;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        DAHttpClient.newInstance(getApplicationContext());
    }

}
