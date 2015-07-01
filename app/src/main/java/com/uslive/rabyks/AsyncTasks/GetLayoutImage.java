package com.uslive.rabyks.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by milos on 7/1/2015.
 */
public class GetLayoutImage extends AsyncTask<String, String, Bitmap> {
    Context context;
    ImageView partnerLayoutImg;

    public GetLayoutImage(Context c, ImageView iv){
        context = c;
        partnerLayoutImg = iv;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        URL url = null;
        try {
            url = new URL(urls[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmp;
    }

    protected void onPostExecute(Bitmap bmp) {
        partnerLayoutImg.setImageBitmap(bmp);
    }
}