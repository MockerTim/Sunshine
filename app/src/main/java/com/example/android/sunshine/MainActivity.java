package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Constants.TAG, "[MainActivity.onCreate()]");

        setContentView(R.layout.activity_main);

        // This two lines of code are mandatory to show app icon in the menubar on android 21
        // See http://stackoverflow.com/questions/26440279/show-icon-in-actionbar-toolbar-with-appcompat-v7-21
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(Constants.D) Log.d(Constants.TAG, "Starting the SettingsActivity.");

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.action_show_location) {
            if(Constants.D) Log.d(Constants.TAG, "Showing the location.");

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String location = sharedPrefs.getString(getString(R.string.pref_location_key), "Moscow,ru");

            Uri.Builder uriBuilder = new Uri.Builder();

            uriBuilder.scheme("geo")
                    .appendQueryParameter("geo:latitude", "0")
                    .appendQueryParameter("geo:longitude", "0")
                    .appendQueryParameter("q", location);

            showMap(uriBuilder.build());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param geoLocation
     */
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
