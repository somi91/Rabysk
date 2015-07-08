package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.uslive.rabyks.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ustankovic on 08-07-15.
 */
public class GetTypes extends AsyncTask<String, Void, String> {

    private static final String TAG = GetTypes.class.getSimpleName();

    private Context context;

    public GetTypes(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(context.getString(R.string.serverIP) + "/getTypes");
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            if (is != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String result = bufferedReader.readLine();

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
