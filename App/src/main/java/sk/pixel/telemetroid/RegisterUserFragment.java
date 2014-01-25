package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapEditText;

public class RegisterUserFragment extends Fragment {

    private BootstrapEditText username;
    private BootstrapEditText password;
    private BootstrapEditText passwordConfirmation;
    private BootstrapEditText mail;
    private BootstrapEditText name;
    private BootstrapEditText comment;

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
            //signUp();
            Log.d("TAG", "valid");
        } else {
            Log.d("TAG", "form invalid");
        }
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
        password = (BootstrapEditText) getView().findViewById(R.id.password);
        passwordConfirmation = (BootstrapEditText) getView().findViewById(R.id.password_confirmation);
        mail = (BootstrapEditText) getView().findViewById(R.id.mail);
        name = (BootstrapEditText) getView().findViewById(R.id.name);
        comment = (BootstrapEditText) getView().findViewById(R.id.comment);
    }

    private boolean valid() {
        String errors = "";
        if (username.getText().length() < 2) {
            errors += "Username must contains at least 2 characters" + "\n";
            setDanger(username);
        } else {
            setSuccess(username);
        }
        if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
            errors += "Passwords don't match" + "\n";
            setDanger(password);
            setDanger(passwordConfirmation);
        } else {
            setSuccess(password);
            setSuccess(passwordConfirmation);
        }
        if (password.getText().length() < 6) {
            errors += "Password must contains at least 6 characters" + "\n";
            setDanger(password);
            setDanger(passwordConfirmation);
        } else {
            setSuccess(password);
            setSuccess(passwordConfirmation);
        }
        if (mail.getText().length() == 0) {
            errors += "E-mail address must be entered" + "\n";
            setDanger(mail);
        } else {
            setSuccess(mail);
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
            errors += "Entered e-mail address is not valid" + "\n";
            setDanger(mail);
        } else {
            setSuccess(mail);
        }
        if (errors.equals("")) {
            hideErrors();
            return true;
        }
        errors = errors.substring(0, errors.length() - 1);
        showErrors(errors);
        return false;
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

}
