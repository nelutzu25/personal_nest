package com.beleiu.raspberry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.beleiu.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.JSONParser;
import helper.SQLiteHandler;
import helper.SessionManager;


public class MainActivity extends BaseActivity {
    TextView txtTemperature;
    TextView txtHumidity;
    TextView txtDate;
    NumberPicker npDesiredTemperature;
    String temperature;
    String humidity;
    String desired_temperature;
    String date;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // JSON Node names
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_PARAMETER = "parameter";
    private static final String TAG_HUMIDITY = "humidity";
    private static final String TAG_DATE = "date";

    private static final String TAG_DEVICE_ID = "device_id";
    private static final String TAG_DESIRED_TEMPERATURE = "desired_temperature";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        new GetCurrentParameters().execute();

    }

    /**
     * Background Async Task to Get complete product details
     */
    class GetCurrentParameters extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading temperature details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting current parameters in background thread
         */
        protected String doInBackground(String... paramsz) {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("device_id", device_id));

                        // getting product details by making HTTP request
                        // Note that product details url will use POST request
                        JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_RECENT_PARAMETERS, "POST", params);

                        // check your log for json response
                        Log.d("Current parameters", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PARAMETER); // JSON Array

                            // get first object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // display details in EditText
                            temperature = product.getString(TAG_TEMPERATURE);
                            humidity = product.getString(TAG_HUMIDITY);
                            date = product.getString(TAG_DATE);
                            desired_temperature = product.getString(TAG_DESIRED_TEMPERATURE);

                        } else {
                            // nothing found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

           return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    txtTemperature = (TextView) findViewById(R.id.temperature);
                    txtHumidity = (TextView) findViewById(R.id.humidity);
                    txtDate = (TextView) findViewById(R.id.date);

                    // display product data in EditText
                    txtTemperature.setText(temperature);
                    txtHumidity.setText(humidity);
                    txtDate.setText(date);

                    npDesiredTemperature = (NumberPicker) findViewById(R.id.numberPicker);
                    npDesiredTemperature.setMaxValue(30);
                    npDesiredTemperature.setMinValue(0);
                    npDesiredTemperature.setValue(Integer.parseInt(desired_temperature));
                    npDesiredTemperature.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                            new UpdateDesiredTemperature().execute();
                        }
                    });

                }
            });
        }
    }

    /**
     * Background Async Task to update desired temperature
     */
    class UpdateDesiredTemperature extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Updating desired temperature ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving desired temperature
         */
        protected String doInBackground(String... args) {

            int desiredTemperature = npDesiredTemperature.getValue();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_DEVICE_ID, device_id));
            params.add(new BasicNameValuePair(TAG_DESIRED_TEMPERATURE, Integer.toString(desiredTemperature)));

            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_UPDATE_DESIRED_TEMPERATURE, "POST", params);

            Log.d("Updating temperature", json.toString());
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    //Intent i = getIntent();
                    // send result code 100 to notify about product update
                    //setResult(100, i);
                    //finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product updated
            pDialog.dismiss();
        }
    }



    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}

