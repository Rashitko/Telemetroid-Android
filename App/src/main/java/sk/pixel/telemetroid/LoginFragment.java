package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment implements ServerPoster.PostDataListener {
    public static final String LOGIN_TYPE = "login_type";
    public static final String LOGIN_URL = "/login";
    private int loginType;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(LOGIN_TYPE)) {
            loginType = getArguments().getInt(LOGIN_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = null;
        if (loginType == ItemListFragment.USER_LOGIN_POSTITION) {
            rootView = inflater.inflate(R.layout.user_login, container, false);
        }
        if (loginType == ItemListFragment.DEVICE_LOGIN_POSTITION) {
            rootView = inflater.inflate(R.layout.device_login, container, false);
        }
        return rootView;
    }

    public void loginAsUserPressed(View view) {
        userLogin();
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
        Log.d("TAG", data);
    }
}
