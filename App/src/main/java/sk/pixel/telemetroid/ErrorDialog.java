package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ErrorDialog extends DialogFragment {

    private final String message;

    public ErrorDialog(String message) {
        this.message = message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_dialog, container);
        getDialog().setTitle("Error occured");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView)getView().findViewById(R.id.message)).setText(message);
        getView().findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}