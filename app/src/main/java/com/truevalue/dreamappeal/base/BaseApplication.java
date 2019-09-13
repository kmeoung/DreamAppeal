package com.truevalue.dreamappeal.base;

import android.app.Application;

import com.truevalue.dreamappeal.http.DreamAppealHttpClient;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DreamAppealHttpClient.newInstance(getApplicationContext());
    }

}
