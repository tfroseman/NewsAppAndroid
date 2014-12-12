package com.newsappandroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.newsappandroid.Config;
import com.newsappandroid.R;
import com.newsappandroid.model.User;


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
        checkPassword(user_password, user_password_verify);
    }

    private void checkPassword(String userPassword, String userPasswordVerify) {
        if(userPassword.matches(userPasswordVerify) && userPassword.length() == Config.MIN_PASSWORD_LENGTH){
            //Alert the user that they are registered in with a toast
            Toast signedIn = Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG);
            signedIn.show();

            // Start next activity

        }else if((userPassword.length() != Config.MIN_PASSWORD_LENGTH) || (userPasswordVerify.length() != Config.MIN_PASSWORD_LENGTH)){
            Toast passwordLength = Toast.makeText(getApplicationContext(), "Password(s) too short", Toast.LENGTH_LONG);
            passwordLength.show();

        }else if(!userPassword.matches(userPasswordVerify)){
            Toast misMatch = Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG);
            misMatch.show();

        }

    }
}
