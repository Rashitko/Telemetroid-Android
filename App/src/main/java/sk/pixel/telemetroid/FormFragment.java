package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;


public abstract class FormFragment extends Fragment implements ServerCommunicator.ServerResponseListener {

    private final int viewResource;

    @Override
    public abstract void onPostDataReceived(String data);

    protected abstract boolean valid();

    protected abstract RequestParams prepareParams();

    public FormFragment(int viewResource) {
        this.viewResource = viewResource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(viewResource, container, false);
        return rootView;
    }

    protected void sendData(String url) {
        if (!valid()) {
            return;
        }
        RequestParams params = prepareParams();
        ServerCommunicator poster = new ServerCommunicator(this, getActivity());
        poster.post(url, params);
        hideButtons();
    }

    @Override
    public void onConnectionError() {
        showButtons();
        showError("Can't connect to server");
    }

    protected void showButtons() {
        getView().findViewById(R.id.submit).setVisibility(View.VISIBLE);
        if (getView().findViewById(R.id.cancel) != null) {
            getView().findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        }
        getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    protected void hideButtons() {
        getView().findViewById(R.id.submit).setVisibility(View.INVISIBLE);
        if (getView().findViewById(R.id.cancel) != null) {
            getView().findViewById(R.id.cancel).setVisibility(View.INVISIBLE);
        }
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    protected void showError(String text) {
        TextView errors = (TextView) getView().findViewById(R.id.errors);
        errors.setText(text);
        errors.setVisibility(View.VISIBLE);
    }

}
