package sk.pixel.telemetroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks {

    private boolean mTwoPane;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public void onItemSelected(int id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(LoginFragment.LOGIN_TYPE, id);
            loginFragment = new LoginFragment();
            loginFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, loginFragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(LoginFragment.LOGIN_TYPE, id);
            startActivity(detailIntent);
        }
    }

    public void loginAsUserPressed(View view) {
        loginFragment.loginAsUserPressed(view);
    }

    public void loginAsDevicePressed(View view) {
        loginFragment.loginAsDevicePressed(view);
    }

}
