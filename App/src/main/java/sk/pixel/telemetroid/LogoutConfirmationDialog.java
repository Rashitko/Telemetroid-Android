package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LogoutConfirmationDialog extends DialogFragment {

    private final LogoutDialogListener parent;

    public interface LogoutDialogListener {
        public void logout();
    }

    public LogoutConfirmationDialog(LogoutDialogListener parent) {
        this.parent = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_confirmation, container);
        getDialog().setTitle("Logout confirmation");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButtonsShowProgress();
                parent.logout();
            }
        });
        getView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void hideButtonsShowProgress() {
        getView().findViewById(R.id.logout).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.cancel).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }
}