package com.newsappandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.newsappandroid.R;
import com.newsappandroid.fragments.ArticleListFragment;


public class ArticleListActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ArticleListFragment articleFragment = new ArticleListFragment();
        articleFragment.setArguments(extras);

        setContentView(R.layout.activity_article_list_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.article_container, articleFragment)
                    .commit();
        }
    }
}
