package sk.pixel.telemetroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        setContentView(button);
    }
}
