package com.truevalue.dreamappeal.base;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    public void Post(String url, HashMap<String, String> body, IOServerCallback callback) {
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

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                String body = response.body().string();
                try {
                    JSONObject object = new JSONObject(body);
                    String RtnKey = object.getString("RtnKey");
                    String RtnValue = object.getString("RtnValue");
                    callback.onResponse(call, code, body, RtnKey, RtnValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Get Server 방식
     *
     * @param url
     * @param body
     * @param callback
     */
    public void Get(String url, HashMap<String, String> body, IOServerCallback callback) {
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

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                String body = response.body().string();
                try {
                    JSONObject object = new JSONObject(body);
                    String RtnKey = object.getString("RtnKey");
                    String RtnValue = object.getString("RtnValue");
                    callback.onResponse(call, code, body, RtnKey, RtnValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
