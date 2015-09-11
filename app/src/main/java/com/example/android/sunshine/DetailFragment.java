package com.example.android.sunshine;

import android.database.Cursor;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ShareActionProvider;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by timur on 08.01.15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//    private ShareActionProvider mShareActionProvider;
    private String mForecastString;

    ShareActionProvider shareActionProvider;

    private static final int DETAIL_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    public static final int COL_WEATHER_DATE = 2;
    public static final int COL_WEATHER_DESC = 3;
    public static final int COL_WEATHER_MAX_TEMP = 6;
    public static final int COL_WEATHER_MIN_TEMP = 5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Constants.D) Log.d(Constants.TAG, "[DetailFragment.onCreate()]");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detailfragment, menu);

        if(Constants.D) Log.d(Constants.TAG, "[DetailFragment.onCreateOptionsMenu()]>");

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(mForecastString != null) {
            shareActionProvider.setShareIntent(createForecastShareIntent());
        } else {
            if(Constants.D) Log.e(Constants.TAG, "Share Action Provider is null!?");
        }

        if(Constants.D) Log.d(Constants.TAG, "shareActionProvider: " + shareActionProvider);

        if(Constants.D) Log.d(Constants.TAG, "<[DetailFragment.onCreateOptionsMenu()]");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Constants.D) Log.d(Constants.TAG, "[DetailFragment.onOptionsItemSelected()]>");
        boolean result = false;

        int id = item.getItemId();

        if(Constants.D) Log.d(Constants.TAG, "id = " + id);

        if (id == R.id.menu_item_share) {

            if(Constants.D) Log.d(Constants.TAG, "Menu item share selected");

            Intent intent = getActivity().getIntent();

            if(intent != null) {
                String message = intent.getStringExtra(Intent.EXTRA_TEXT);

                //setShareIntent(createForecastShareIntent());
            } else {
                if(Constants.D) Log.d(Constants.TAG, "Intent is null!");
            }
        }

        if(Constants.D) Log.d(Constants.TAG, "result = " + result);

        if(Constants.D) Log.d(Constants.TAG, "<[DetailFragment.onOptionsItemSelected()]");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null) {
            mForecastString = intent.getDataString();
        }

        if(null != mForecastString) {
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastString);
        }

        return rootView;
    }

    private Intent createForecastShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastString + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        } else {
            if(Constants.D) Log.e(Constants.TAG, "shareActionProvider is null!");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");

        Intent intent = getActivity().getIntent();

        if(intent == null) {
            return null;
        }

        return new CursorLoader(getActivity(), intent.getData(), FORECAST_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if(!data.moveToFirst()) {return;}

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));

        String weatherDescription = data.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

        String low = Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        mForecastString = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView) getView().findViewById(R.id.detail_text);
        detailTextView.setText(mForecastString);

        if(shareActionProvider != null) {
            shareActionProvider.setShareIntent(createForecastShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
