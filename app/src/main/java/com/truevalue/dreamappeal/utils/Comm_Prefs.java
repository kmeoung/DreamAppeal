package com.truevalue.dreamappeal.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.truevalue.dreamappeal.R;

import static android.content.Context.MODE_PRIVATE;

public class Comm_Prefs {
    private static Comm_Prefs mInstance;
    private SharedPreferences pref;

    private Context mContext;

    public Comm_Prefs(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(Comm_Param.APP_NAME, MODE_PRIVATE);
    }

    public static Comm_Prefs getInstance(Context context){
        if(mInstance == null){
            mInstance = new Comm_Prefs(context);
        }
        return mInstance;
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

    public void setToken(String token){
        pref.edit().putString(Comm_Prefs_Param.PREFS_USER_TOKEN,token).commit();
    }

    public String getToken(){
        return pref.getString(Comm_Prefs_Param.PREFS_USER_TOKEN,null);
    }

    public void setProfileIndex(int profileIndex,boolean isMy){
        if(isMy) setMyProfileIndex(profileIndex);
        pref.edit().putInt(Comm_Prefs_Param.PREFS_PROFILE_INDEX,profileIndex).commit();
    }

    public int getProfileIndex(){
        return pref.getInt(Comm_Prefs_Param.PREFS_PROFILE_INDEX,-1);
    }

    public void setMyProfileIndex(int profileIndex){

        pref.edit().putInt(Comm_Prefs_Param.PREFS_MY_PROFILE_INDEX,profileIndex).commit();
    }

    public int getMyProfileIndex(){
        return pref.getInt(Comm_Prefs_Param.PREFS_MY_PROFILE_INDEX,-1);
    }

    public void setDreamListIndex(int dream_list_index){
        pref.edit().putInt(Comm_Prefs_Param.PREFS_DREAM_LIST_INDEX,dream_list_index).commit();
    }

    public int getDreamListIndex(){
        return pref.getInt(Comm_Prefs_Param.PREFS_DREAM_LIST_INDEX,-1);
    }



}
