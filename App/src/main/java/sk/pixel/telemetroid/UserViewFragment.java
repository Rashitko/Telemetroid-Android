package sk.pixel.telemetroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.pixel.telemetroid.utils.ServerCommunicator;

public class UserViewFragment extends Fragment {

    private final UserViewEventsListener listener;

    public interface UserViewEventsListener {

    }

    public UserViewFragment(UserViewEventsListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_view, container, false);
//        ServerCommunicator communicator = new ServerCommunicator(this);
//        communicator.getUserDetails(getUserName());
        return rootView;
    }


}
