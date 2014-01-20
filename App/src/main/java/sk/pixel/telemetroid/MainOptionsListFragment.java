package sk.pixel.telemetroid;

import android.view.View;
import android.widget.ListView;

public class MainOptionsListFragment extends OptionsListFragment {

    private static int LOGOUT_POSITION = 0;
    private static final String[] OPTIONS = {"Logout"};
    private final Callbacks parent;

    public interface Callbacks {
        public void onLogoutClicked();
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
    }
}
