package com.metova.volleysample;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class WeatherAdapter extends BaseAdapter {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    private String[] query;
    private HashMap<String, WeatherData> weatherDataMap = new HashMap<String, WeatherData>();
    private RequestQueue requestQueue;

    public WeatherAdapter(Context context, String[] query) {

        setRequestQueue( Volley.newRequestQueue( context ) );
        setQuery( query );
    }

    @Override
    public int getCount() {

        return getQuery().length;
    }

    @Override
    public WeatherData getItem( int position ) {

        String query = getQuery()[position];
        return getWeatherDataMap().get( query );
    }

    @Override
    public long getItemId( int position ) {

        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        Context context = parent.getContext();
        View view = convertView;
        if ( view == null ) {
            view = LayoutInflater.from( context ).inflate( R.layout.weather_row, parent, false );
        }

        TextView nameTextView = (TextView) view.findViewById( R.id.name );
        TextView descriptionTextView = (TextView) view.findViewById( R.id.description );
        TextView tempNowTextView = (TextView) view.findViewById( R.id.temp_now );
        TextView tempLowTextView = (TextView) view.findViewById( R.id.temp_low );
        TextView tempHighTextView = (TextView) view.findViewById( R.id.temp_high );

        WeatherData data = getItem( position );
        if ( data == null ) {
            nameTextView.setText( context.getString( R.string.loading_data ) );
            descriptionTextView.setText( null );
            tempNowTextView.setText( null );
            tempLowTextView.setText( null );
            tempHighTextView.setText( null );

            loadData( getQuery()[position] );
        }
        else {
            nameTextView.setText( data.getName() );
            descriptionTextView.setText( data.getDescription() );
            tempNowTextView.setText( context.getString( R.string.temp_now, data.getTemperature() ) );
            tempLowTextView.setText( context.getString( R.string.temp_low, data.getMinTemperature() ) );
            tempHighTextView.setText( context.getString( R.string.temp_high, data.getMaxTemperature() ) );
        }

        return view;
    }

    private void loadData( final String query ) {

        getRequestQueue().add( new JsonObjectRequest( Method.GET, API_URL + query, null, new Listener<JSONObject>() {

            @Override
            public void onResponse( JSONObject response ) {

                try {
                    WeatherData data = new WeatherData( response );
                    getWeatherDataMap().put( query, data );
                    notifyDataSetChanged();
                }
                catch (JSONException e) {
                    throw new RuntimeException( e );
                }
            }
        }, null ) );
    }

    public class WeatherData {

        private String name;
        private String description;
        private int temperature;
        private int minTemperature;
        private int maxTemperature;

        public WeatherData(JSONObject json) throws JSONException {

            setName( json.getString( "name" ) );

            JSONObject weather = json.getJSONArray( "weather" ).getJSONObject( 0 );
            setDescription( weather.getString( "description" ) );

            JSONObject main = json.getJSONObject( "main" );
            setTemperature( kelvinToFahrenheit( (int) main.getDouble( "temp" ) ) );
            setMinTemperature( kelvinToFahrenheit( (int) main.getDouble( "temp_min" ) ) );
            setMaxTemperature( kelvinToFahrenheit( (int) main.getDouble( "temp_max" ) ) );
        }

        private int kelvinToFahrenheit( int kelvin ) {

            return (int) ( ( kelvin - 273.15 ) * 1.8 + 32 );
        }

        public String getName() {

            return name;
        }

        private void setName( String name ) {

            this.name = name;
        }

        public String getDescription() {

            return description;
        }

        private void setDescription( String description ) {

            this.description = description;
        }

        public int getTemperature() {

            return temperature;
        }

        private void setTemperature( int temperature ) {

            this.temperature = temperature;
        }

        public int getMinTemperature() {

            return minTemperature;
        }

        private void setMinTemperature( int minTemperature ) {

            this.minTemperature = minTemperature;
        }

        public int getMaxTemperature() {

            return maxTemperature;
        }

        private void setMaxTemperature( int maxTemperature ) {

            this.maxTemperature = maxTemperature;
        }
    }

    private RequestQueue getRequestQueue() {

        return requestQueue;
    }

    private void setRequestQueue( RequestQueue requestQueue ) {

        this.requestQueue = requestQueue;
    }

    private String[] getQuery() {

        return query;
    }

    private void setQuery( String[] query ) {

        this.query = query;
    }

    private HashMap<String, WeatherData> getWeatherDataMap() {

        return weatherDataMap;
    }
}
