package com.newsappandroid;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

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
    protected String newsJsonStr = null;
    protected ArrayAdapter<String> categoriesAdapter;
    protected ListView listView;
    protected JSONObject obj = null;

    public NewsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FetchNewsTask newsTask = new FetchNewsTask();
        newsTask.execute();





        // Tells the android system the activity has
        // a menu as described in onCreateOptionsMenu()
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Testing listItem
        String[] categories = {
                "Food",
                "Art",
                "Money",
                "Something"
        };

        //List<String> newsCategories = new ArrayList<String>(Arrays.asList(categories));

        categoriesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_categories, R.id.list_item_categories_textview, categories);

        listView = (ListView) rootView.findViewById(R.id.listview_categories);
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

        switch (item.getItemId()) {
            case R.id.action_refresh:
                FetchNewsTask newsTask = new FetchNewsTask();
                newsTask.execute();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_news, menu);
    }

    public class FetchNewsTask extends AsyncTask<String, Integer, String> {
        private final String LOG_TAG = NewsFragment.class.getSimpleName();
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

////////////////////////////////////////////////////////////////
        private String server = "http://104.131.109.166/account";
        private String account = "a@alk.im" + ":" + "test";
////////////////////////////////////////////////////////////////

        @Override
        protected String doInBackground(String... params) {
            try {
                // Construct the URL for the api request
                // params could be more than one string.
                //URL url = new URL("server" + Arrays.toString(params));
                URL url = new URL(server);

                logInfo("URL", url.toString());
                logInfo("PlainAccount", account);
                //Encode the username and password
                String encodedCredentials = Base64.encodeToString(account.getBytes(), Base64.NO_WRAP);

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

            /*try {
                newsJsonObject = new JSONObject(newsJsonStr);
                //return newsJsonObject;

            } catch (JSONException e) {
                e.printStackTrace();
            }*/


            return newsJsonStr;
        }

        @Override
        protected void onPostExecute(String result){
            try {
                obj = new JSONObject(newsJsonStr);
                Log.i("My App", obj.toString());
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + newsJsonStr + "\"");
            }

            if (obj != null) {
                try {
                    Log.i("USER",obj.getString("user"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.i("NULL", "Obj is null need more time");
            }


            categoriesAdapter.notifyDataSetChanged();
        }

        private void logInfo(String url, String s) {
            Log.e(url, s);
        }
    }


}

