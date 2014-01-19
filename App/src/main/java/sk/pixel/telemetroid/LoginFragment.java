package sk.pixel.telemetroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment implements ServerPoster.PostDataListener {
    public static final String LOGIN_TYPE = "login_type";
    public static final String LOGIN_URL = "/login";
    private static final String USERNAME = "username";
    private static final String SAVE_USERNAME = "save_username";
    private static final String PREFS_NAME = "login_preferences";
    private final LoginCallbacks parent;

    public interface LoginCallbacks {
        public void loginSucessfull();
    }

    public LoginFragment(LoginCallbacks parent) {
        this.parent = parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((EditText) getView().findViewById(R.id.username)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).setText("");
                    ((CheckBox) getView().findViewById(R.id.rememeber_username)).setChecked(false);
                }
            }
        });
        setSaveCheckbox();
        fillUsernameFromPrefs();
    }

    private void setSaveCheckbox() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        CheckBox save = (CheckBox) getView().findViewById(R.id.rememeber_username);
        if (preferences.contains(SAVE_USERNAME)) {
            save.setChecked(preferences.getBoolean(SAVE_USERNAME, false));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.user_login, container, false);
        return rootView;
    }

    private void fillUsernameFromPrefs() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        EditText username = (EditText) getView().findViewById(R.id.username);
        if (preferences.contains(USERNAME)) {
            username.setText(preferences.getString(USERNAME, ""));
        }
    }

    public void loginAsUserPressed(View view) {
        userLogin();
    }

    private void saveUsername() {
        Log.d("TAG", "save");
        CheckBox checkBox = (CheckBox) getView().findViewById(R.id.rememeber_username);
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        if (checkBox.isChecked()) {
            EditText username = (EditText) getView().findViewById(R.id.username);
            editor.putString(USERNAME, username.getText().toString());
            editor.putBoolean(SAVE_USERNAME, true);
        }
        editor.commit();
    }

    private void userLogin() {
        //TODO login as user
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        EditText username = (EditText) getView().findViewById(R.id.username);
        EditText password = (EditText) getView().findViewById(R.id.password);
        nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
        ServerPoster poster = new ServerPoster(this, nameValuePairs);
        poster.execute(LOGIN_URL);
    }

    public void loginAsDevicePressed(View view) {
       deviceLogin();
    }

    private void deviceLogin() {
        //TODO login as device
    }

    @Override
    public void onPostDataReceived(String data) {
        if (data.equals("")) {
            saveUsername();
            parent.loginSucessfull();
            return;
        }
        Gson gson = new Gson();
        GitHubService response = gson.fromJson(data, GitHubService.class);
        if (response.getCode() == 3) {
            TextView errors = (TextView) getView().findViewById(R.id.errors);
            errors.setText(response.getMessages());
            errors.setVisibility(View.VISIBLE);
        }
    }
}
