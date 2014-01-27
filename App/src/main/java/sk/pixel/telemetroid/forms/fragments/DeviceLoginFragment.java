package sk.pixel.telemetroid.forms.fragments;

import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.server.responses.ServerErrorResponse;
import sk.pixel.telemetroid.utils.DeviceIdentifiers;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class DeviceLoginFragment extends FormFragment {

    private final String TAG = "DeviceLoginFragment";

    private final Callbacks listener;
    private EditText password;

    public interface Callbacks {
        public void onDeviceLoginSuccessful();
    }

    public DeviceLoginFragment(Callbacks listener) {
        super(R.layout.device_login);
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        password = (EditText) getView().findViewById(R.id.password);
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        password.setText(identifiers.getPassword());
    }

    public void loginAsUserPressed(View view) {
        execute(ServerCommunicator.DEVICE_LOGIN_URL);
    }

    protected RequestParams prepareParams() {
        RequestParams params = new RequestParams();
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        String identifier = identifiers.getIdentifier();
        String password = identifiers.getPassword();
        params.put("identifier", identifier);
        params.put("password", password);
        return params;
    }

    @Override
    public void onPostDataReceived(String data) {
        if (data.equals("")) {
            listener.onDeviceLoginSuccessful();
        } else {
            Gson gson = new Gson();
            ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
            showErrors(response.getMessages());
        }

    }

    @Override
    protected boolean valid() {
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        return identifiers.getPassword().length() > 0;
    }

}
