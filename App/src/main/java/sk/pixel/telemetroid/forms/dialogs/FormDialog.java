package sk.pixel.telemetroid.forms.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.InfoDialog;
import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public abstract class FormDialog extends DialogFragment implements ServerCommunicator.ServerResponseListener {

    private int layoutId;
    private String url;

    protected abstract RequestParams prepareParams();
    protected abstract boolean valid();
    public abstract void onPostDataReceived(String data);

    public FormDialog(int layoutId, String url) {
        this.layoutId = layoutId;
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutId, container, false);
        getDialog().setTitle("Register this device - fields are optional");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute();
            }
        });
        getView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void execute() {
        if (!valid()) {
            return;
        }
        RequestParams params = prepareParams();
        ServerCommunicator communicator = new ServerCommunicator(this, getActivity());
        communicator.post(url, params);
        hideButtons();
    }

    @Override
    public void onConnectionError() {
        showButtons();
        InfoDialog dialog = new InfoDialog("Can't connect to server", "Error", InfoDialog.BUTTON_TYPE_DANGER);
        dialog.show(getActivity().getSupportFragmentManager(), "error_dialog");
    }

    protected void showButtons() {
        getView().findViewById(R.id.submit).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    protected void hideButtons() {
        getView().findViewById(R.id.submit).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.cancel).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

}
