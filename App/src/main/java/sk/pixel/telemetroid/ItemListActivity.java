package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class ItemListActivity extends FragmentActivity
        implements LoginFragment.LoginCallbacks, LoginOptionsListFragment.Callbacks, MainOptionsListFragment.Callbacks, ServerPoster.PostDataListener, LogoutConfirmationDialog.LogoutDialogListener {

    private boolean mTwoPane;
    private LoginFragment loginFragment;
    private MainScreenFragment mainScreenFragment;
    private LogoutConfirmationDialog logoutConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.options_container, new LoginOptionsListFragment(this))
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
    public void onLogoutClicked() {
        FragmentManager fm = getSupportFragmentManager();
        logoutConfirmDialog = new LogoutConfirmationDialog(this);
        logoutConfirmDialog.show(fm, "logout_confirmation");
    }

    public void logout() {
        ServerPoster poster = new ServerPoster(this, null);
        poster.execute("/logout");
    }

    @Override
    public void onPostDataReceived(String data) {
        logoutConfirmDialog.dismiss();
        if (data.equals(ServerPoster.CONNECTION_ERROR)) {
            FragmentManager fm = getSupportFragmentManager();
            ErrorDialog errorDialog = new ErrorDialog("Can't connect to server");
            errorDialog.show(fm, "error_confirmation");
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.item_detail_container));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.options_container, new LoginOptionsListFragment(this))
                .commit();
    }
}
