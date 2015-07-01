package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by marezina on 1.7.2015.
 */
public class AddWaiter extends AsyncTask<String, Void, String> {
    private Context context;
    private OnAddWaiterCompleted listener;

    public AddWaiter(Context c, OnAddWaiterCompleted l){
        context = c;
        listener = l;
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
            HttpPost httpPost = new HttpPost(address);

            StringEntity se = new StringEntity(jsonArray);
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
        Toast.makeText(context, "Data Sent!", Toast.LENGTH_LONG).show();
        listener.OnAddWaiterCompleted();
    }
}
