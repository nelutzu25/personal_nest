package helper;

/**
 * Created by Beleiu on 02.05.2015.
 */
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_NOTIFY_MIN_TEMP = "notify_min_temp";
    private static final String KEY_NOTIFY_MAX_TEMP = "notify_max_temp";
    private static final String KEY_NOTIFY_MIN_HUMIDITY = "notify_min_humidity";
    private static final String KEY_NOTIFY_MAX_HUMIDITY = "notify_max_humidity";
    private static final String KEY_COMFORT_INDEX = "comfort_index";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT,"
                + KEY_DEVICE_ID + " TEXT,"
                + KEY_NOTIFICATION + " TEXT,"
                + KEY_NOTIFY_MIN_TEMP + " TEXT,"
                + KEY_NOTIFY_MAX_TEMP + " TEXT,"
                + KEY_NOTIFY_MIN_HUMIDITY + " TEXT,"
                + KEY_NOTIFY_MAX_HUMIDITY + " TEXT,"
                + KEY_COMFORT_INDEX + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUserAtLogin(String name, String email, String uid, String created_at, String device_id,
                String notification, String notify_min_temp, String notify_max_temp, String notify_min_humidity,
                String notify_max_humidity, String comfort_index) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_DEVICE_ID, device_id); // Device Id
        values.put(KEY_NOTIFICATION, notification); // Device Id
        values.put(KEY_NOTIFY_MIN_TEMP, notify_min_temp); // Device Id
        values.put(KEY_NOTIFY_MAX_TEMP, notify_max_temp); // Device Id
        values.put(KEY_NOTIFY_MIN_HUMIDITY, notify_min_humidity); // Device Id
        values.put(KEY_NOTIFY_MAX_HUMIDITY, notify_max_humidity); // Device Id
        values.put(KEY_COMFORT_INDEX, comfort_index); // Device Id

        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Storing user details in database
     * */
    public void addUserAtRegistration(String name, String email, String uid, String created_at, String device_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_DEVICE_ID, device_id); // Device Id

        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void update(String field,String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE login SET " + field + "='" + value +"'");
        db.close(); // Closing database connection

        Log.d(TAG, "User updated into sqlite: " + field + value);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("device_id", cursor.getString(4));
            user.put("notification", cursor.getString(5));
            user.put("notify_min_temp", cursor.getString(6));
            user.put("notify_max_temp", cursor.getString(7));
            user.put("notify_min_humidity", cursor.getString(8));
            user.put("notify_max_humidity", cursor.getString(9));
            user.put("comfort_index", cursor.getString(10));
            user.put("created_at", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user login status return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
