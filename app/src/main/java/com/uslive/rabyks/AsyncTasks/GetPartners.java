package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by milos on 6/18/2015.
 */
public class GetPartners extends AsyncTask<String, String, String> {
    Context context;
    public GetPartners(Context c){
        context = c;
    }
//    JSONParser jsonParser = new JSONParser();

    JSONArray result;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        InputStream inputStream;

        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet("http://ec2-52-26-233-225.us-west-2.compute.amazonaws.com:80/getPartners");
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
//            HttpPost httpPost = new HttpPost(urls[0]);
//
//            StringEntity se = new StringEntity(urls[1]);
//            httpPost.setEntity(se);
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }else
                result = new JSONArray();
        } catch (Exception ex) {
            Log.d("HTTP POST ERROR", ex.getMessage());
        }
        return result.toString();
    }

    protected void onPostExecute(String name) {
        Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
        Log.i("SUCCESS or NO SUCCESS","IZVRSIO SE USPESNO ILI NE USPESNO ALI SE IZVRSIO");
    }

    private JSONArray convertInputStreamToString(InputStream inputStream) throws Exception{
        InputStream is = null;
        JSONObject jObj = null;
        JSONArray jArray = null;
        String json = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
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
