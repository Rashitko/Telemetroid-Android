package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class DeviceLoginFragment extends Fragment implements ServerCommunicator.ServerResponseListener {

    private final Callbacks listener;
    private EditText password;

    public interface Callbacks {
        public void onDeviceLoginSuccessful();
    }

    public DeviceLoginFragment(Callbacks listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_login, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        password = (EditText) getView().findViewById(R.id.password);
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        password.setText(identifiers.getPassword());
    }

    public void loginAsUserPressed(View view) {

        if (password.getText().length() > 0) {
            login();
        } else {
            showError(new String[] {"Password is not saved. Please log in as owner and reset it"});
        }
    }

    private void login() {
        hideButton();
        RequestParams params = new RequestParams();
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        String identifier = identifiers.getIdentifier();
        String password = identifiers.getPassword();
        params.put("identifier", identifier);
        params.put("password", password);
        ServerCommunicator communicator = new ServerCommunicator(this, getActivity());
        communicator.post(ServerCommunicator.DEVICE_LOGIN_URL, params);
    }

    @Override
    public void onPostDataReceived(String data) {
        Log.d("TAG", data);
        if (data.equals("")) {
            listener.onDeviceLoginSuccessful();
        } else {
            Gson gson = new Gson();
            ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
            showError(response.getMessages());
        }

    }

    private void showError(String[] messages) {
        showButton();
        String text = "";
        for (String m : messages) {
            text += m + "\n";
        }
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
        TextView errors = (TextView) getView().findViewById(R.id.errors);
        errors.setVisibility(View.VISIBLE);
        errors.setText(text);
    }

    private void showButton() {
        getView().findViewById(R.id.login).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    private void hideButton() {
        getView().findViewById(R.id.login).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionError() {
        showError(new String[] {"Can't connect to server"});
    }

}
