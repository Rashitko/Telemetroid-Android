package sk.pixel.telemetroid.forms.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public abstract class FormDialog extends DialogFragment implements ServerCommunicator.ServerResponseListener {

    private final String title;
    private int layoutId;
    private String url;

    protected abstract RequestParams prepareParams();
    protected abstract boolean valid();
    public abstract void onPostDataReceived(String data);

    public FormDialog(int layoutId, String url, String title) {
        this.layoutId = layoutId;
        this.url = url;
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutId, container, false);
        getDialog().setTitle(title);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
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
        showError("Can't connect to server");
    }

    protected void showErrors(String[] messages) {
        String text = "";
        for (String m : messages) {
            text += m +"\n";
        }
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
        showError(text);
    }

    protected void showError(String text) {
        TextView errors = (TextView) getView().findViewById(R.id.errors);
        errors.setVisibility(View.VISIBLE);
        errors.setText(text);
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
