package sk.pixel.telemetroid.forms.dialogs;

import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.InfoDialog;
import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.server.responses.ServerErrorResponse;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class ChangePasswordDialog extends FormDialog {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newConfirmation;

    public ChangePasswordDialog() {
        super(R.layout.change_password, ServerCommunicator.CHANGE_PASSWORD_URL, "Change password");
    }

    @Override
    public void onStart() {
        super.onStart();
        oldPassword = (EditText) getView().findViewById(R.id.old_password);
        newPassword = (EditText) getView().findViewById(R.id.password);
        newConfirmation = (EditText) getView().findViewById(R.id.password_confirmation);
    }

    @Override
    protected RequestParams prepareParams() {
        RequestParams params = new RequestParams();
        params.put("old", oldPassword.getText().toString());
        params.put("new", newPassword.getText().toString());
        return params;
    }

    @Override
    protected boolean valid() {
        if (oldPassword.getText().length() < 6 || newPassword.getText().length() < 6 || newConfirmation.getText().length() < 6) {
            showError("Password is too short");
            return false;
        }
        if (newPassword.getText().toString().equals(newConfirmation.getText().toString())) {
            return true;
        }
        showError("Passwords don't match");
        return false;
    }

    @Override
    public void onPostDataReceived(String data) {
        if (data.equals("")) {
            InfoDialog infoDialog = new InfoDialog("Password successfully changed", "Password changed", InfoDialog.BUTTON_TYPE_SUCCESS);
            infoDialog.show(getActivity().getSupportFragmentManager(), "password_change");
            dismiss();
            return;
        }
        Gson gson = new Gson();
        ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
        showErrors(response.getMessages());
        showButtons();
    }
}
