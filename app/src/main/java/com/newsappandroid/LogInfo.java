package com.newsappandroid;

import android.content.Intent;
import android.util.Log;

public class LogInfo {
    public LogInfo() {
    }

    public void logUser(User user){
        Log.i("User Info", user.toString());
    }

    public void logArticle(Article article){
        Log.i("Article", article.toString());
    }

    public void logCategories(Categories categories){
        Log.i("Categories", categories.toString());
    }

    public void logNetwork(NetworkConnection networkConnection){
        Log.i("Network Connection", networkConnection.toString());
    }

    public void logIntent(Intent intent){
        Log.i("Intent", intent.toString());
    }


}
