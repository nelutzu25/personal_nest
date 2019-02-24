package com.beleiu.raspberry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import helper.JSONParser;


public class HistoryActivity extends BaseActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    NumberPicker npHistory;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, Double>> temperaturesList;
    ArrayList<HashMap<String, Double>> humiditiesList;
    ArrayList<HashMap<String, Date>> datesList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PARAMETERS = "parameters";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_HUMIDITY = "humidity";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_DEVICE_ID = "device_id";
    private static final String TAG_HISTORY_DAYS = "history_days";

    // parameters JSONArray
    JSONArray parameters = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Hashmap for ListView
        temperaturesList = new ArrayList<HashMap<String, Double>>();
        humiditiesList = new ArrayList<HashMap<String, Double>>();
        datesList = new ArrayList<HashMap<String, Date>>();
        npHistory = (NumberPicker) findViewById(R.id.numberPicker);
        npHistory.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npHistory.setMinValue(1);
        npHistory.setMaxValue(30);
        npHistory.setValue(2);
        // Loading parameters in Background Thread
        new LoadParameters().execute();
        npHistory.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                new LoadParameters().execute();
            }
        });
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadParameters extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HistoryActivity.this);
            pDialog.setMessage("Loading history. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            npHistory.setEnabled(false);
        }

        /**
         * getting All parameters from url
         */
        protected String doInBackground(String... args) {
            int history_days = npHistory.getValue();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_DEVICE_ID, "1"));
            params.add(new BasicNameValuePair(TAG_HISTORY_DAYS, Integer.toString(history_days)));

            JSONObject json = jParser.makeHttpRequest(AppConfig.URL_GET_ALL_PARAMETERS, "POST", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    parameters = json.getJSONArray(TAG_PARAMETERS);

                    for (int i = 0; i < parameters.length(); i++) {
                        JSONObject c = parameters.getJSONObject(i);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = format.parse(c.getString(TAG_DATE));
                        Double temperature = c.getDouble(TAG_TEMPERATURE);
                        Double humidity = c.getDouble(TAG_HUMIDITY);

                        // creating new HashMap
                        HashMap<String, Double> mapT = new HashMap<String, Double>();
                        HashMap<String, Double> mapH = new HashMap<String, Double>();
                        HashMap<String, Date> mapD = new HashMap<String, Date>();

                        // adding each child node to HashMap key => value
                        mapD.put(TAG_DATE, ((Date) date));
                        mapT.put(TAG_TEMPERATURE, temperature);
                        mapH.put(TAG_HUMIDITY, humidity);

                        // adding HashList to ArrayList
                        datesList.add(mapD);
                        temperaturesList.add(mapT);
                        humiditiesList.add(mapH);
                    }
                } else {
                    // no parameters found
                    // Launch Add New product Activity
                   // Intent i = new Intent(getApplicationContext(),
                           // NewProductActivity.class);
                    // Closing all previous activities
                   // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   // startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all parameters

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    GraphView graph = (GraphView) findViewById(R.id.graph);

                    DataPoint[] dataPointsTemperature = new DataPoint[datesList.size()];
                    DataPoint[] dataPointsHumidity = new DataPoint[datesList.size()];
                    Date d1 = new Date();
                    Date d2 = new Date();
                    Date d3 = new Date();
                    for (int i = 0; i < datesList.size(); i++) {
                        if(i == 0) {
                            d1 = datesList.get(i).get(TAG_DATE);
                        }
                        if(i == datesList.size()/2 ) {
                            d2 = datesList.get(i).get(TAG_DATE);
                        }
                        if(i == datesList.size()-1 ) {
                            d3 = datesList.get(i).get(TAG_DATE);
                        }
                        dataPointsTemperature[i] = new DataPoint(datesList.get(i).get(TAG_DATE), temperaturesList.get(i).get(TAG_TEMPERATURE));
                        dataPointsHumidity[i] = new DataPoint(datesList.get(i).get(TAG_DATE), humiditiesList.get(i).get(TAG_HUMIDITY));
                    }
                    datesList.clear();
                    temperaturesList.clear();
                    humiditiesList.clear();

                    LineGraphSeries<DataPoint> temperatureSeries = new LineGraphSeries<DataPoint>(dataPointsTemperature);
                    temperatureSeries.setColor(Color.RED);

                    LineGraphSeries<DataPoint> humiditySeries = new LineGraphSeries<DataPoint>(dataPointsHumidity);
                    //humiditySeries.setColor(Color.RED);

                    graph.addSeries(temperatureSeries);
                    //graph.addSeries(humiditySeries);

                    // set date label formatter
                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                    graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

                    // set manual x bounds to have nice steps
                    graph.getViewport().setMinX(d1.getTime());
                    graph.getViewport().setMaxX(d3.getTime());
                    graph.getViewport().setXAxisBoundsManual(true);

                    // set second scale
                    graph.getSecondScale().addSeries(humiditySeries);
                    // the y bounds are always manual for second scale
                    graph.getSecondScale().setMinY(0);
                    graph.getSecondScale().setMaxY(100);
                    humiditySeries.setColor(Color.CYAN);
                    graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.CYAN);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.RED);

                    temperatureSeries.setTitle("temperature");
                    humiditySeries.setTitle("humidity");
                    //graph.getLegendRenderer().setVisible(true);
                    //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                    graph.setTitle("Temperature and Humidity");
                    pDialog.dismiss();

                    npHistory.setEnabled(true);

                }
            });
        }
    }
}
