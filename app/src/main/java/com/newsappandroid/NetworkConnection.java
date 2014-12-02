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

    private static final String LOG_TAG = NetworkConnection.class.getSimpleName();

    private NetworkConnection(){}

    public static String basicAuth(String urlString, String username, String password) throws IOException {
        String newsJsonStr;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the api request
            // params could be more than one string.
            URL url = new URL(urlString);

            logInfo("URL", url.toString());
            logInfo("PlainAccount", username + ":" + password);
            //Encode the username and password
            String encodedCredentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);

            logInfo("Account", encodedCredentials);

            // Create the request to the server, and open the connection

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            urlConnection.setRequestMethod("GET");

            logInfo("URLConnection", urlConnection.toString());
            try {
                urlConnection.connect();
                Log.i(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Info", e);
                Log.e(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));
            }


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                newsJsonStr = null;
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
                newsJsonStr = null;
                return null;
            }
            newsJsonStr = buffer.toString();
            Log.i(LOG_TAG, newsJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the json, there's no point in attemping
            // to parse it.
            newsJsonStr = null;
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

        return newsJsonStr;
    }

    public static void logInfo(String tag, String log) {
        Log.i(tag, log);
    }
}
