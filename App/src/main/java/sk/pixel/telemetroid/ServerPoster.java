package sk.pixel.telemetroid;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by micha_000 on 19.1.2014.
 */
public class ServerPoster extends AsyncTask<String, Void, String> {

    public static final String CONNECTION_ERROR = "error";
    public static final String SERVER_ADDRESS = "http://192.168.0.158:3000";
    private final PostDataListener listener;
    List<NameValuePair> nameValuePairs;

    public ServerPoster(PostDataListener listener, List<NameValuePair> nameValuePairs) {
        this.listener = listener;
        this.nameValuePairs = nameValuePairs;
    }

    public interface PostDataListener {
        public void onPostDataReceived(String data);
    }

    @Override
    protected String doInBackground(String... params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        httpclient.setParams(httpParameters);
        HttpPost httppost = new HttpPost(SERVER_ADDRESS + params[0]);
        String result = CONNECTION_ERROR;
        try {
            if (nameValuePairs != null) {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            HttpResponse response = httpclient.execute(httppost);
            result = EntityUtils.toString(response.getEntity());
            Log.d("TAG", result);
        } catch (ClientProtocolException e) {
            Log.e("TAG", e.toString());
        } catch (IOException e) {
            Log.e("TAG", e.toString());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onPostDataReceived(s);
    }
}
