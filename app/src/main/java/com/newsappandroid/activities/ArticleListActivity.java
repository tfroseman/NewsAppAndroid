package com.newsappandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.newsappandroid.Config;
import com.newsappandroid.NetworkConnection;
import com.newsappandroid.R;
import com.newsappandroid.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ArticleListActivity extends Activity {
    ArrayList<String> lst;
    private ArrayAdapter<String> articlesAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] articles = {""};

        lst = new ArrayList<String>();
        lst.addAll(Arrays.asList(articles));

        /*
         * We will need to pass the categories adapter an empty String array instead and then add them
         * once the FetchNewsTask has completed
         */

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        FetchJsonTask jsonTask = new FetchJsonTask();
        jsonTask.execute("article/category/" + extras.getString("ARTICLE_ID"));

        articlesAdapter = new ArrayAdapter<String>(this, R.layout.activity_article_list, lst);

        //Update the categoriesAdapter with the categories or do it inside of the FetchNewsTask
        listView = (ListView) this.findViewById(R.id.listview_articles);
        listView.setAdapter(articlesAdapter);


        // Allows on to select an item from a list view and open a new activity based on that id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleListActivity.class);
                intent.putExtra("ARTICLE_ID", position);
                startActivity(intent);
            }
        });

        setContentView(R.layout.activity_article_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_list, menu);
        return true;
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
        private ArrayAdapter<String> adapter = null;
        private final String LOG_TAG = ArticleListActivity.class.getSimpleName();
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

            articlesAdapter.notifyDataSetChanged();

        }

    }

}
