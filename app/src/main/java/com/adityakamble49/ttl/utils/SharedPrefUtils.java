package com.adityakamble49.ttl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    public static void putStringInPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromPreferences(Context context, String key, String
            defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        String value = sharedPreferences.getString(key, defaultValue);
        return value;
    }

    public static void putIntegerInPreferences(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntegerFromPreferences(Context context, String key, int
            defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, defaultValue);
        return value;
    }

    public static void putLongInPreferences(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLongFromPreferences(Context context, String key, long
            defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        long value = sharedPreferences.getLong(key, defaultValue);
        return value;
    }

}
