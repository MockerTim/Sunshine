package com.example.android.sunshine;

import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by timur on 08.01.15.
 */
public class DetailFragment extends Fragment {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastString;

    ShareActionProvider shareActionProvider;

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

        if(shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareIntent());
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

                //setShareIntent(createShareIntent());
            } else {
                if(Constants.D) Log.d(Constants.TAG, "Intent is null!");
            }
        }

        if(Constants.D) Log.d(Constants.TAG, "result = " + result);

        if(Constants.D) Log.d(Constants.TAG, "<[DetailFragment.onOptionsItemSelected()]");

        //return result;
        return super.onOptionsItemSelected(item);
//        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null) {
            mForecastString = intent.getStringExtra(Intent.EXTRA_TEXT);
            TextView textView = (TextView) rootView.findViewById(R.id.detail_text);
            textView.setText(mForecastString);
        }

        return rootView;
    }

    private Intent createShareIntent() {
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
}
