package com.newsappandroid;

/**
 * NetworkConnection.java
 * Just a simple network helper class. This is not threaded and simple places calls to servers
 * as needed. and then returns the response
 */

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkConnection {

    private static final String LOG_TAG = NetworkConnection.class.getSimpleName();
    private static final String ERROR_LOGIN = "LOGIN_FAILURE";

    private NetworkConnection() {
    }

    public static String basicAuth(String url, String username, String password) throws IOException {
        String encodedCredentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);

        HttpURLConnection urlConnection = urlConnection(url);
        urlConnection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        urlConnection.setRequestMethod("GET");

        return request(urlConnection);
    }

    public static String register(String url, String username, String password) throws IOException {
        HttpURLConnection urlConnection = urlConnection(url + "/" + username + "/" + password);
        urlConnection.setRequestMethod("GET");

        return request(urlConnection);
    }

    public static String tokenAuth(String url, String token) throws IOException {
        HttpURLConnection urlConnection = urlConnection(url);
        urlConnection.setRequestProperty("X-Auth-Token", token);
        urlConnection.setRequestMethod("GET");

        return request(urlConnection);
    }

    private static HttpURLConnection urlConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

    private static String request(HttpURLConnection urlConnection) {
        String jsonStr;
        BufferedReader reader = null;

        try {
            try {
                urlConnection.connect();
                Log.i(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));

            } catch (Exception e) {
                Log.e(LOG_TAG, "Info", e);
            }


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                jsonStr = null;
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                jsonStr = null;
                return null;
            }
            jsonStr = buffer.toString();
            //Log.i(LOG_TAG, jsonStr);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return jsonStr;
    }

}
