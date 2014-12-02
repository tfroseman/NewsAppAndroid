package com.newsappandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsFragment extends Fragment {
    private ArrayAdapter<String> categoriesAdapter;
    private ListView listView;

    public NewsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tells the android system the activity has
        // a menu as described in onCreateOptionsMenu()
        setHasOptionsMenu(true);

        String[] categories = {
            "Food",
            "Art",
            "Money",
            "Something"
        };

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /*
         * We will need to pass the categories adapter an empty String array instead and then add them
         * once the FetchNewsTask has completed
         */
        categoriesAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_categories, R.id.list_item_categories_textview, categories);

        FetchNewsTask newsTask = new FetchNewsTask("a@alk.im", "test", categoriesAdapter);
        newsTask.execute();

        //Update the categoriesAdapter with the categories or do it inside of the FetchNewsTask
        listView = (ListView) rootView.findViewById(R.id.listview_categories);
        listView.setAdapter(categoriesAdapter);

        //List<String> newsCategories = new ArrayList<String>(Arrays.asList(categories));

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_refresh:
                FetchNewsTask newsTask = new FetchNewsTask("a@alk.im", "test", categoriesAdapter);
                newsTask.execute();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_news, menu);
    }
}

