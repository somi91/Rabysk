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
 * Created by marezina on 15.7.2015.
 */
public class ArrivalConfirmation extends AsyncTask<String, Void, String> {

    private Context context;

    public ArrivalConfirmation(Context c){
        context = c;
    }

    @Override
    protected String doInBackground(String... params) {
        String partnerId = params[0];
        String objectId = params[1];
        String type = params[2];
        String address = context.getString(R.string.serverIP) + "/reservationSuccess";
        // Jos treba menjati na dole...
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(address);

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("partnerId", partnerId));
            postParameters.add(new BasicNameValuePair("objectId", objectId));
            postParameters.add(new BasicNameValuePair("type", type));

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
        Toast.makeText(context, "Confirmed arrival!", Toast.LENGTH_LONG).show();
    }
}
