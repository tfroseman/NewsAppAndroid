package com.newsappandroid.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.newsappandroid.Config;
import com.newsappandroid.NetworkConnection;
import com.newsappandroid.R;
import com.newsappandroid.activities.ArticleListActivity;
import com.newsappandroid.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * CategoryFragment.java
 * Is launched by CategoryActivity but is only the fragment so that it can be
 * reused if needed.
 */

public class CategoryFragment extends Fragment {
    private ArrayAdapter<String> categoriesAdapter;
    private ListView listView;
    ArrayList<String> lst;


    public CategoryFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tells the android system the activity has
        // a menu as described in onCreateOptionsMenu()
        setHasOptionsMenu(true);
        final Context context = getActivity().getApplicationContext();

        String[] categories = {""};

        lst = new ArrayList<String>();
        lst.addAll(Arrays.asList(categories));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /*
         * We will need to pass the categories adapter an empty String array instead and then add them
         * once the FetchNewsTask has completed
         */


        FetchJsonTask jsonTask = new FetchJsonTask();
        jsonTask.execute("category");

        categoriesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_categories, R.id.list_item_categories_textview, lst);

        //Update the categoriesAdapter with the categories or do it inside of the FetchNewsTask
        listView = (ListView) rootView.findViewById(R.id.listview_categories);
        listView.setAdapter(categoriesAdapter);


        // Allows on to select an item from a list view and open a new activity based on that id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ArticleListActivity.class);
                intent.putExtra("ARTICLE_ID", position);
                startActivity(intent);
            }
        });

        //List<String> newsCategories = new ArrayList<String>(Arrays.asList(categories));

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_refresh:
                FetchJsonTask jsonTask = new FetchJsonTask();
                jsonTask.execute("category");

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

    public class FetchJsonTask extends AsyncTask<String, Integer, String> {
        /**
         * JSON objected that was collected from the server as a string
         */
        public JSONObject jsonObject = null;
        protected JSONArray jsonArray = null;
        private ArrayAdapter<String> adapter = null;
        private final String LOG_TAG = CategoryFragment.class.getSimpleName();
        private User user = User.getUser();

        public FetchJsonTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (user.hasToken()) {
                    return NetworkConnection.tokenAuth(Config.API_SERVER_HOST + params[0], user.getApi_token());
                } else {
                    return NetworkConnection.basicAuth(Config.API_SERVER_HOST + params[0], user.getEmail(), user.getPassword());
                }
            } catch (IOException e) {
                Log.i(LOG_TAG, "Error fetching: " + e);
            }
            return "ERROR";
        }

        /**
         * Called after the task has completed
         */
        @Override
        protected void onPostExecute(String result) {
            String[] categories;
            try {
                Log.i(LOG_TAG, result);
                //jsonObject = new JSONObject(result);
                jsonArray = new JSONArray(result);
            } catch (Throwable t) {
                //Log.e("My App", "Could not parse malformed JSON: \"" + newsJsonStr + "\"");
            }


            if (jsonArray != null) {
                Log.i("Array", jsonArray.toString());
            } else {
                Log.i("NULL", "Obj is null need more time");
            }
            lst.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    lst.add(jsonObject.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            categoriesAdapter.notifyDataSetChanged();

        }

    }
}

