package com.newsappandroid;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsFragment extends Fragment {

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] categories = {
                "Food",
                "Art",
                "Money",
                "Something"
        };

        List<String> newsCategories = new ArrayList<String>(Arrays.asList(categories));
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_categories, R.id.list_item_categories_textview, categories);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_categories);
        listView.setAdapter(categoriesAdapter);

        //NetworkConnection networkConnection = new NetworkConnection();
        //networkConnection.networkRequest("");

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchNewsTask newsTask = new FetchNewsTask();
            newsTask.execute();
            Log.i("NewsTask", newsTask.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchNewsTask extends AsyncTask<String, Integer, String> {
        private final String LOG_TAG = NewsFragment.class.getSimpleName();
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String newsJsonStr = null;
        private String server = "http://104.131.13.79/";
        private String account = "roseman.tom@gmail.com" + ":" + "1234";

        @Override
        protected String doInBackground(String... params) {
            try {
                // Construct the URL for the api request
                // params could be more than one string.
                //URL url = new URL("server" + Arrays.toString(params));
                URL url = new URL("server");

                logInfo("URL", url.toString());

                //Encode the username and password
                String encodedCredentials = Base64.encodeToString(account.getBytes(), Base64.NO_WRAP);

                logInfo("Account", encodedCredentials);

                // Create the request to the server, and open the connection

                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestProperty("Authorization", "Basic" + encodedCredentials);
                urlConnection.setRequestMethod("GET");

                logInfo("URLConnection", urlConnection.toString());
                try {
                    urlConnection.connect();
                } catch (Exception e) {
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

        private void logInfo(String url, String s) {
            Log.e(url, s);
        }
    }
}

