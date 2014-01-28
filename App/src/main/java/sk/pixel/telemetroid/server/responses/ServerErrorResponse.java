package sk.pixel.telemetroid.server.responses;


import android.util.Log;

import java.util.Arrays;

public class ServerErrorResponse {
    private final String TAG = "ServerErrorResponse";

    private int code;
    private String[] messages;

    public String toString() {
        String result = "code: " + code + " messages: " + Arrays.toString(messages);
        return result;
    }

    public int getCode() {
        return code;
    }

    public String[] getMessages() {
        return messages;
    }

    public String getMessagesAsString() {
        String result = "";
        for (String m : messages) {
            result += m + "\n";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
