package com.example.juandavid.sunshine;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juandavid.sunshine.data.WeatherContract;

public class DetailActivity extends ActionBarActivity {

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
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);




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
            Intent startIntentSettings = new Intent(this, SettingsActivity.class);
            startActivity(startIntentSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final int FORECAST_ITEM_LOADER = 0;
        private Uri mReceivedUri;
        private ShareActionProvider mShareActionProvider;
        private static final String LOG_TAG =PlaceholderFragment.class.getSimpleName();
        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        private String mForecastStr;
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
        static final int COL_WEATHER_ID = 0;
        static final int COL_WEATHER_DATE = 1;
        static final int COL_WEATHER_DESC = 2;
        static final int COL_WEATHER_MAX_TEMP = 3;
        static final int COL_WEATHER_MIN_TEMP = 4;
        static final int COL_LOCATION_SETTING = 5;
        static final int COL_WEATHER_CONDITION_ID = 6;
        static final int COL_COORD_LAT = 7;
        static final int COL_COORD_LONG = 8;

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


            inflater.inflate(R.menu.detailfragment, menu);
            MenuItem item = menu.findItem(R.id.action_share);

            ShareActionProvider mShareActionProvider=
                    (ShareActionProvider) MenuItemCompat.getActionProvider(item);

            if(mForecastStr!=null){
                mShareActionProvider.setShareIntent(createSharedForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }

        }

        private Intent createSharedForecastIntent(){

            Intent shareIntent =new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,mForecastStr+FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent=getActivity().getIntent();

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


            if (intent != null) {

                mForecastStr = intent.getDataString();
            }

            if (null != mForecastStr) {
                ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
            }
            return rootView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(FORECAST_ITEM_LOADER, null,  this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Intent intent= getActivity().getIntent();
            if(intent==null){
                return null;
            }
            mReceivedUri=intent.getData();
            return new CursorLoader(getActivity(),
                    mReceivedUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(!data.moveToFirst()){return;}
                String dateString=Utility.formatDate(data.getLong(COL_WEATHER_DATE));
                String weatherDescription=data.getString(COL_WEATHER_DESC);
                boolean isMetric=Utility.isMetric(getActivity());

                String high=Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP),isMetric);
                String low=Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP),isMetric);

                mForecastStr=String.format("%s - %s - %s/%s", dateString,weatherDescription,high,low);
                TextView detailTextView= (TextView)getView().findViewById(R.id.detail_text);
                detailTextView.setText(mForecastStr);

                if(mShareActionProvider!=null){
                    mShareActionProvider.setShareIntent(createSharedForecastIntent());
                }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}