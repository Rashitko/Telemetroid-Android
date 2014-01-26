package sk.pixel.telemetroid.forms.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.InfoDialog;
import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.server.responses.ServerErrorResponse;
import sk.pixel.telemetroid.utils.DeviceIdentifiers;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class RegisterDeviceDialog extends DialogFragment implements ServerCommunicator.ServerResponseListener {
    private final String TAG = "RegisterDeviceDialog";

    private EditText name;
    private EditText comment;
    private CheckBox publicDevice;

    @Override
    public void onPostDataReceived(String data) {
        showButton();
        try {
            Gson gson = new Gson();
            ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
            showErrors(response.getMessages());
        } catch (Exception e) {
            savePassword(data);
        }
        dismiss();
        InfoDialog dialog = new InfoDialog("Device was registered", "Registration successful", InfoDialog.BUTTON_TYPE_SUCCESS);
        dialog.show(getActivity().getSupportFragmentManager(), "info_dialog");
    }

    private void savePassword(String data) {
        Gson gson = new Gson();
        DeviceIdentifiers deviceIdentifiers = gson.fromJson(data, DeviceIdentifiers.class);
        deviceIdentifiers.setContext(getActivity());
        deviceIdentifiers.save();
        Log.d(TAG, deviceIdentifiers.getPassword());
    }

    private void showErrors(String[] messages) {
        String text = "";
        for (String m : messages) {
            text += m +"\n";
        }
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
        TextView errors = (TextView) getView().findViewById(R.id.errors);
        errors.setVisibility(View.VISIBLE);
        errors.setText(text);
    }

    @Override
    public void onConnectionError() {
        showButton();
        InfoDialog dialog = new InfoDialog("Can't connect to server", "Error", InfoDialog.BUTTON_TYPE_DANGER);
        dialog.show(getActivity().getSupportFragmentManager(), "error_dialog");
        Log.e("TAG", "error");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_registration, container, false);
        getDialog().setTitle("Register this device - fields are optional");
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
        hideButton();
        RequestParams params = new RequestParams();
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        params.put("identifier", identifiers.getIdentifier());
        params.put("public", Boolean.toString(publicDevice.isChecked()));
        if (name.getText().length() > 0) {
            params.put("name", name.getText().toString());
        }
        if (comment.getText().length() > 0) {
            params.put("comment", comment.getText().toString());
        }
        ServerCommunicator communicator = new ServerCommunicator(this, getActivity());
        communicator.post(ServerCommunicator.REGISTER_DEVICE_URL, params);
    }



    private void hideButton() {
        getView().findViewById(R.id.register).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.cancel).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void showButton() {
        getView().findViewById(R.id.register).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }
}
