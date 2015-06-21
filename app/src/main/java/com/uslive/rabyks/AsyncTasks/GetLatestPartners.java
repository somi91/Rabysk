package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
import java.net.URLEncoder;

/**
 * Created by milos on 6/20/2015.
 */
public class GetLatestPartners extends AsyncTask<String, String, JSONArray> {
    Context context;
    OnTaskCompletedUpdateGridView listener;
    public GetLatestPartners(Context c, OnTaskCompletedUpdateGridView l){
        context = c;
        listener = l;
    }
//    JSONParser jsonParser = new JSONParser();

    JSONArray result;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        InputStream inputStream;
        String broj = params[0];
        try {
            HttpGet httpGet = new HttpGet(context.getString(R.string.serverIP)+"/getLatestPartners/"+ params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }else
                result = new JSONArray();
        } catch (Exception ex) {
            Log.d("HTTP GET ERROR", ex.getMessage());
        }
        return result;
    }

    protected void onPostExecute(JSONArray partners) {
        if (partners != null && partners.length() != 0) {
            for (int i = 0; i < partners.length(); i++) {
                Partner partner = new Partner();
                try {
                    partner.setPartner_id(partners.getJSONObject(i).getInt("id"));
                    partner.setName(partners.getJSONObject(i).getString("name"));
                    partner.setAddress(partners.getJSONObject(i).getString("address"));
                    partner.setImageUrl(partners.getJSONObject(i).getString("logoUrl"));
                    partner.setCreated_at(partners.getJSONObject(i).getLong("createdAt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new FetchPartnerData(context, listener).execute(partner);
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

        // return JSON Array
        return jArray;

    }
}
