package com.truevalue.dreamappeal.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.truevalue.dreamappeal.R;

import static android.content.Context.MODE_PRIVATE;

public class Comm_Prefs {
    SharedPreferences pref;

    private Context mContext;

    public Comm_Prefs(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(Comm_Param.APP_NAME, MODE_PRIVATE);
    }

    public void setLogined(boolean isLogin) {
        pref.edit().putBoolean(Comm_Prefs_Param.PREFS_IS_LOGIN, isLogin).commit();
    }

    public boolean isLogin() {
        return pref.getBoolean(Comm_Prefs_Param.PREFS_IS_LOGIN, false);
    }

    public void setUserName(String name){
        pref.edit().putString(Comm_Prefs_Param.PREFS_USER_NAME,name).commit();
    }

    public String getUserName(){
        return pref.getString(Comm_Prefs_Param.PREFS_USER_NAME,null);
    }

}
