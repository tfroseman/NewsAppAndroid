package com.newsappandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;


public class LoginActivity extends Activity {
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_PASSWORD = "USER_PASSWORD";
    public static final String ACTIVITY = "ACTIVITY";

    public LogInfo logInfo = new LogInfo();

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
        EditText emailText = (EditText)findViewById(R.id.user_email);
        EditText passwordText = (EditText)findViewById(R.id.user_password);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        NewsAccountManager.create(this, email, "1833631fa88387cd05d893151a0f5cd08fc1546c0d10a714110b8da2d8f30b7c");

        // Create intent to launch new activity
        Intent intent = new Intent(this, MainActivity.class);

        // Wrap up user info to send to next activity
        Bundle userInfo = new Bundle();
        userInfo.putString(USER_EMAIL, email);
        userInfo.putString(USER_PASSWORD, password);

        intent.putExtras(userInfo);

        startActivity(intent);
    }
}
