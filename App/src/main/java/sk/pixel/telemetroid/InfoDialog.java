package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

public class InfoDialog extends DialogFragment {

    private final String TAG = "InfoDialog";

    public static final String BUTTON_TYPE_DANGER = "danger", BUTTON_TYPE_SUCCESS = "success";

    private final String message;
    private final String title;
    private final String type;

    public InfoDialog(String message, String title, String type) {
        this.message = message;
        this.title = title;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_dialog, container);
        getDialog().setTitle(title);
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
        ((BootstrapButton) getView().findViewById(R.id.confirm)).setBootstrapType(type);
    }
}