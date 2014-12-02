package com.newsappandroid;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchNewsTask extends AsyncTask<String, Integer, String> {
    /**
     * JSON objected that was collected from the server as a string
     */
    protected JSONObject jsonObject = null;

    private ArrayAdapter<String> adapter = null;
    private final String LOG_TAG = NewsFragment.class.getSimpleName();

    private String email, password;

    public FetchNewsTask(String email, String password, ArrayAdapter<String> adapter){
        this.email = email;
        this.password = password;
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            //TODO: Change this to tokenAuth once implemented
            return NetworkConnection.basicAuth(Config.API_SERVER_HOST + "account", email, password);
        } catch (IOException e) {
            logInfo(LOG_TAG, "Error fetching: " + e);
        }

        return null;
    }


    /**
     * Called after the task has completed
     */
    @Override
    protected void onPostExecute(String result) {
        try {
            jsonObject = new JSONObject(result);
        } catch (Throwable t) {
            //Log.e("My App", "Could not parse malformed JSON: \"" + newsJsonStr + "\"");
        }

        if (jsonObject != null) {
            try {
                Log.i("USER", jsonObject.getString("user"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("NULL", "Obj is null need more time");
        }

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void logInfo(String url, String s) {
        Log.e(url, s);
    }
}
