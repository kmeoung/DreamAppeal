package com.truevalue.dreamappeal.base;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;

public interface IOServerCallback {

    String SUCCESS = "SUCCESS";
    String VALIDATE_EMAIL = "VALIDATE_EMAIL";
    String API_NOT_FOUND = "API_NOT_FOUND";
    String NOT_GRANTED = "NOT_GRANTED";
    String USER_NOT_FOUND = "USER_NOT_FOUND";
    String EMAIL_NOT_FOUND = "EMAIL_NOT_FOUND";
    String PASSWORD_NOT_MATCHED = "PASSWORD_NOT_MATCHED";
    String DUPLICATED_EMAIL = "DUPLICATED_EMAIL";

    void onFailure(@NotNull Call call, @NotNull IOException e);

    void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException;
}
