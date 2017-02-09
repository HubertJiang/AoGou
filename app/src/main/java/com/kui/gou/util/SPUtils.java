package com.kui.gou.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

    public static final String SP_NAME = "AoGou";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String TOKEN = "token";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String UPDATE_TIME = "updateTime";
    public static final String NOTICE_COUNT = "noticeCount";
    public static final String LEVEL = "level";
    public static final String USER = "user";
    public static final String CONFIG = "config";
    public static final String UPDATE = "update";
    public static final String NOTICE = "notice";
    public static final String SHARE_ID = "shareId";
    public static final String OLD_TIME = "oldTime";

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getString(key, null);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putString(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getLong(key, 0);
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putLong(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getInt(key, 0);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }



}
