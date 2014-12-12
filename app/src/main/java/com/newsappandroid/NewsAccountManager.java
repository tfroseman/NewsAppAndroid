package com.newsappandroid;

/**
 * NewsAccountManager.java
 * Stores account in application preferences within android
 * Will need to move to account manager from system but will need
 * more time to add in that feature
 */

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
