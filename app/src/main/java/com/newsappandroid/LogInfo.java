package com.newsappandroid;

/**
 * LogInfo.java
 * Made to help make logging Info to logcat much easier.
 */

import android.content.Intent;
import android.util.Log;

import com.newsappandroid.model.Article;
import com.newsappandroid.model.Categories;
import com.newsappandroid.model.User;

public class LogInfo {

    public LogInfo() {
    }

    public static void logUser(User user){
        Log.i("User Info", user.toString());
    }

    public static void logArticle(Article article){
        Log.i("Article", article.toString());
    }

    public static void logCategories(Categories categories){
        Log.i("Categories", categories.toString());
    }

    public static void logNetwork(NetworkConnection networkConnection){
        Log.i("Network Connection", networkConnection.toString());
    }

    public static void logIntent(Intent intent){
        Log.i("Intent", intent.toString());
    }


}
