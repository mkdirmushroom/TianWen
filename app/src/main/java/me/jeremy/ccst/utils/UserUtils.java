package me.jeremy.ccst.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import me.jeremy.ccst.App;
import me.jeremy.ccst.model.user.UserInfoResponse;

/**
 * Created by qiugang on 2014/9/28.
 */
public class UserUtils {


    public static void saveUserInfo(UserInfoResponse userInfoResponse) {
        SharedPreferences sp = App.getContext().getSharedPreferences("user",0);
        sp.edit().putString("userInfo", new Gson().toJson(userInfoResponse))
                .putInt("id", userInfoResponse.getId())
                .putString("username", userInfoResponse.getUserName())
                .apply();
    }

    public static UserInfoResponse getUserInfoResponse() {
        SharedPreferences sp = App.getContext().getSharedPreferences("user", 0);
        return new Gson().fromJson(sp.getString("userInfo", null), UserInfoResponse.class);
    }

    public static SharedPreferences getUserSharedPreference() {
        return App.getContext().getSharedPreferences("user",0);
    }

    public static boolean logined(Context context) {
        String userName;
        userName = App.getContext().getSharedPreferences("user", 0).getString("username", null);
        if (userName != null) {
            return true;
        } else {
            return false;
        }
    }

    public static Integer getUserId() {
        return App.getContext().getSharedPreferences("user", 0).getInt("id", 0);
    }

    public static String getUserName() {
        return App.getContext().getSharedPreferences("user", 0).getString("username", null);
    }

    public static boolean isHaveToast() {
        int flag =  App.getContext().getSharedPreferences("user", 0).getInt("haveOpen", 0);
        if (flag == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void cacheData(String type, String data) {
        SharedPreferences sp = App.getContext().getSharedPreferences("user", 0);
        sp.edit().putString(type, data).apply();
    }

    public static String getCache(String type) {
       return  App.getContext().getSharedPreferences("user", 0).getString(type, null);
    }

}
