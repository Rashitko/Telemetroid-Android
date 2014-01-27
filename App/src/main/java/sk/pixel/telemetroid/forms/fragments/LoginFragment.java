package sk.pixel.telemetroid.forms.fragments;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.server.responses.ServerErrorResponse;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class LoginFragment extends FormFragment {
    private final String TAG = "LoginFragment";

    private static final String USERNAME = "username";
    private static final String SAVE_USERNAME = "save_username";
    private static final String PREFS_NAME = "login_preferences";
    private final LoginCallbacks parent;

    public interface LoginCallbacks {
        public void loginSuccessful();
    }

    public LoginFragment(LoginCallbacks parent) {
        super(R.layout.user_login);
        this.parent = parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        clearEditTextsOnFocus();
        setSaveCheckbox();
        fillUsernameFromPrefs();
    }

    private void clearEditTextsOnFocus() {
        getView().findViewById(R.id.username).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).setText("");
                    ((EditText) getView().findViewById(R.id.password)).setText("");
                    ((CheckBox) getView().findViewById(R.id.rememeber_username)).setChecked(false);
                }
            }
        });
    }

    private void setSaveCheckbox() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        CheckBox save = (CheckBox) getView().findViewById(R.id.rememeber_username);
        if (preferences.contains(SAVE_USERNAME)) {
            save.setChecked(preferences.getBoolean(SAVE_USERNAME, false));
        }
    }

    private void fillUsernameFromPrefs() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        EditText username = (EditText) getView().findViewById(R.id.username);
        if (preferences.contains(USERNAME)) {
            username.setText(preferences.getString(USERNAME, ""));
        }
    }

    public void loginAsUserPressed(View view) {
        execute(ServerCommunicator.LOGIN_URL);
    }

    private void saveUsername() {
        CheckBox checkBox = (CheckBox) getView().findViewById(R.id.rememeber_username);
        if (checkBox.isChecked()) {
            SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            EditText username = (EditText) getView().findViewById(R.id.username);
            editor.putString(USERNAME, username.getText().toString());
            editor.putBoolean(SAVE_USERNAME, true);
            editor.commit();
        }
    }

    protected boolean valid() {
        EditText username = (EditText) getView().findViewById(R.id.username);
        EditText password = (EditText) getView().findViewById(R.id.password);
        if (username.length() < 1) {
            showError("Username is too short");
            return false;
        }
        if (password.length() < 6) {
            showError("Password is too short");
            return false;
        }
        return true;
    }

    @Override
    protected RequestParams prepareParams() {
        EditText username = (EditText) getView().findViewById(R.id.username);
        EditText password = (EditText) getView().findViewById(R.id.password);
        RequestParams result = new RequestParams();
        result.put("username", username.getText().toString());
        result.put("password", password.getText().toString());
        return result;
    }

    @Override
    public void onPostDataReceived(String data) {
        if (data.equals("")) {
            saveUsername();
            parent.loginSuccessful();
            return;
        }
        Gson gson = new Gson();
        ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
        if (response.getCode() == 3) {
            showErrors(response.getMessages());
        }
        showButtons();
    }

}
