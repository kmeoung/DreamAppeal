package com.truevalue.dreamappeal.base;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;

public interface IOServerCallback {

    String DAOK = "DAOK";
    String DANO = "DANO";

    void onFailure(@NotNull Call call, @NotNull IOException e);

    void onResponse(@NotNull Call call, int serverCode, String body, String RtnKey, String RtnValue) throws IOException;
}
