package sk.pixel.telemetroid.forms.fragments;

import android.view.View;
import android.widget.CheckBox;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import sk.pixel.telemetroid.R;
import sk.pixel.telemetroid.server.responses.ServerErrorResponse;
import sk.pixel.telemetroid.utils.ServerCommunicator;

public class RegisterUserFragment extends FormFragment {
    private final String TAG = "RegisterUserFragment";

    private BootstrapEditText username;
    private BootstrapEditText password;
    private BootstrapEditText passwordConfirmation;
    private BootstrapEditText mail;
    private BootstrapEditText name;
    private BootstrapEditText comment;
    private CheckBox publicEmail;
    private String errors;
    private UserSignUpListener parent;

    public interface UserSignUpListener {
        public void signUpComplete();
    }

    public RegisterUserFragment(UserSignUpListener parent) {
        super(R.layout.user_registration);
        this.parent = parent;
    }

    public void signUpOnClick(View view) {
        sendData(ServerCommunicator.REGISTER_USER_URL);
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
                    validatePasswordPresence();
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

    protected boolean valid() {
        errors = "";
        errors = validateUsername();
        validatePasswordConfirmation();
        validatePasswordPresence();
        validateMailPresence();
        validateMail();
        if (errors.equals("")) {
            hideError();
            return true;
        }
        errors = errors.substring(0, errors.length() - 1);
        showError(errors);
        return false;
    }

    @Override
    protected RequestParams prepareParams() {
        RequestParams params = new RequestParams();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        params.put("mail", mail.getText().toString());
        params.put("name", name.getText().toString());
        params.put("comment", comment.getText().toString());
        params.put("public_email", publicEmail.isChecked());
        return params;
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
            return false;
        } else {
            setSuccess(password);
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

    @Override
    public void onPostDataReceived(String data) {
        if (data.equals("")) {
            parent.signUpComplete();
        } else {
            setAllToDefaultState();
            Gson gson = new Gson();
            ServerErrorResponse response = gson.fromJson(data, ServerErrorResponse.class);
            if (response.getCode() == 1) {
                showErrors(response.getMessages());
            }
        }
        showButtons();
    }

    private void setAllToDefaultState() {
        username.setState(BootstrapEditText.BOOTSTRAP_EDIT_TEXT_DEFAULT);
        password.setState(BootstrapEditText.BOOTSTRAP_EDIT_TEXT_DEFAULT);
        passwordConfirmation.setState(BootstrapEditText.BOOTSTRAP_EDIT_TEXT_DEFAULT);
        mail.setState(BootstrapEditText.BOOTSTRAP_EDIT_TEXT_DEFAULT);
    }

}
