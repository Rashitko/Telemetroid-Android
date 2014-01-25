package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class RegisterUserFragment extends Fragment implements ServerCommunicator.ServerResponseListener {

    private BootstrapEditText username;
    private BootstrapEditText password;
    private BootstrapEditText passwordConfirmation;
    private BootstrapEditText mail;
    private BootstrapEditText name;
    private BootstrapEditText comment;
    private CheckBox publicEmail;
    private String errors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_registration, container, false);
        return rootView;
    }

    public void signUpOnClick(View view) {
        if (valid()) {
            signUp();
        }
    }

    private void signUp() {
        hideErrors();
        hideButton();
        RequestParams params = new RequestParams();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        params.put("mail", mail.getText().toString());
        params.put("name", name.getText().toString());
        params.put("comment", comment.getText().toString());
        params.put("public_email", publicEmail.isChecked());
        ServerCommunicator communicator = new ServerCommunicator(this, getActivity());
        communicator.post(ServerCommunicator.REGISTER_USER_URL, params);
    }

    private void hideErrors() {
        getView().findViewById(R.id.errors).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        username = (BootstrapEditText) getView().findViewById(R.id.username);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateUsername();
                }
            }
        });
        password = (BootstrapEditText) getView().findViewById(R.id.password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (validatePasswordPresence()) {
                        validatePasswordConfirmation();
                    }
                }
            }
        });
        passwordConfirmation = (BootstrapEditText) getView().findViewById(R.id.password_confirmation);
        passwordConfirmation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (validatePasswordPresence()) {
                        validatePasswordConfirmation();
                    }
                }
            }
        });
        mail = (BootstrapEditText) getView().findViewById(R.id.mail);
        mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateMailPresence();
                    validateMail();
                }
            }
        });
        name = (BootstrapEditText) getView().findViewById(R.id.name);
        comment = (BootstrapEditText) getView().findViewById(R.id.comment);
        publicEmail = (CheckBox) getView().findViewById(R.id.public_email);
    }

    private boolean valid() {
        errors = "";
        errors = validateUsername();
        validatePasswordConfirmation();
        validatePasswordPresence();
        validateMailPresence();
        validateMail();
        if (errors.equals("")) {
            hideErrors();
            return true;
        }
        errors = errors.substring(0, errors.length() - 1);
        showErrors(errors);
        return false;
    }

    private void validatePasswordConfirmation() {
        if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
            errors += "Passwords don't match" + "\n";
            setDanger(password);
            setDanger(passwordConfirmation);
        } else {
            setSuccess(password);
            setSuccess(passwordConfirmation);
        }
    }

    private void validateMail() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
            errors += "Entered e-mail address is not valid" + "\n";
            setDanger(mail);
        } else {
            setSuccess(mail);
        }
    }

    private void validateMailPresence() {
        if (mail.getText().length() == 0) {
            errors += "E-mail address must be entered" + "\n";
            setDanger(mail);
        } else {
            setSuccess(mail);
        }
    }

    private boolean validatePasswordPresence() {
        if (password.getText().length() < 6) {
            errors += "Password must contains at least 6 characters" + "\n";
            setDanger(password);
            setDanger(passwordConfirmation);
            return false;
        } else {
            setSuccess(password);
            setSuccess(passwordConfirmation);
            return true;
        }
    }

    private String validateUsername() {
        if (username.getText().length() < 2) {
            errors += "Username must contains at least 2 characters" + "\n";
            setDanger(username);
        } else {
            setSuccess(username);
        }
        return errors;
    }

    private void setSuccess(BootstrapEditText editText) {
        editText.setState(BootstrapEditText.BOOTSTRAP_EDIT_TEXT_SUCCESS);
    }

    private void setDanger(BootstrapEditText editText) {
        editText.setState(BootstrapEditText.BOOTSTRAP_EDIT_TEXT_DANGER);
    }

    private void showErrors(String errors) {
        getView().findViewById(R.id.errors).setVisibility(View.VISIBLE);
        ((TextView) getView().findViewById(R.id.errors)).setText(errors);
    }

    @Override
    public void onPostDataReceived(String data) {
        Log.d("TAG", data);
        if (data.equals("")) {
            Log.d("TAG", "registered");
        } else {
            Gson gson = new Gson();
            ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
            if (response.getCode() == 1) {
                String serverErrors = "";
                for (int i = 0; i < response.getMessages().length; i++) {
                    serverErrors += response.getMessages()[i] + "\n";
                }
                if (serverErrors.length() > 0) {
                    serverErrors = serverErrors.substring(0, serverErrors.length() - 1);
                }
                showErrors(serverErrors);
            }
        }
        showButton();
    }

    @Override
    public void onConnectionError() {
        showButton();
        showErrors("Can't connect to server");
        Log.e("TAG", "error");
    }

    private void showButton() {
        getView().findViewById(R.id.submit).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    private void hideButton() {
        getView().findViewById(R.id.submit).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }
}
