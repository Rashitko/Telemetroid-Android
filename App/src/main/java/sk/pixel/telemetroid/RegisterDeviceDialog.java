package sk.pixel.telemetroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

public class RegisterDeviceDialog extends DialogFragment implements ServerCommunicator.ServerResponseListener {

    private final DeviceRegistrationListener listener;
    private EditText name;
    private EditText comment;
    private CheckBox publicDevice;

    @Override
    public void onPostDataReceived(String data) {
        Log.d("TAG", data);
    }

    @Override
    public void onConnectionError() {
        Log.e("TAG", "error");
    }

    public interface DeviceRegistrationListener {
        public void onDeviceRegistrationSuccessful();
    }

    public RegisterDeviceDialog(DeviceRegistrationListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_registration, container, false);
        getDialog().setTitle("Register this device");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        getView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        name = (EditText) getView().findViewById(R.id.name);
        comment = (EditText) getView().findViewById(R.id.comment);
        publicDevice = (CheckBox) getView().findViewById(R.id.public_device);
    }

    private void register() {
        RequestParams params = new RequestParams();
        params.put("identifier", getIdentifier());
        params.put("public", true);
        if (name.getText().length() > 0) {
            params.put("name", name.getText().toString());
        }
        if (comment.getText().length() > 0) {
            params.put("comment", comment.getText().toString());
        }
        ServerCommunicator communicator = new ServerCommunicator(this, getActivity());
        communicator.post(communicator.PARAMS_URL, params);
    }

    private String getIdentifier() {

        TelephonyManager tManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }
}
