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

import com.newsappandroid.ArticleViewActivity;
import com.newsappandroid.Config;
import com.newsappandroid.NetworkConnection;
import com.newsappandroid.R;
import com.newsappandroid.model.ArticleList;
import com.newsappandroid.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ArticleListFragment extends Fragment {
    ArrayList<String> lst;
    private ArrayAdapter<String> articlesAdapter;
    private ListView listView;
    private ArrayList<ArticleList> articleLists = new ArrayList<ArticleList>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final Context context = getActivity().getApplicationContext();
        String[] articles = {"Hey"}; // Need to populate the adapter with a blank array

        lst = new ArrayList<String>(); // Make arrayList so that the listView can be updated as needed
        lst.addAll(Arrays.asList(articles));

        View rootView = inflater.inflate(R.layout.activity_article_list, container, false);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras(); // Intent carries any info from the previous activity

       FetchJsonTask jsonTask = new FetchJsonTask();
       jsonTask.execute("article/category/" + extras.get(CategoryFragment.ARTICLE) + "/1");

        articlesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_categories, R.id.list_item_categories_textview, lst);


        //

        // Update the listview with the blank array for temp
        listView = (ListView) rootView.findViewById(R.id.listview_articles);
        listView.setAdapter(articlesAdapter);


        // Allows on to select an item from a list view and open a new activity based on that id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ArticleViewActivity.class);
                intent.putExtra("URL", articleLists.get(position++).getUrl());
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_article_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchJsonTask extends AsyncTask<String, Integer, String> {
        /**
         * JSON objected that was collected from the server as a string
         */
        protected JSONObject jsonObject = null;
        protected JSONArray jsonArray = null;
        private final String LOG_TAG = ArticleListFragment.class.getSimpleName();
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
            try {
                jsonArray = new JSONArray(result); // Array is needed as objects are not returned
            } catch (Throwable t) {
                Log.e("JSON ERROR", "Could not parse malformed JSON");
            }

            lst.clear(); // Clear the adapter container so that it can have values added
            Log.i("Array Length",String.valueOf(jsonArray.length()));

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i); // Grab each object from the array
                    lst.add(jsonObject.getString("title")); // Insert the name value from the jsonObject

                    articleLists.add(new ArticleList(jsonObject.getString("id"),jsonObject.getString("title"),jsonObject.getString("description"),jsonObject.getString("link")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            articlesAdapter.notifyDataSetChanged(); // Notify the adapter that the information has changed
        }
    }
}
