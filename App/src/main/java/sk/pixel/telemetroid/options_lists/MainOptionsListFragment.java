package sk.pixel.telemetroid.options_lists;

import android.view.View;
import android.widget.ListView;

public class MainOptionsListFragment extends OptionsListFragment {

    private final String TAG = "MainOptionsListFragment";

    private static int WHO_POSITION = 0, DEVICE_MANAGMENT_POSITION = 1, USER_PROFILE_POSITION = 2, LOGOUT_POSITION = 3;
    private static final String[] OPTIONS = {"--Who--", "Manage devices", "User profile", "Logout"};
    private static final Integer[] ICONS = {android.R.drawable.ic_menu_info_details, android.R.drawable.ic_menu_view, android.R.drawable.ic_menu_view, android.R.drawable.ic_menu_close_clear_cancel};
    private final Callbacks parent;

    public interface Callbacks {
        public void onLogoutClicked();
        public void onWhoClicked();
        public void onDeviceManagementClicked();
        public void onUserProfileClicked();
    }

    public MainOptionsListFragment(Callbacks parent) {
        super(OPTIONS, ICONS);
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
            parent.onDeviceManagementClicked();
        }
        if (position == USER_PROFILE_POSITION) {
            parent.onUserProfileClicked();
        }
    }
}
