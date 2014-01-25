package sk.pixel.telemetroid;

import android.content.Context;
import android.content.SharedPreferences;

public class DevicePassword {

    private static final String PREFS_NAME = "device_preferences";
    private static final String PASSWORD_KEY = "password";

    private String password;

    public void save(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PASSWORD_KEY, password);
        editor.commit();
    }


    public String getPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        return preferences.getString(PASSWORD_KEY, "not present");
    }
}
