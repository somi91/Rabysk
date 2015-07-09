package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    ProgressBar bar;
    LinearLayout filterLayout;

    public GetLatestPartners(Context c, OnTaskCompletedUpdateGridView l, ProgressBar b, LinearLayout f){
        context = c;
        listener = l;
        bar = b;
        filterLayout = f;
    }
//    JSONParser jsonParser = new JSONParser();

    JSONArray result;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bar.setVisibility(View.VISIBLE);
        filterLayout.setVisibility(View.GONE);
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        InputStream inputStream;
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
            Log.d("HTTP GET ERROR UPDATE", ex.getMessage());
        }
        return result;
    }

    protected void onPostExecute(JSONArray partners) {
        bar.setVisibility(View.GONE);
        filterLayout.setVisibility(View.VISIBLE);
        if (partners != null && partners.length() != 0) {
            for (int i = 0; i < partners.length(); i++) {
                Partner partner = new Partner();
                try {
                    partner.setId(partners.getJSONObject(i).getInt("id"));
                    partner.setName(partners.getJSONObject(i).getString("name"));
                    partner.setAddress(partners.getJSONObject(i).getString("address"));
                    partner.setNumber(partners.getJSONObject(i).getString("number"));
                    partner.setLogo_url(partners.getJSONObject(i).getString("logoUrl"));
                    partner.setLayout_img_url(partners.getJSONObject(i).getString("layoutImgUrl"));
                    JSONArray types = partners.getJSONObject(i).getJSONArray("types");
                    //read all types
                    String typesString = "";
                    for(int j = 0; j < types.length(); j++) {
                        JSONObject type = types.getJSONObject(j);
                        int id = type.getInt("id");
                        typesString += id + ",";
                    }
                    //remove last comma
                    typesString = typesString.substring(0, typesString.length()-1);
                    partner.setType(typesString);
                    partner.setWorking_hours(partners.getJSONObject(i).getString("workingHours"));
                    partner.setCreated_at(partners.getJSONObject(i).getLong("createdAt"));
                    partner.setModified_at(partners.getJSONObject(i).getLong("modifiedAt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new FetchPartnerData(context, listener, bar, filterLayout).execute(partner);
            }
        } else {
            listener.onTaskCompletedUpdateGridView();
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

        // return JSON Array
        return jArray;

    }
}
