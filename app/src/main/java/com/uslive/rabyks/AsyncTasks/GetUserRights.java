package com.uslive.rabyks.AsyncTasks;

/**
 * Created by milos on 6/29/2015.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.uslive.rabyks.R;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.models.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetUserRights extends AsyncTask <String, Void, String> {

    private static final String TAG = GetUserRights.class.getSimpleName();

    private Context context;

    private SQLiteHandler db;

    private User user;

    public GetUserRights(Context context, SQLiteHandler db, User user) {
        this.context = context;
        this.db = db;
        this.user = user;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(context.getString(R.string.serverIP)+"/getUserRoles/" + user.getId());
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            if (is != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String result = bufferedReader.readLine();

                // Ako je druga rola upisi u bazu
                if(!user.getRole().equals(result)) {
                    db.updateUser(user);
                }

                if (result.equals("admin")) {
                    // TODO OTKRIJ STVARI U MENIJU

                } else if (result.equals("konobar")) {
                    // TODO OTKRIJ STVARI U MENIJU

                } else {
                    // OBRISI IZ BAZE USERA JER VISE NIJE KONOBAR ILI ADMIN
                    db.deleteUsers();
                }
            }
        } catch (Exception e) {
            Log.e("HTTP GET ERROR", e.getMessage());
        }
        return null;
    }
}