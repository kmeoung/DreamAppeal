package com.truevalue.dreamappeal.base;

import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BaseOkHttpClient {

    private OkHttpClient mClient;

    public BaseOkHttpClient() {
        mClient = new OkHttpClient();
    }

    /**
     * Post Server 방식
     *
     * @param url
     * @param body
     * @param callback
     */
    public void Post(String url, HashMap<String, String> body, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();

        if (body != null && body.size() > 0) {

            for (String key : body.keySet()) {
                String value = body.get(key);
                builder.add(key, value);
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        mClient.newCall(request).enqueue(callback);
    }

    /**
     * Get Server 방식
     *
     * @param url
     * @param body
     * @param callback
     */
    public void Get(String url, HashMap<String, String> body, Callback callback) {
        String urlBody = (body != null && body.size() > 0) ? url + "?" : url;

        int i = 0;
        if (body != null && body.size() > 0) {
            for (String key : body.keySet()) {
                String value = body.get(key);
                if (i == 0) {
                    urlBody = urlBody + key + "=" + value;
                    i++;
                } else urlBody = urlBody + "&" + key + "=" + value;
            }
        }

        Request request = new Request.Builder()
                .url(urlBody)
                .get()
                .build();

        mClient.newCall(request).enqueue(callback);
    }


}
