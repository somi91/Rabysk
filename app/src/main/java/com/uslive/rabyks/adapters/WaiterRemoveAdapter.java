package com.uslive.rabyks.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.uslive.rabyks.R;
import com.uslive.rabyks.fragments.ClubOwnerWaiter;
import com.uslive.rabyks.models.RowWaiterRemove;

import java.util.List;

/**
 * Created by marezina on 16.6.2015.
 */
public class WaiterRemoveAdapter extends BaseAdapter {
    Context context;
    List<RowWaiterRemove> rowItems;
    ClubOwnerWaiter clubOwnerWaiter;

    public WaiterRemoveAdapter(Context context, List<RowWaiterRemove> items, ClubOwnerWaiter clubOwnerWaiter) {
        this.context = context;
        this.rowItems = items;
        this.clubOwnerWaiter = clubOwnerWaiter;
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
            convertView = mInflater.inflate(R.layout.row_waiter_remove, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.btnImageRemove);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        RowWaiterRemove rowItem = (RowWaiterRemove) getItem(position);
        holder.txtName.setText(rowItem.getName());
        holder.imageButton.setImageResource(rowItem.getImageId());
        final String waiterName = holder.txtName.getText().toString();
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubOwnerWaiter.RemoveWaiter(waiterName);
            }
        });

        return convertView;
    }
}
