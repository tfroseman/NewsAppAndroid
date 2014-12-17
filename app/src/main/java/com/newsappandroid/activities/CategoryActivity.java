package com.newsappandroid.activities;

/**
 * CategoryActivity.java
 * Launches after login passes and then loads up the CategoryFragment into view
 * Do no work other that load a fragment
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.newsappandroid.fragments.CategoryFragment;
import com.newsappandroid.R;

public class CategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent coming from login
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //Passing bundle from login to fragment view
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(extras);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, categoryFragment)
                    .commit();
        }

    }
}
