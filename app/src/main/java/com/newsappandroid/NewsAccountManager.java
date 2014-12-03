package com.newsappandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NewsAccountManager {

    public static void create(Context context, String email, String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("token", "");
    }

}
