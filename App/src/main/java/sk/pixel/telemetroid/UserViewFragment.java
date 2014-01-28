package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.server.responses.ServerErrorResponse;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class UserViewFragment extends Fragment implements ServerCommunicator.ServerResponseListener {

    private static final String TAG = "UserViewFragment";
    private final UserViewEventsListener listener;
    private EditText username;
    private CheckBox publicMail;
    private BootstrapEditText name;
    private BootstrapEditText comment;
    private BootstrapEditText mail;
    private boolean save = false;

    @Override
    public void onPostDataReceived(String data) {
        fillForm(data);
    }

    private void fillForm(String data) {
        Gson gson = new Gson();
        UserDetails userDetails = gson.fromJson(data, UserDetails.class);
        Log.d(TAG, userDetails.toString());
        username.setText(userDetails.getUsername());
        publicMail.setChecked(userDetails.hasPublicMail());
        if (publicMail.isChecked()) {
            mail.setText(userDetails.getMail());
        }
        name.setText(userDetails.getName());
        comment.setText(userDetails.getComment());
        showButtons();
    }

    private void showButtons() {
        getActivity().findViewById(R.id.submit).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnectionError() {
        Log.e(TAG, "connectionError");
    }

    public interface UserViewEventsListener {
        public String getLoggedUser();
        public void reloadUserView();
    }

    public UserViewFragment(UserViewEventsListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_view, container, false);
        ServerCommunicator communicator = new ServerCommunicator(this, getActivity());
        communicator.getUserDetails(getUserName());
        return rootView;
    }

    private String getUserName() {
        return listener.getLoggedUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        username = (EditText) getView().findViewById(R.id.username);
        publicMail = (CheckBox) getActivity().findViewById(R.id.public_email);
        name = (BootstrapEditText) getView().findViewById(R.id.name);
        comment = (BootstrapEditText) getView().findViewById(R.id.comment);
        mail = (BootstrapEditText) getActivity().findViewById(R.id.mail);
        makeNotEditable(name);
        makeNotEditable(comment);
        makeNotEditable(publicMail);
        getView().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save) {
                    save();
                    return;
                }
                save = true;
                BootstrapButton button = (BootstrapButton) v;
                button.setText("Save");
                button.setBootstrapType("success");
                makeEditable(name);
                makeEditable(comment);
                makeEditable(publicMail);
                username.setEnabled(false);
                mail.setEnabled(false);
                name.requestFocus();
            }
        });
    }

    private void save() {
        RequestParams params = new RequestParams();
        params.put("name", name.getText().toString());
        params.put("comment", comment.getText().toString());
        params.put("public_email", Boolean.toString(publicMail.isChecked()));
        ServerCommunicator communicator = new ServerCommunicator(new ServerCommunicator.ServerResponseListener() {
            @Override
            public void onPostDataReceived(String data) {
                if (data.equals("")) {
                    InfoDialog infoDialog = new InfoDialog("Details updated", "Update successfull", InfoDialog.BUTTON_TYPE_SUCCESS);
                    infoDialog.show(getFragmentManager(), "info_dialog");
                    listener.reloadUserView();
                    return;
                }
                Gson gson = new Gson();
                ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
                showError(response.getMessagesAsString());
            }
            @Override
            public void onConnectionError() {
                showError("Can't connect to server");
            }
        }, getActivity());
        communicator.post(ServerCommunicator.EDIT_USER_URL, params);
    }

    private void showError(String text) {
        TextView errors = (TextView) getView().findViewById(R.id.errors);
        errors.setText(text);
        errors.setVisibility(View.VISIBLE);
    }

    private void makeEditable(View view) {
        changeEditability(view, true);
    }

    private void makeNotEditable(View view) {
        changeEditability(view, false);
    }

    private void changeEditability(View view, boolean value) {
        view.setFocusable(value);
        view.setFocusableInTouchMode(value);
        view.setClickable(value);
    }
}
