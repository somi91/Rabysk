package com.uslive.rabyks.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uslive.rabyks.R;
import com.uslive.rabyks.fragments.ClubOwnerWaiter;
import com.uslive.rabyks.models.DrawerRow;
import com.uslive.rabyks.models.RowWaiterRemove;

import java.util.List;

/**
 * Created by marezina on 8.7.2015.
 */
public class DrawerRowAdapter extends BaseAdapter {
    Context context;
    List<DrawerRow> rowItems;

    public DrawerRowAdapter(Context context, List<DrawerRow> items) {
        this.context = context;
        this.rowItems = items;
    }

    private class ViewHolder {
        ImageButton imageButton;
        TextView txtName;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_row, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.btnImage);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        DrawerRow rowItem = (DrawerRow) getItem(position);
        holder.txtName.setText(rowItem.getName());
        holder.imageButton.setImageResource(rowItem.getImageId());

        return convertView;
    }
}
