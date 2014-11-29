package com.newsappandroid;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class NetworkConnection {
    private String server = "http://104.131.13.79/";
    private String account = "roseman.tom@gmail.com" + ":" + "1234";
    Categories categories;
    Article article;

    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Will contain the raw JSON response as a string.
    String newsJsonStr = null;

    protected void networkRequest(String uri){
        try{
            // Construct the URL for the api request
            URL url = new URL(server+uri);

            logInfo("URL", url.toString());

            //Encode the username and password
            String encodedCredentials = Base64.encodeToString(account.getBytes(),Base64.NO_WRAP);

            logInfo("Account", encodedCredentials);

            // Create the request to the server, and open the connection

            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestProperty("Authorization", "Basic" + encodedCredentials);
            urlConnection.setRequestMethod("GET");

            logInfo("URLConnection",urlConnection.toString());
            try {
                urlConnection.connect();
            }catch (Exception e){
                Log.i("Exception", e.toString());
            }


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                newsJsonStr = null;
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
            }
            newsJsonStr = buffer.toString();
            Log.i("JSON", newsJsonStr.toString());
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the json, there's no point in attemping
            // to parse it.
            newsJsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    public Categories getCategories() {
       return null;
    }

    public void logInfo(String tag, String log){
        Log.i(tag,log);
    }

    public class NewsTask extends AsyncTask<String, Integer, String>{
        private final String LOG_TAG = NewsTask.class.getSimpleName();
        @Override
        protected String doInBackground(String... params) {
            try{
                // Construct the URL for the api request
                // params could be more than one string.
                URL url = new URL(server+ Arrays.toString(params));

                logInfo("URL", url.toString());

                //Encode the username and password
                String encodedCredentials = Base64.encodeToString(account.getBytes(),Base64.NO_WRAP);

                logInfo("Account", encodedCredentials);

                // Create the request to the server, and open the connection

                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestProperty("Authorization", "Basic" + encodedCredentials);
                urlConnection.setRequestMethod("GET");

                logInfo("URLConnection",urlConnection.toString());
                try {
                    urlConnection.connect();
                }catch (Exception e){
                    Log.e(LOG_TAG, "Info", e);
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
            } finally{
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
    }

    @Override
    public String toString() {
        return "NetworkConnection{" +
                "server='" + server + '\'' +
                ", account='" + account + '\'' +
                ", categories=" + categories +
                ", article=" + article +
                ", urlConnection=" + urlConnection +
                ", reader=" + reader +
                ", newsJsonStr='" + newsJsonStr + '\'' +
                '}';
    }
}
