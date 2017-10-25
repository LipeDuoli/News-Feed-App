package com.example.android.newsapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.newsapp.R;
import com.example.android.newsapp.models.News;
import com.example.android.newsapp.utils.QueryUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    //Base URL to GuardianAPI
    private static final String GUARDIAN_API_REQUEST_URL =
            "https://content.guardianapis.com/search";

    private static final String LOG_TAG = QueryUtils.class.getName();

    private Context mContext;
    private String mSearchText;

    public NewsLoader(Context context, String searchText) {
        super(context);
        mContext = context;
        mSearchText = searchText;
    }

    private String configureUrl() {
        Uri baseUri = Uri.parse(GUARDIAN_API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", mSearchText);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String orderBy = sharedPrefs.getString(
                mContext.getString(R.string.settings_order_by_key),
                mContext.getString(R.string.settings_order_by_default));

        uriBuilder.appendQueryParameter("section", "technology");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", "test");

        return uriBuilder.toString();
    }

    @Override
    public List<News> loadInBackground() {
        Log.v("loaderTest", "loadInBackground");
        URL url = QueryUtils.createUrl(configureUrl());

        String jsonResponse = "";
        try {
            jsonResponse = QueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        Log.v("TAG_LOG", jsonResponse != null ? jsonResponse : "null");

        return QueryUtils.extractNews(jsonResponse);
    }
}
