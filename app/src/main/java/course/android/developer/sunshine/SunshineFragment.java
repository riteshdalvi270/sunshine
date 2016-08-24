package course.android.developer.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SunshineFragment extends android.support.v4.app.Fragment {

    public SunshineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.forcastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if(R.id.action_refresh ==  menuItem.getItemId()) {
            new FetchWeatherTask().execute("q=94043","mode=json","units=metric","cnt=7");
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_sunshine, container, false);

        final List<String> forecast = new ArrayList<>();
        forecast.add("Today-Sunny-88/63");
        forecast.add("Tomorrow-Foggy-70/46");
        forecast.add("Wed-Cloudy-72/63");
        forecast.add("Thursday-Rainy-64/51");
        forecast.add("Friday-Foggy-70/46");
        forecast.add("Saturday-Sunny-76/68");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forcast_textview,forecast);

        final ListView listView =  (ListView) view.findViewById(R.id.listview_forecast);

        listView.setAdapter(arrayAdapter);

        //new FetchWeatherTask().execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

        return view;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {

                StringBuilder uris = new StringBuilder();

                buildURI(uris,params);

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily"+uris+"&appid=6b6c11f811430983627c8f2b8eb15159");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG,"Forcast Json String" + forecastJsonStr);

                final double max = getMaxTemperatureForADay(forecastJsonStr, 1);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

    private static double getMaxTemperatureForADay(final String jsonString, int dayIndex) {

        Double day = null;
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            final JSONArray list = jsonObject.getJSONArray("list");

            final JSONObject jsonObject1 = (JSONObject) list.get(dayIndex);

            final JSONObject temp = jsonObject1.getJSONObject("temp");

            if(temp.getDouble("day") >= 0) {
                return temp.getDouble("day");
            }

            return -1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return day;
    }

    private void buildURI(final StringBuilder uri,final String... params) {

        for(String param : params) {

            if(uri.length() == 0) {
                uri.append("?");
                uri.append(param);
            }
            uri.append("&");
            uri.append(param);
        }
    }
}
