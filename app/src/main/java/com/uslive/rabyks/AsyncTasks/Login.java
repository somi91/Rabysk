package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.uslive.rabyks.R;
import com.uslive.rabyks.activities.LoginActivity;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.models.Partner;
import com.uslive.rabyks.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ustankovic on 30-06-15.
 */
public class Login extends AsyncTask<String, Void, JSONObject> {

    private static final String TAG = Login.class.getSimpleName();

    private Context context;

    private SQLiteHandler db;

    private OnLoginComplete olc;

    JSONObject result;

    public Login(Context context, SQLiteHandler db, LoginActivity la) {
        this.context = context;
        this.db = db;
        this.olc = la;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        InputStream inputStream;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(context.getString(R.string.serverIP) + "/login");
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("email", params[0]));
            postParameters.add(new BasicNameValuePair("password", params[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return result;
    }

    protected void onPostExecute(JSONObject result) {
        try {
            db.addUser((Integer) result.get("id"), ((String) result.get("number")).equals("null") ? null : (String) result.get("number"), (String) result.get("email"), (String) result.get("password"), (String) result.get("role"));
            JSONArray partners = result.getJSONArray("partners");
            for (int i = 0; i < partners.length(); ++i) {
                db.addUserPartner((Integer)result.get("id"), partners.optInt(i));
            }
            olc.onLoginComplete();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private JSONObject convertInputStreamToString(InputStream inputStream) throws Exception{
        JSONObject jsonObject;
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
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            throw new Exception(e.getMessage());
        }
        return jsonObject;
    }
}
