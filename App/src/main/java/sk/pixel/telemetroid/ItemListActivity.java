package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ItemListActivity extends FragmentActivity
        implements LoginFragment.LoginCallbacks, LoginOptionsListFragment.Callbacks, MainOptionsListFragment.Callbacks, ServerCommunicator.ServerResponseListener, LogoutConfirmationDialog.LogoutDialogListener, RegisterUserFragment.UserSignUpListener {

    private boolean mTwoPane;
    private LoginFragment loginFragment;
    private MainScreenFragment mainScreenFragment;
    private LogoutConfirmationDialog logoutConfirmDialog;
    private RegisterUserFragment registerUserFragment;
    private LoginOptionsListFragment loginOptionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        loginOptionsFragment = new LoginOptionsListFragment(this);
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.options_container, loginOptionsFragment)
                    .commit();
        }
        // TODO: If exposing deep links into your app, handle intents here.
    }

    public void loginAsUserPressed(View view) {
        loginFragment.loginAsUserPressed(view);
    }

    public void loginAsDevicePressed(View view) {
        loginFragment.loginAsDevicePressed(view);
    }

    public void signUpOnClick(View view) {
        registerUserFragment.signUpOnClick(view);
    }

    @Override
    public void loginSucessfull() {
        if (mTwoPane) {
            mainScreenFragment = new MainScreenFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, mainScreenFragment)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.options_container, new MainOptionsListFragment(this))
                    .commit();
        }
    }

    @Override
    public void loginAsUserOptionClicked() {
        if (mTwoPane) {
            loginFragment = new LoginFragment(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, loginFragment)
                    .commit();
        }
    }

    @Override
    public void registerNewUserClicked() {
        if (mTwoPane) {
            registerUserFragment = new RegisterUserFragment(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, registerUserFragment)
                    .commit();
        }
    }

    @Override
    public void onLogoutClicked() {
        FragmentManager fm = getSupportFragmentManager();
        logoutConfirmDialog = new LogoutConfirmationDialog(this);
        logoutConfirmDialog.show(fm, "logout_confirmation");
    }

    @Override
    public void onWhoClicked() {
        ServerCommunicator poster = new ServerCommunicator(this, this);
        poster.get(ServerCommunicator.WHO_URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d("TAG", content);
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e("TAG", "/who failed");
            }
        });
    }

    public void logout() {
        ServerCommunicator poster = new ServerCommunicator(this, this);
        poster.post(ServerCommunicator.LOGOUT_URL, null);
    }

    @Override
    public void onPostDataReceived(String data) {
        logoutConfirmDialog.dismiss();
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.item_detail_container));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.options_container, loginOptionsFragment)
                    .commit();
        }
    }

    @Override
    public void onConnectionError() {
        logoutConfirmDialog.dismiss();
        FragmentManager fm = getSupportFragmentManager();
        InfoDialog infoDialog = new InfoDialog("Can't connect to server", "Error occurred", InfoDialog.BUTTON_TYPE_DANGER);
        infoDialog.show(fm, "error_confirmation");
    }

    @Override
    public void signUpComplete() {
        FragmentManager fm = getSupportFragmentManager();
        InfoDialog infoDialog = new InfoDialog("Now you can log in", "Registration successful", InfoDialog.BUTTON_TYPE_SUCCESS);
        infoDialog.show(fm, "inf_confirmation");
        loginOptionsFragment.setActivatedPosition(LoginOptionsListFragment.USER_LOGIN_POSITION);
        loginAsUserOptionClicked();
    }
}
