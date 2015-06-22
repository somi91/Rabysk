package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uslive.rabyks.R;
import com.uslive.rabyks.activities.MainActivity;
import com.uslive.rabyks.models.Partner;

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
public class GetPartners extends AsyncTask<String, String, JSONArray> {
    Context context;
    OnTaskCompletedUpdateGridView listener;
    ProgressBar bar;

    public GetPartners(Context c, OnTaskCompletedUpdateGridView l, ProgressBar b){
        context = c;
        listener = l;
        bar = b;
    }
//    JSONParser jsonParser = new JSONParser();

    JSONArray result;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected JSONArray doInBackground(String... urls) {
        InputStream inputStream;

        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(context.getString(R.string.serverIP)+"/getPartners");
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }else
                result = new JSONArray();
        } catch (Exception ex) {
            Log.d("HTTP POST ERROR", ex.getMessage());
        }
        return result;
    }

    protected void onPostExecute(JSONArray results) {
        bar.setVisibility(View.GONE);
        if (results.length() != 0 && results != null) {
            for (int i = 0; i < results.length(); i++) {
                Partner partner = new Partner();
                try {
                    partner.setPartner_id(results.getJSONObject(i).getInt("id"));
                    partner.setName(results.getJSONObject(i).getString("name"));
                    partner.setAddress(results.getJSONObject(i).getString("address"));
                    partner.setImageUrl(results.getJSONObject(i).getString("logoUrl"));
                    partner.setCreated_at(results.getJSONObject(i).getLong("createdAt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new FetchPartnerData(context, listener, bar).execute(partner);
            }
        } else {
            listener.onTaskCompletedUpdateGridView();
        }
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
