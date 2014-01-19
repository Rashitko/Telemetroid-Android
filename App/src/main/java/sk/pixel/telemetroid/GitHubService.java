package sk.pixel.telemetroid;


import android.util.Log;

import java.util.Arrays;

public class GitHubService {
    private int code;
    private String messages;

    public String toString() {
        String result = "code: " + code + " messages: " + messages;
        Log.d("TAG", result);
        return result;
    }

}
