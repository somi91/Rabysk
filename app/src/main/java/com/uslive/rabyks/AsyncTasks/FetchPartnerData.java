package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.models.Partner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by marezina on 19.6.2015.
 */
public class FetchPartnerData extends AsyncTask<Partner, Void, byte[]> {

    private Context context;
    Partner partner;
    OnTaskCompletedUpdateGridView listener;
    ProgressBar bar;
    LinearLayout filterLayout;

    public FetchPartnerData(Context c, OnTaskCompletedUpdateGridView l, ProgressBar b, LinearLayout f){
        context = c;
        listener = l;
        bar = b;
        filterLayout = f;
    }

    @Override
    protected void onPreExecute(){
        bar.setVisibility(View.VISIBLE);
        filterLayout.setVisibility(View.GONE);
    }

    @Override
    protected byte[] doInBackground(Partner... params) {
        partner = params[0];
        byte[] data = null;
        try {

            URL url = new URL(partner.getLogo_url());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            data = read(input);

//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            data = stream.toByteArray();

//            DefaultHttpClient client = new DefaultHttpClient();
//            HttpGet request = new HttpGet(partner.getLogo_url());
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//            InputStream is = entity.getContent();
//            data = read(is);
//            if (data != null) {
//                Bitmap downloadedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            }
        } catch (IOException e) {
            Log.i("Error in Fetch Data", partner.getName());
            e.printStackTrace();
        }

        return data;
    }

    protected void onPostExecute(byte[] result) {
        bar.setVisibility(View.GONE);
        filterLayout.setVisibility(View.VISIBLE);
        SQLiteHandler sqLiteHandler = new SQLiteHandler(context);
        Long tsLong = System.currentTimeMillis()/1000;
        sqLiteHandler.addPartner(new Partner(partner.getId(), partner.getName(), partner.getAddress(), partner.getNumber(), partner.getLogo_url(), result, partner.getLayout_img_url(), partner.getType(), partner.getWorking_hours(), partner.getCreated_at(), partner.getModified_at()));
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
