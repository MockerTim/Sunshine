package com.example.android.sunshine;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.sunshine.app.FetchWeatherTask;
import com.example.android.sunshine.app.ForecastAdapter;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 * Created by timur on 25.12.14.
 */
public class ForecastFragment extends Fragment {

    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }

    public void onStart() {
        super.onStart();
        updateWeather();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Constants.D) Log.d(Constants.TAG, "[ForecastFragment.onCreate()]");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Constants.D) Log.d(Constants.TAG, "[ForecastFragment.onOptionsItemSelected()]");

        boolean result = false;

        int id = item.getItemId();

        if(Constants.D) Log.d(Constants.TAG, "id = " + id);

        if(id == R.id.action_refresh) {
            if(Constants.D) Log.d(Constants.TAG, "Executing the FetchWeatherTask.");

            updateWeather();

            result = true;
        }

        if(Constants.D) Log.d(Constants.TAG, "result = " + result);

        if(Constants.D) Log.d(Constants.TAG, "<[ForecastFragment.onOptionsItemSelected()]");

        return result;
        //return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());

        String location = Utility.getPreferredLocation(getActivity());

        weatherTask.execute(location);
    }

    /**
     * If you rotate the device this method is called.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if(Constants.D) Log.d(Constants.TAG, "[ForecastFragment.onCreateView()]");

        String locationSetting = Utility.getPreferredLocation(getActivity());

        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                        locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView forecastListView = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(mForecastAdapter);

        return rootView;
    }
}
