package sk.pixel.telemetroid;


import android.util.Log;

import java.util.Arrays;

public class ServerErrorResponse {
    private int code;
    private String messages;

    public String toString() {
        String result = "code: " + code + " messages: " + messages;
        Log.d("TAG", result);
        return result;
    }

    public int getCode() {
        return code;
    }

    public String getMessages() {
        return messages;
    }
}
