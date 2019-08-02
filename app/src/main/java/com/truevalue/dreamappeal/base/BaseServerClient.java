package com.truevalue.dreamappeal.base;

import okhttp3.OkHttpClient;

public class BaseServerClient {

    private OkHttpClient mClient;

    public BaseServerClient(){
        mClient = new OkHttpClient();
    }


}
