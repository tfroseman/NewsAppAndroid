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
    private User user = User.getUser();

    public FetchNewsTask(ArrayAdapter<String> adapter){
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if(user.hasToken()) {
                return NetworkConnection.tokenAuth(Config.API_SERVER_HOST + "categories", user.getApi_token());
            }else {
                return NetworkConnection.basicAuth(Config.API_SERVER_HOST + "categories", user.getEmail(), user.getPassword());
            }
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
            Log.i("USER", jsonObject.toString());
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
