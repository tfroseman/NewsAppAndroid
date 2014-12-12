package com.newsappandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.newsappandroid.Config;
import com.newsappandroid.model.User;
import com.newsappandroid.NetworkConnection;
import com.newsappandroid.NewsAccountManager;
import com.newsappandroid.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * LoginActivity.java
 * Activity that opens on launch of the app
 * Shows a login screen and then moves to <CategoryActivity>
 */


public class LoginActivity extends Activity {
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_PASSWORD = "USER_PASSWORD";

    private String user_email;
    private String user_password;

    int signedIn = -1;
    boolean completed = false;

    User user = User.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        NewsAccountManager.getToken(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view) {
        //Grab text from editText fields
        user_email = ((EditText) findViewById(R.id.user_email)).getText().toString();
        user_password = ((EditText) findViewById(R.id.user_password)).getText().toString();


        user.setEmail(user_email);
        user.setPassword(user_password);

        signedIn = requestAccount(signedIn, user.getEmail(), user.getPassword());

    }

    public void register(View view){
        // Create intent to launch new register activity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private int requestAccount(int signedIn, String email, String password) {
        String response = "";
        LoginRequestTask loginTask = new LoginRequestTask();

        loginTask.execute(email, password);

        return user.getSigned_in();
    }

    private void nextActivity() {
        // Create intent to launch new activity
        Intent intent = new Intent(this, CategoryActivity.class);

        // Wrap up user info to send to next activity
        Bundle userInfo = new Bundle();
        userInfo.putString(USER_EMAIL, user_email);
        userInfo.putString(USER_PASSWORD, user_password);

        intent.putExtras(userInfo);

        startActivity(intent);
    }

    private class LoginRequestTask extends AsyncTask<String, Boolean, String> {

        /**
         * JSON objected that was collected from the server as a string
         */
        protected JSONObject jsonObject = null;
        User user = User.getUser();

        public LoginRequestTask() {
        }

        /**
         * Called by object.execute
         *
         */
        @Override
        protected String doInBackground(String... params) {
                try {
                    if (user.hasToken()) {
                        return NetworkConnection.tokenAuth(Config.API_SERVER_HOST + "account", user.getApi_token());
                    } else {
                        return NetworkConnection.basicAuth(Config.API_SERVER_HOST + "account", user.getEmail(), user.getPassword());
                        //result = NetworkConnection.basicAuth(Config.API_SERVER_HOST + "account", user.getEmail(), user.getPassword());
                        //Log.i("Result", result);
                        //return result;
                    }

                } catch (IOException e) {
                    // Log exception
                }
            // Should not hit this
            return "ERROR";
        }

        /**
         * Automatically called after the task has completed
         * Should never be manually called
         */
        @Override
        protected void onPostExecute(String result) {
            completed = true;
            //if (result.matches("Network Error")){
                //Toast signedIn = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                //signedIn.show();
                //user.setSigned_in(-9);
            //}else {

                JSONObject userObject;
                JSONObject tokenObject;
                try {
                    jsonObject = new JSONObject(result);
                } catch (Throwable t) {
                    // Log here about malformed json
                }

                try {
                    userObject = jsonObject.getJSONObject("user");
                    tokenObject = jsonObject.getJSONObject("token");
                    populateUser(userObject);
                    //Bug in that the token needed is not passed with the  user info
                    user.setApi_token(tokenObject.getString("api_token"));

                    //Alert the user that they are signed in with a toast
                    Toast signedIn = Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_LONG);
                    signedIn.show();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
           // }
            nextActivity();
        }

        /**
         * Called from doInBackground to update main thread
         *
         */
        @Override
        protected void onProgressUpdate(Boolean... progress) {

        }

        //Populate User object with json
        private void populateUser(JSONObject userObject) {
            try {
                user.setId(userObject.getInt("id"));
                user.setActivated(userObject.getBoolean("activated"));
                user.setActivated_at(userObject.getString("activated_at"));
                user.setLast_login(userObject.getString("last_login"));
                user.setFirst_name(userObject.getString("first_name"));
                user.setLast_name(userObject.getString("last_name"));
                user.setCreated_at(userObject.getString("created_at"));
                user.setUpdated_at(userObject.getString("updated_at"));
                //user.setApi_token(userObject.getString("api_token"));
                user.setSigned_in(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private boolean checkNetwork(){
            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

    }

}
