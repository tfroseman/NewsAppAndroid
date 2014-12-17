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
import com.newsappandroid.NetworkConnection;
import com.newsappandroid.R;
import com.newsappandroid.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class RegisterActivity extends Activity {

    User user = User.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void registerAccount(View view) {
        //Grab text from editText fields
        String user_email = ((EditText) findViewById(R.id.user_email)).getText().toString();
        String user_password = ((EditText) findViewById(R.id.user_password)).getText().toString();
        String user_password_verify = ((EditText) findViewById(R.id.user_password_verify)).getText().toString();
        checkPassword(user_password, user_password_verify, user_email);
    }

    private void checkPassword(String userPassword, String userPasswordVerify, String user_email) {
        if (userPassword.matches(userPasswordVerify) && userPassword.length() == Config.MIN_PASSWORD_LENGTH) {
            user.setEmail(user_email);
            user.setPassword(userPassword);

            RegisterRequestTask registerRequestTask = new RegisterRequestTask();
            registerRequestTask.execute();

            //Alert the user that they are registered in with a toast
            Toast signedIn = Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG);
            signedIn.show();

            // Start next activity

        } else if ((userPassword.length() != Config.MIN_PASSWORD_LENGTH) || (userPasswordVerify.length() != Config.MIN_PASSWORD_LENGTH)) {
            Toast passwordLength = Toast.makeText(getApplicationContext(), "Password(s) too short", Toast.LENGTH_LONG);
            passwordLength.show();

        } else if (!userPassword.matches(userPasswordVerify)) {
            Toast misMatch = Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG);
            misMatch.show();

        }

    }

    private void nextActivity() {
        // Create intent to launch new activity
        Intent intent = new Intent(this, CategoryActivity.class);

        // Wrap up user info to send to next activity
        Bundle userInfo = new Bundle();
        //userInfo.putString(USER_EMAIL, user_email);
        //userInfo.putString(USER_PASSWORD, user_password);

        intent.putExtras(userInfo);

        startActivity(intent);
    }

    private class RegisterRequestTask extends AsyncTask<String, Boolean, String> {

        /**
         * JSON objected that was collected from the server as a string
         */
        protected JSONObject jsonObject = null;
        User user = User.getUser();

        public RegisterRequestTask() {
        }

        /**
         * Called by object.execute
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                return NetworkConnection.register(Config.API_SERVER_HOST + "register", user.getEmail(), user.getPassword());
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

        private boolean checkNetwork() {
            ConnectivityManager cm =
                    (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

    }

}
