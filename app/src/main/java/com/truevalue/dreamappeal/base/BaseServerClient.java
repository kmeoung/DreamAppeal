package com.truevalue.dreamappeal.base;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BaseServerClient {

    private OkHttpClient mClient;

    public BaseServerClient(){
        mClient = new OkHttpClient();
    }

    public void Get(String url, HashMap header){
//        try {
//            OkHttpClient client = new OkHttpClient();
//
//            Request.Builder builder = new Request.Builder();
//
//            Request request = new Request.Builder()
//                    .addHeader("Authorization", "TEST AUTH")
//                    .url(url)
//                    .build();
//            Response response = client.newCall(request)
//                    .execute();
//
//            String result = response.body().string();
//
//            Gson gson = new Gson();
//            UserInfo info = gson.fromJson(result, UserInfo.class);
//
//            Log.i("id: " + info.id);
//            Log.i("name: " + info.name);
//
//            return true;
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }

}
