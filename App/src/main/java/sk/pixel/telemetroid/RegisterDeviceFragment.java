package sk.pixel.telemetroid;

import android.support.v4.app.Fragment;

public class RegisterDeviceFragment extends Fragment {

    private final DeviceRegistrationListener listener;

    public interface DeviceRegistrationListener {
        public void onDeviceRegistrationSuccessful();
    }

    public RegisterDeviceFragment(DeviceRegistrationListener listener) {
        this.listener = listener;
    }

}
