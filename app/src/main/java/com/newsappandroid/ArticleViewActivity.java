package com.newsappandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.newsappandroid.model.ArticleList;
import com.newsappandroid.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ArticleViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString("URL");

        url = url.replace("\\" , "" );


        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        //FetchJsonTask fetchJsonTask = new FetchJsonTask();
        //fetchJsonTask.execute("article/" + extras.getString("ARTICLE_ID"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_view, menu);
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
        private final String LOG_TAG = ArticleViewActivity.class.getSimpleName();
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

            Log.i("Array Length",String.valueOf(jsonArray.length()));

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    jsonObject = jsonArray.getJSONObject(i); // Grab each object from the array

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
