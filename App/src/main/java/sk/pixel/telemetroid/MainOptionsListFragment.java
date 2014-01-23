package sk.pixel.telemetroid;

import android.view.View;
import android.widget.ListView;

public class MainOptionsListFragment extends OptionsListFragment {

    private static int WHO_POSITION = 0, LOGOUT_POSITION = 1;
    private static final String[] OPTIONS = {"--Who--", "Logout"};
    private final Callbacks parent;

    public interface Callbacks {
        public void onLogoutClicked();
        public void onWhoClicked();
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
    }
}
