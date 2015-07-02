package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.uslive.rabyks.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by marezina on 2.7.2015.
 */
public class GetWaiters extends AsyncTask<String, String, JSONArray> {
    Context context;
    OnGetWaitersCompleted listener;

    public GetWaiters(Context c, OnGetWaitersCompleted l){
        context = c;
        listener = l;
    }
//    JSONParser jsonParser = new JSONParser();

    JSONArray result;

    @Override
    protected JSONArray doInBackground(String... urls) {
        InputStream inputStream;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(context.getString(R.string.getWaiters) +"/" + urls[0]);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }else
                result = new JSONArray();
        } catch (Exception ex) {
            Log.e("HTTP GET ERROR", ex.getMessage());
        }
        return result;
    }

    protected void onPostExecute(JSONArray results) {
        if (results != null && results.length() != 0) {
            listener.OnGetWaitersCompleted(results);
        }
    }

    private JSONArray convertInputStreamToString(InputStream inputStream) throws Exception{
        JSONArray jArray;
        String json;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            json = sb.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        try {
            jArray = new JSONArray(json);
        } catch (JSONException e) {
            throw new Exception(e.getMessage());
        }

        // return JSON String
        return jArray;

    }
}
