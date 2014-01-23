package sk.pixel.telemetroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class LoginOptionsListFragment extends OptionsListFragment{

    public static final int USER_LOGIN_POSITION = 0, REGISTRATION_POSITION = 1;
    private static final String[] OPTIONS = new String[]{"Sign in as user", "Sign up"};
    private Callbacks parent;

    public interface Callbacks {
        public void loginAsUserOptionClicked();
        public void registerNewUserClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public LoginOptionsListFragment(Callbacks parent) {
        super(OPTIONS);
        this.parent = parent;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == USER_LOGIN_POSITION) {
            parent.loginAsUserOptionClicked();
        }
        if (position == REGISTRATION_POSITION) {
            parent.registerNewUserClicked();
        }
    }
}
