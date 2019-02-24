package helper;

/**
 * Created by Beleiu on 02.05.2015.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_DEVICE = "device_id";
    private static final String KEY_UID = "uid";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyDevice(String device) {
        if(isLoggedIn()) {
            editor.putString(KEY_DEVICE, device);

            // commit changes
            editor.commit();

            Log.d(TAG, "User key device id set to " + device);
        }
    }

    public void setKeyUser(String uid) {
        if(isLoggedIn()) {
            editor.putString(KEY_UID, uid);

            // commit changes
            editor.commit();

            Log.d(TAG, "User id set to " + uid);
        }
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}