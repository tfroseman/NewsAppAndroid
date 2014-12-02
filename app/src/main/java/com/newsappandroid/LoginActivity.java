package com.newsappandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_PASSWORD = "USER_PASSWORD";

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
        EditText user_email_Text = (EditText)findViewById(R.id.user_email);
        EditText user_password_Text = (EditText)findViewById(R.id.user_password);

        String email = user_email_Text.getText().toString();
        String password = user_password_Text.getText().toString();

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
