package sk.pixel.telemetroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;

public class DeviceIdentifiers {

    private final String TAG = "DeviceIdentifiers";

    private static final String PREFS_NAME = "device_preferences";
    private static final String PASSWORD_KEY = "password";

    private String password = "";
    private Context context;

    public DeviceIdentifiers(Context context) {
        this.context = context;
    }

    public void save() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PASSWORD_KEY, password);
        editor.commit();
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public String getPassword() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        return preferences.getString(PASSWORD_KEY, "");
    }

    public String getIdentifier() {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }
}
