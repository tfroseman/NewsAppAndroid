package com.newsappandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent coming from login
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //Passing bundle from login to fragment view
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(extras);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, newsFragment)
                    .commit();
        }


    }
}
