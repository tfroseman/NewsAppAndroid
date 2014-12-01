package com.newsappandroid;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkConnection {
    Categories categories;
    Article article;

    public static String basicAuth(String urlString, String username, String password) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String newsJsonStr = null;


        // Construct the URL for the api request
        // params could be more than one string.
        URL url = new URL(urlString);

        //Encode the username and password
        String encodedCredentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);

        // Create the request to the server, and open the connection
        urlConnection = (HttpURLConnection) url.openConnection();
        //urlConnection.setRequestProperty("Authorization", "Basic" + encodedCredentials);
        urlConnection.setRequestMethod("GET");

        urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();

        if (inputStream == null) {
            urlConnection.disconnect();
            return null;
        }

        reader = new BufferedReader(new InputStreamReader(inputStream));

        if(reader == null) {
            return null;
        }

        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }

        reader.close();
        urlConnection.disconnect();

        if (buffer.length() == 0) {
            return null;
        }

        newsJsonStr = buffer.toString();

        return newsJsonStr;
    }

    public Categories getCategories() {
        return null;
    }

    public void logInfo(String tag, String log) {
        Log.i(tag, log);
    }

    @Override
    public String toString() {
        return "NetworkConnection{" +
                "server='" + Config.API_SERVER_HOST + '\'' +
                ", categories=" + categories +
                ", article=" + article +
                '}';
    }
}
