package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uslive.rabyks.activities.MainActivity;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.models.Club;
import com.uslive.rabyks.models.Partner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by marezina on 19.6.2015.
 */
public class FetchPartnerData extends AsyncTask<Partner, Void, byte[]> {

    private Context context;
    Partner partner;
    OnTaskCompletedUpdateGridView listener;
    ProgressBar bar;


    public FetchPartnerData(Context c, OnTaskCompletedUpdateGridView l, ProgressBar b){
        context = c;
        listener = l;
        bar = b;
    }

    @Override
    protected void onPreExecute(){
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected byte[] doInBackground(Partner... params) {
        partner = params[0];
        byte[] data = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(partner.getImageUrl());
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            data = read(is);
            if (data != null) {
                Bitmap downloadedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        } catch (IOException e) {
            Log.i("Error in Fetch Data", partner.getName());
            e.printStackTrace();
        }

        return data;
    }

    protected void onPostExecute(byte[] result) {
        bar.setVisibility(View.GONE);
        SQLiteHandler sqLiteHandler = new SQLiteHandler(context);
        Long tsLong = System.currentTimeMillis()/1000;
        sqLiteHandler.addClub(new Club(partner.getName(), partner.getPartner_id(), result, partner.getPartner_id(), tsLong));
        sqLiteHandler.close();
        listener.onTaskCompletedUpdateGridView();
        Toast.makeText(context, "Data Sent!", Toast.LENGTH_LONG).show();
    }

    public static byte[] read(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        try {
            // Read buffer, to read a big chunk at a time.
            byte[] buf = new byte[2048];
            int len;
            // Read until -1 is returned, i.e. stream ended.
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            Log.e("Downloader", "File could not be downloaded", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Input stream could not be closed.
            }
        }
        return baos.toByteArray();
    }
}
