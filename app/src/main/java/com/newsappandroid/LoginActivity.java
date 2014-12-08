package com.newsappandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class LoginActivity extends Activity {
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_PASSWORD = "USER_PASSWORD";

    private String user_email;
    private String user_password;

    boolean signedIn = false;
    boolean completed = false;

    User user = User.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        completed = false;
        user_email = ((EditText) findViewById(R.id.user_email)).getText().toString();
        user_password = ((EditText) findViewById(R.id.user_password)).getText().toString();


        user.setEmail(user_email);
        user.setPassword(user_password);

        signedIn = requestAccount(signedIn, user.getEmail(), user.getPassword());
        while(user.getSigned_in())
        if (signedIn) {
            nextActivity(view);
        }

    }

    private boolean requestAccount(boolean signedIn, String email, String password) {
        String response = "";
        LoginRequestTask loginTask = new LoginRequestTask();

        loginTask.execute(email, password);

        return user.getSigned_in();
    }

    private void nextActivity(View view) {
        // Create intent to launch new activity
        Intent intent = new Intent(this, MainActivity.class);

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
                //if(user.hasToken()){

                //}else{
                    return NetworkConnection.basicAuth(Config.API_SERVER_HOST + "account", user.getEmail(), user.getPassword());
                //}

            } catch (IOException e) {
                // Log exception
            }
            return null;
        }

        /**
         * Automatically called after the task has completed
         * Should never be manually called
         */
        @Override
        protected void onPostExecute(String result) {
            JSONObject userObject;
            try {
                jsonObject = new JSONObject(result);
            } catch (Throwable t) {
                // Log here about malformed json
            }

            try {
                userObject = jsonObject.getJSONObject("user");
                populateUser(userObject);

                //Alert the user that they are signed in with a toast
                Toast signedIn = Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_LONG);
                signedIn.show();


            } catch (JSONException e) {
                e.printStackTrace();

            }
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
                user.setApi_token(userObject.getString("api_token"));
                user.setSigned_in(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
