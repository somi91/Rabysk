package com.uslive.rabyks.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uslive.rabyks.R;

import java.util.ArrayList;
import java.util.List;

import com.uslive.rabyks.models.Partner;

/**
 * Created by milos on 5/21/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bitmap> images;
    private List<Partner> partners;
    private LayoutInflater mInflater;

    public ImageAdapter(Context c, List<Partner> p) {
        mContext = c;
        images = new ArrayList<Bitmap>();
        partners = p;
        mInflater = LayoutInflater.from(mContext);
        byte[] img;
        for (Partner partner : partners) {
            img = partner.getLogo_url_bytes();
            images.add(BitmapFactory.decodeByteArray(img, 0, img.length));
        }
    }

    public int getCount() {
        return partners.size();
//        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return partners.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        TextView name;
        ImageView imageView;
        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        imageView = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        imageView.setImageBitmap(images.get(position));
        name.setText(partners.get(position).getName());

        return v;
    }

}
