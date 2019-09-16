package com.truevalue.dreamappeal.base;

import android.app.Application;

import com.truevalue.dreamappeal.http.DAHttpClient;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DAHttpClient.newInstance(getApplicationContext());
    }

}
