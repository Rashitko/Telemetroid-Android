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

public class RegisterDeviceDialog extends FormDialog {
    private final String TAG = "RegisterDeviceDialog";

    private EditText name;
    private EditText comment;
    private CheckBox publicDevice;

    public RegisterDeviceDialog() {
        super(R.layout.device_registration, ServerCommunicator.REGISTER_DEVICE_URL);
    }

    @Override
    public void onPostDataReceived(String data) {
        showButtons();
        try {
            Gson gson = new Gson();
            ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
            showErrors(response.getMessages());
            return;
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

    @Override
    public void onStart() {
        super.onStart();
        name = (EditText) getView().findViewById(R.id.name);
        comment = (EditText) getView().findViewById(R.id.comment);
        publicDevice = (CheckBox) getView().findViewById(R.id.public_device);
    }

    @Override
    protected RequestParams prepareParams() {
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
        return params;
    }

    @Override
    protected boolean valid() {
        DeviceIdentifiers identifiers = new DeviceIdentifiers(getActivity());
        if (identifiers.getIdentifier().length() > 0) {
            return true;
        }
        showErrors(new String[] {"Can't find identifier of this device. Please report this bug"});
        return false;
    }
}
