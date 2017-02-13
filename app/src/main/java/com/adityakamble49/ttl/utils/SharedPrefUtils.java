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

}
