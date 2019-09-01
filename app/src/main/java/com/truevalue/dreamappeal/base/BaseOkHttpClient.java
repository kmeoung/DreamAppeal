package com.truevalue.dreamappeal.base;

import android.os.Handler;
import android.util.Log;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.RequestBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaseOkHttpClient {

    private OkHttpClient mClient;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public BaseOkHttpClient() {
        mClient = new OkHttpClient();
    }

    private android.os.Handler handler = new Handler();

    /**
     * Post Server 방식
     *
     * @param url
     * @param header
     * @param responseBody
     * @param callback
     */
    public void Post(String url, HashMap<String, String> header, HashMap<String, String> responseBody, IOServerCallback callback) {
        RequestBody body = RequestBody.create(new JSONObject().toString(), JSON);
        if (BuildConfig.DEBUG) Log.d("SERVER POST URL", url);
        if (responseBody != null && responseBody.size() > 0) {
            JSONObject object = new JSONObject();
            for (String key : responseBody.keySet()) {
                String value = responseBody.get(key);
                try {
                    object.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (BuildConfig.DEBUG) Log.d("SERVER POST BODY", object.toString());
            body = RequestBody.create(object.toString(), JSON);
        }

        Request request;
        if (header != null && header.size() > 0) {
            JSONObject object = new JSONObject();

            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder = requestBuilder.url(url);
            requestBuilder = requestBuilder.post(body);

            for (String key : header.keySet()) {
                requestBuilder = requestBuilder.addHeader(key, header.get(key));
                try {
                    object.put(key, header.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (BuildConfig.DEBUG) Log.d("SERVER POST HEADER", object.toString());

            request = requestBuilder.build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
        Call call = mClient.newCall(request);
        if (BuildConfig.DEBUG) Log.d("SERVER REQUEST", call.request().toString());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int serverCode = response.code();
                String body = response.body().string();
                if (BuildConfig.DEBUG) Log.d("SERVER POST CODE", serverCode + "");
                if (BuildConfig.DEBUG) Log.d("SERVER POST RESPONSE", body);
                try {
                    JSONObject object = new JSONObject(body);
                    String code = object.getString("code");
                    String message = object.getString("message");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onResponse(call, serverCode, body, code, message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

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
     * @param header
     * @param body
     * @param callback
     */
    public void Get(String url, HashMap<String, String> header, HashMap<String, String> body, IOServerCallback callback) {
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
        if (BuildConfig.DEBUG) Log.d("SERVER GET URL", urlBody);
        Request request;
        if (header != null && header.size() > 0) {
            JSONObject object = new JSONObject();
            Request.Builder builder = new Request.Builder();
            builder = builder.url(urlBody);
            builder = builder.get();

            for (String key : header.keySet()) {
                builder = builder.addHeader(key, header.get(key));
                try {
                    object.put(key, header.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (BuildConfig.DEBUG) Log.d("SERVER GET HEADER", object.toString());
            request = builder.build();
        } else {
            request = new Request.Builder()
                    .url(urlBody)
                    .get()
                    .build();
        }
        Call call = mClient.newCall(request);
        if (BuildConfig.DEBUG) Log.d("SERVER REQUEST", call.request().toString());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int serverCode = response.code();
                String body = response.body().string();
                if (BuildConfig.DEBUG) Log.d("SERVER GET CODE", serverCode + "");
                if (BuildConfig.DEBUG) Log.d("SERVER GET RESPONSE", body);
                try {
                    JSONObject object = new JSONObject(body);
                    String code = object.getString("code");
                    String message = object.getString("message");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onResponse(call, serverCode, body, code, message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Patch Server 방식
     *
     * @param url
     * @param header
     * @param responseBody
     * @param callback
     */
    public void Patch(String url, HashMap<String, String> header, HashMap<String, String> responseBody, IOServerCallback callback) {
        RequestBody body = RequestBody.create(new JSONObject().toString(), JSON);
        if (BuildConfig.DEBUG) Log.d("SERVER PATCH URL", url);
        if (responseBody != null && responseBody.size() > 0) {
            JSONObject object = new JSONObject();
            for (String key : responseBody.keySet()) {
                String value = responseBody.get(key);
                try {
                    object.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (BuildConfig.DEBUG) Log.d("SERVER PATCH BODY", object.toString());
            body = RequestBody.create(object.toString(), JSON);
        }

        Request request;
        if (header != null && header.size() > 0) {
            JSONObject object = new JSONObject();

            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder = requestBuilder.url(url);
            requestBuilder = requestBuilder.patch(body);

            for (String key : header.keySet()) {
                requestBuilder = requestBuilder.addHeader(key, header.get(key));
                try {
                    object.put(key, header.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (BuildConfig.DEBUG) Log.d("SERVER PATCH HEADER", object.toString());

            request = requestBuilder.build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .patch(body)
                    .build();
        }
        Call call = mClient.newCall(request);
        if (BuildConfig.DEBUG) Log.d("SERVER REQUEST", call.request().toString());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int serverCode = response.code();
                String body = response.body().string();
                if (BuildConfig.DEBUG) Log.d("SERVER PATCH CODE", serverCode + "");
                if (BuildConfig.DEBUG) Log.d("SERVER PATCH RESPONSE", body);
                try {
                    JSONObject object = new JSONObject(body);
                    String code = object.getString("code");
                    String message = object.getString("message");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onResponse(call, serverCode, body, code, message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Delete Server 방식
     *
     * @param url
     * @param header
     * @param responseBody
     * @param callback
     */
    public void Delete(String url, HashMap<String, String> header, HashMap<String, String> responseBody, IOServerCallback callback) {
        RequestBody body = RequestBody.create(new JSONObject().toString(), JSON);
        if (BuildConfig.DEBUG) Log.d("SERVER DELETE URL", url);
        if (responseBody != null && responseBody.size() > 0) {
            JSONObject object = new JSONObject();
            for (String key : responseBody.keySet()) {
                String value = responseBody.get(key);
                try {
                    object.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (BuildConfig.DEBUG) Log.d("SERVER DELETE BODY", object.toString());
            body = RequestBody.create(object.toString(), JSON);
        }

        Request request;
        if (header != null && header.size() > 0) {
            JSONObject object = new JSONObject();

            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder = requestBuilder.url(url);
            requestBuilder = requestBuilder.delete(body);

            for (String key : header.keySet()) {
                requestBuilder = requestBuilder.addHeader(key, header.get(key));
                try {
                    object.put(key, header.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (BuildConfig.DEBUG) Log.d("SERVER DELETE HEADER", object.toString());

            request = requestBuilder.build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .build();
        }
        Call call = mClient.newCall(request);
        if (BuildConfig.DEBUG) Log.d("SERVER REQUEST", call.request().toString());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int serverCode = response.code();
                String body = response.body().string();
                if (BuildConfig.DEBUG) Log.d("SERVER DELETE CODE", serverCode + "");
                if (BuildConfig.DEBUG) Log.d("SERVER DELETE RESPONSE", body);
                try {
                    JSONObject object = new JSONObject(body);
                    String code = object.getString("code");
                    String message = object.getString("message");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onResponse(call, serverCode, body, code, message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
