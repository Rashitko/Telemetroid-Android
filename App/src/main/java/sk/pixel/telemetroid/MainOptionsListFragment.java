package sk.pixel.telemetroid;

import android.view.View;
import android.widget.ListView;

public class MainOptionsListFragment extends OptionsListFragment {

    private static int WHO_POSITION = 0, DEVICE_MANAGMENT_POSITION = 1, LOGOUT_POSITION = 2;
    private static final String[] OPTIONS = {"--Who--", "Manage devices", "Logout"};
    private final Callbacks parent;

    public interface Callbacks {
        public void onLogoutClicked();
        public void onWhoClicked();
        public void onDeviceManagmentClicked();
    }

    public MainOptionsListFragment(Callbacks parent) {
        super(OPTIONS);
        this.parent = parent;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == LOGOUT_POSITION) {
            parent.onLogoutClicked();
        }
        if (position == WHO_POSITION) {
            parent.onWhoClicked();
        }
        if (position == DEVICE_MANAGMENT_POSITION) {
            parent.onDeviceManagmentClicked();
        }
    }
}
