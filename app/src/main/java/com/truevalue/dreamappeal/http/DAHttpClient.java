package com.truevalue.dreamappeal.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.BuildConfig;
import com.truevalue.dreamappeal.activity.ActivityLogin;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class DAHttpClient {

    private OkHttpClient mClient;
    private Context mContext;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static DAHttpClient instance;

    public static DAHttpClient newInstance(Context context) {
        instance = new DAHttpClient(context);
        return instance;
    }

    public static DAHttpClient getInstance(Context context) {
        instance.mContext = context;
        return instance;
    }

    public DAHttpClient(Context context) {
        mContext = context;
        mClient = new OkHttpClient();
    }

    private android.os.Handler handler = new Handler();

    public static boolean isInternet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { // 와이파이
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) { // 데이터
                return true;
            }
        }
        return false;
    }

    /**
     * 파일 전송 테스트
     *
     * @param url
     * @param fileBody
     * @param callback
     */
    public void Put(String url,File fileBody,Callback callback){

        String contents_type;

        MediaType MEDIA_TYPE_MARKDOWN = null;
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileBody);
            contents_type = fileBody.getName().split("\\.")[1];
            MEDIA_TYPE_MARKDOWN = MediaType.parse("image/" + contents_type + "; charset=utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBodyUtil.create(MEDIA_TYPE_MARKDOWN, inputStream);
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();


        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * Post Server 방식
     *
     * @param url
     * @param header
     * @param responseBody
     * @param callback
     */
    public void Post(String url, HashMap<String, String> header, HashMap<String, String> responseBody, IOServerCallback callback) {

        if(!isInternet(mContext)){
            handler.post(() -> {
                Toast.makeText(mContext.getApplicationContext(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            });
            Intent intent = new Intent(mContext, ActivityLogin.class);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
            return;
        }

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

                    if (TextUtils.equals(code, IOServerCallback.USER_NOT_FOUND)) {
                        handler.post(() -> {
                            Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        });
                        Intent intent = new Intent(mContext, ActivityLogin.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                        return;
                    }

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

        if(!isInternet(mContext)){
            handler.post(() -> {
                Toast.makeText(mContext.getApplicationContext(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            });
            Intent intent = new Intent(mContext, ActivityLogin.class);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
            return;
        }

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

                    if (TextUtils.equals(code, IOServerCallback.USER_NOT_FOUND)) {
                        handler.post(() -> {
                            Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        });
                        Intent intent = new Intent(mContext, ActivityLogin.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                        return;
                    }

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

        if(!isInternet(mContext)){
            handler.post(() -> {
                Toast.makeText(mContext.getApplicationContext(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            });
            Intent intent = new Intent(mContext, ActivityLogin.class);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
            return;
        }

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

                    if (TextUtils.equals(code, IOServerCallback.USER_NOT_FOUND)) {
                        handler.post(() -> {
                            Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        });
                        Intent intent = new Intent(mContext, ActivityLogin.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                        return;
                    }

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

        if(!isInternet(mContext)){
            handler.post(() -> {
                Toast.makeText(mContext.getApplicationContext(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            });
            Intent intent = new Intent(mContext, ActivityLogin.class);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
            return;
        }

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

                    if (TextUtils.equals(code, IOServerCallback.USER_NOT_FOUND)) {
                        handler.post(() -> {
                            Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        });
                        Intent intent = new Intent(mContext, ActivityLogin.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                        return;
                    }

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
