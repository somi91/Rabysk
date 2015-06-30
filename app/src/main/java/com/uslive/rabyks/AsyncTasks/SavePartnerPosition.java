package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.uslive.rabyks.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by marezina on 30.6.2015.
 */
public class SavePartnerPosition extends AsyncTask<String, Void, String> {
    private Context context;

    public SavePartnerPosition(Context c){
        context = c;
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonArray = params[0];
        String address = context.getString(R.string.serverIP) + "/postPartnerObjectSetup";
        // Jos treba menjati na dole...
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);

            StringEntity se = new StringEntity(params[1]);
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
//            Log.e(TAG, ex.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
//        Toast.makeText(registerContext, "Data Sent!", Toast.LENGTH_LONG).show();
    }
}
