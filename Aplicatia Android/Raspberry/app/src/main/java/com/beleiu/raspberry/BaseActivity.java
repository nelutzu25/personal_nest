package com.beleiu.raspberry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;

/**
 * Created by Beleiu on 02.06.2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private SQLiteHandler db;
    private SessionManager session;

    String name;
    String email;
    public String device_id;
    public String uid;
    public boolean notification;
    public int notify_min_temp;
    public int notify_max_temp;
    public int notify_min_humidity;
    public int notify_max_humidity;
    public int comfort_index;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");
        device_id = user.get("device_id");
        uid = user.get("uid");
        notification = "1".equals(user.get("notification")) || "true".equalsIgnoreCase(user.get("notification"));
        notify_min_temp = Integer.parseInt(user.get("notify_min_temp"));
        notify_max_temp = Integer.parseInt(user.get("notify_max_temp"));
        notify_min_humidity = Integer.parseInt(user.get("notify_min_humidity"));
        notify_max_humidity = Integer.parseInt(user.get("notify_max_humidity"));
        comfort_index = Integer.parseInt(user.get("comfort_index"));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_name", name);
        editor.putString("email", email);
        editor.putString("device_id", device_id);
        editor.putString("uid", uid);
        editor.putBoolean("notification", notification);
        editor.putInt("notify_min_temp", notify_min_temp);
        editor.putInt("notify_max_temp", notify_max_temp);
        editor.putInt("notify_min_humidity", notify_min_humidity);
        editor.putInt("notify_max_humidity", notify_max_humidity);
        editor.putInt("comfort_index", comfort_index);
        editor.commit();

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    public void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this,  LoginActivity.class);
        startActivity(intent);
        finish();
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                Intent intentHistory = new Intent(this, HistoryActivity.class);
                startActivity(intentHistory);
                return true;
            case R.id.action_home:
                Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                return true;
            case R.id.action_logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}