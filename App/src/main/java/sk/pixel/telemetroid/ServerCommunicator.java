package sk.pixel.telemetroid;

import android.content.Context;

import com.loopj.android.http.*;

import org.apache.http.Header;

public class ServerCommunicator {

    private static final String BASE_URL = "http://10.0.0.145:3000";
    public static final String LOGIN_URL = "/login", LOGOUT_URL = "/logout";
    public static final String WHO_URL = "/who", PARAMS_URL = "/params";
    public static final String REGISTER_USER_URL = "/users/new";
    private final ServerResponseListener listener;
    private AsyncHttpClient client;
    private final AsyncHttpResponseHandler responseHandler;

    public ServerCommunicator(ServerResponseListener parent, Context context) {
        this.listener = parent;
        client = new AsyncHttpClient();
        client.setTimeout(4000);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
        responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                listener.onPostDataReceived(content);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onConnectionError();
            }
        };
    }

    public interface ServerResponseListener {
        public void onPostDataReceived(String data);
        public void onConnectionError();
    }

    public void get(String url, RequestParams params) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
