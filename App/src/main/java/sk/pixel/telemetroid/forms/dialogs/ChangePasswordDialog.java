package sk.pixel.telemetroid.forms.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class ChangePasswordDialog extends DialogFragment {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newConfirmation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.change_password, container, false);
        getDialog().setTitle("Register this device - fields are optional");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        getView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        oldPassword = (EditText) getView().findViewById(R.id.old_password);
        newPassword = (EditText) getView().findViewById(R.id.password);
        newConfirmation = (EditText) getView().findViewById(R.id.password_confirmation);
    }

    private void change() {
        
    }
}
