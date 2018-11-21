
package com.example.ricardo.grabbingjsonattributestest;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Accessibility>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String selfdriving =
            "https://content.guardianapis.com/search?from-date=2018-01-10&to-date=2018-04-10&show-tags=contributor&q=self%20driving%20car&api-key=90d1ab3e-1584-49e2-9c72-dc09c9e75d3b";

//             "https://content.guardianapis.com/search?api-key=90d1ab3e-1584-49e2-9c72-dc09c9e75d3b";

    private static final int LOADER = 1;

    private AcessibilityAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView accessibilityListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        accessibilityListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new AcessibilityAdapter(this, new ArrayList<Accessibility> ());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        accessibilityListView.setAdapter(mAdapter);

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        accessibilityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Accessibility currentAcessibility = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentAcessibility.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER, null, this);
        } else {


            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_pages_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            mAdapter.clear();





    // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(LOADER, null, this);
        }
    }

    @Override
    public Loader<List<Accessibility>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String pagesize = sharedPrefs.getString(
                getString(R.string.settings_pages_key),
                getString(R.string.settings_pages_default));

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)


        );
        Uri baseUri = Uri.parse(selfdriving);
        Uri.Builder uriBuilder = baseUri.buildUpon();


//        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", pagesize);
                uriBuilder.appendQueryParameter("show-references", "author");
                uriBuilder.appendQueryParameter("show-tags", "contributor");
//                uriBuilder.appendQueryParameter("q", "Android");
                uriBuilder.appendQueryParameter("q", "Technology");
//        uriBuilder.appendQueryParameter("api-key", "test");


        return new BlindLoader.EarthquakeLoader(this, uriBuilder.toString());
    }




    public void onLoadFinished(Loader<List<Accessibility>> loader, List <Accessibility> accessibility) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_new_stories);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        if (accessibility != null && !accessibility.isEmpty()) {
            mAdapter.addAll(accessibility);
            updateUi(accessibility);


        }
    }

    @Override
    public void onLoaderReset(Loader <List <Accessibility>> loader) {

    }

    private void updateUi(List<Accessibility> acessibility) {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


