package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.uslive.rabyks.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by milos on 7/1/2015.
 */
public class RemoveWaiter extends AsyncTask<String, Void, String> {
    private Context context;
    private OnRemoveWaiterCompleted listener;

    public RemoveWaiter(Context c, OnRemoveWaiterCompleted l){
        context = c;
        listener = l;
    }

    @Override
    protected String doInBackground(String... params) {
        String waiterName = params[0];
        String address = context.getString(R.string.serverIP) + "/removeWaiter";
        // Jos treba menjati na dole...
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(address);

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("waiterName", waiterName));
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

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
        listener.OnRemoveWaiterCompleted(result);
    }
}