package clubs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marezina.rabysk.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import models.Club;

/**
 * Created by milos on 5/24/2015.
 */
public class SearchClubAdapter extends ArrayAdapter<Club>{
    Context context;
    int layoutResourceId;
    ArrayList<Club> data=new ArrayList<Club>();
    public SearchClubAdapter(Context context, int layoutResourceId, ArrayList<Club> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        Club club = data.get(position);
        holder.txtTitle.setText(club.get_name());
        //convert byte to bitmap take from contact class
        byte[] outImage = club.get_image();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.imgIcon.setImageBitmap(theImage);
        return row;
    }
    static class ImageHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}

