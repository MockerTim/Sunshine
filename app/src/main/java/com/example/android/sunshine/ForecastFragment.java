package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.sunshine.app.FetchWeatherTask;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 * Created by timur on 25.12.14.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

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
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key), "Moscow,ru");//"Moscow,ru";//"94043";
        String tempUnits = prefs.getString(getString(R.string.pref_temp_units_key), "metric");

        fetchWeatherTask.execute(location, tempUnits);
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forecastArray = new String[] {
                "Today - Sunny - 83˚/63˚",
                "Tomorrow - Foggy - 70˚/ 43˚",
                "Weds - Cloudy - 72˚/ 63˚",
                "Thurs - Rainy - 61˚/ 50˚",
                "Fri - Cloudy - 73˚/ 65˚",
                "Sat - Sunny - 81˚/ 70˚",
                "Sun - Heavy Rain - 70˚/ 60˚"
        };

        ArrayList<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);

        ListView forecastListView = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(mForecastAdapter);

        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Show Toast with the same date that is displayed on the list item
                String forecast = mForecastAdapter.getItem(position);
                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                //EditText editText = (EditText) getActivity().findViewById(R.id.textview_forecast_detail);
                //String message = editText.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
                // TODO Add some more data to the Toast
            }
        });

        return rootView;
    }
}
