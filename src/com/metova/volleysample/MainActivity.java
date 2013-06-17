package com.metova.volleysample;

import android.app.ListActivity;
import android.os.Bundle;


public class MainActivity extends ListActivity {

    private static final String[] QUERY = new String[] { "Nashville,%20TN", "Atlanta,%20GA", "Brewton,%20AL", "San%20Francisco,%20CA", "New%20York%20City,%20NY", "Jackson,%20WY", "London,%20UK", "Austin,%20TX", "Seattle,%20OR", "Kansas%20City,%20KS", "Chicago,%20,IL" };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setListAdapter( new WeatherAdapter( this, QUERY ) );
    }
}
