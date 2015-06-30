package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ustankovic on 27-05-15.
 */
public class Register extends AsyncTask<String, Void, String> {

    private static final String TAG = Register.class.getSimpleName();

    private Context registerContext;

    public Register(Context context){
        registerContext = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urls[0]);

            StringEntity se = new StringEntity(urls[1]);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            if (inputStream != null)
                result = bufferedReader.readLine();
            else
                result = "Did not work!";
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return result;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(registerContext, "Data Sent!", Toast.LENGTH_LONG).show();
    }
}