package com.example.android.newsapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.newsapp.R;
import com.example.android.newsapp.adapters.NewsAdapter;
import com.example.android.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final int NEWS_LOADER_ID = 1;
    private static final String QUERY_KEY = "query";

    private RecyclerView mNewsRecyclerView;
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;
    private NewsAdapter mNewsAdapter;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsRecyclerView = (RecyclerView) findViewById(R.id.news_recylerview);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mLoadingIndicator = findViewById(R.id.loading_indicator);

        // Set empty state text for first open
        mEmptyStateTextView.setText(getString(R.string.first_open_app_text));

        configureRecycleView();

        mLoaderManager = getSupportLoaderManager();

        //Load loader if exists
        if (mLoaderManager.getLoader(NEWS_LOADER_ID) != null){
            mLoaderManager = getSupportLoaderManager();
            mLoaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            //Make a blank search based on settings
            searchNews("");
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchNews(query);
        }
    }

    private void configureRecycleView() {
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        mNewsRecyclerView.setAdapter(mNewsAdapter);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        setListEmptyState();
    }

    private void setListEmptyState() {
        if (mNewsAdapter.getItemCount() == 0)
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        else
            mEmptyStateTextView.setVisibility(View.GONE);
    }

    private void searchNews(String query) {
        // Clear the adapter of previous news data
        mNewsAdapter.clear();

        if (deviceHasNetworkConnection()) {
            Bundle bundle = new Bundle();
            bundle.putString(QUERY_KEY, query);

            mEmptyStateTextView.setText(getString(R.string.searching_news));
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLoaderManager.restartLoader(NEWS_LOADER_ID, bundle, this).forceLoad();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        setListEmptyState();
    }

    private boolean deviceHasNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.v("loaderTest", "onCreateLoader");
        String query = args.getString(QUERY_KEY);
        return new NewsLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        Log.v("loaderTest", "onLoadFinished");
        mLoadingIndicator.setVisibility(View.GONE);

        // Set empty state text for no results
        mEmptyStateTextView.setText(R.string.no_news);

        if (data != null && !data.isEmpty()) {
            mNewsAdapter.addAll(data);
        }

        setListEmptyState();
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();
        setListEmptyState();
    }

}
