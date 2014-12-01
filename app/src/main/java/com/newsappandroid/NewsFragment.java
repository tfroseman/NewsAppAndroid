package com.newsappandroid;

import android.app.Fragment;
import android.content.Intent;
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
        setHasOptionsMenu(true);

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

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.i("Hey", "Before NewsTask");
                FetchNewsTask newsTask = new FetchNewsTask("a@alk.im", "test");
                newsTask.execute();
                Log.i("Hey", "I was pressed");
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
        private String email, password;

        protected FetchNewsTask(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //TODO: Change this to token authentication when implemented
                return NetworkConnection.basicAuth(Config.API_SERVER_HOST + "account", email, password);
            } catch (IOException e) {
                //TODO: Log the exception
            }

            //TODO: Handle this case somehow.
            return null;
        }

        private void logInfo(String url, String s) {
            Log.e(url, s);
        }
    }
}

