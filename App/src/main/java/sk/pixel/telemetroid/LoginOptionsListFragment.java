package sk.pixel.telemetroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class LoginOptionsListFragment extends OptionsListFragment{

    public static final int USER_LOGIN_POSITION = 0, DEVICE_LOGIN_POSITION = 1, REGISTRATION_POSITION = 2;
    private static final String[] OPTIONS = new String[]{"Sign in as user", "Sign in as device", "Sign up"};
    private Callbacks listener;

    public interface Callbacks {
        public void loginAsUserOptionClicked();
        public void loginAsDeviceOptionClicked();
        public void registerNewUserClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public LoginOptionsListFragment(Callbacks listener) {
        super(OPTIONS);
        this.listener = listener;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == USER_LOGIN_POSITION) {
            listener.loginAsUserOptionClicked();
        }
        if (position == DEVICE_LOGIN_POSITION){
            listener.loginAsDeviceOptionClicked();
        }
        if (position == REGISTRATION_POSITION) {
            listener.registerNewUserClicked();
        }
    }
}
