
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
            "https://content.guardianapis.com/search?api-key=90d1ab3e-1584-49e2-9c72-dc09c9e75d3b";
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

        accessibilityListView.setAdapter(mAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        prefs.registerOnSharedPreferenceChangeListener(this);
        accessibilityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Accessibility currentAcessibility = mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentAcessibility.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();


            loaderManager.initLoader(LOADER, null, this);
        } else {


            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_pages_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            mAdapter.clear();

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
                getString(R.string.settings_pages_default));

        String organizeby  = sharedPrefs.getString(
                        getString(R.string.settings_order_by_key),
                        getString(R.string.settings_pages_default));
        ;
        Uri baseUri = Uri.parse(selfdriving);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", pagesize);
                uriBuilder.appendQueryParameter("show-references", "author");
                uriBuilder.appendQueryParameter("show-tags", "contributor");
                uriBuilder.appendQueryParameter("q", "games");
        uriBuilder.appendQueryParameter("API-KEY", "TEST");


        return new BlindLoader.EarthquakeLoader(this, uriBuilder.toString());
    }
    public void onLoadFinished(Loader<List<Accessibility>> loader, List <Accessibility> accessibility) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_new_stories);

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


