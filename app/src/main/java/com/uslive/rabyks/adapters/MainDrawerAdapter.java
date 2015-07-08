package com.uslive.rabyks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uslive.rabyks.R;
import com.uslive.rabyks.activities.ClubActivity;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.models.DrawerRow;
import com.uslive.rabyks.models.Partner;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by marezina on 18.6.2015.
 */
public class MainDrawerAdapter extends BaseAdapter {
    private Context mContext;
    private String[] drawerItems;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<DrawerRow> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();
    ArrayList<String> mainList = new ArrayList<>();

    public MainDrawerAdapter(Context context, String[] array) {
//      TODO pokupiti podatke iz baze i odgovarajucim podacima popuniti adapter
        SQLiteHandler db = new SQLiteHandler(context);
        mainList.add("Pocetna");

        List<String> favorites = db.getReservations();
        mainList.addAll(favorites);

        drawerItems = array;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IteratorThrowData(mainList);
    }

    public void IteratorThrowData(ArrayList data) {
        for (int i = 0; i < data.size(); i++) {
            if( i == 1 ){
                addSeparatorItem("Favorites");
            }
            addItem((String) data.get(i));
        }
    }

    public void addItem(final String item) {
        DrawerRow row = new DrawerRow(R.drawable.star, item);
        mData.add(row);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final String item) {
        DrawerRow row = new DrawerRow(0, item);
        mData.add(row);
        // save separator position
        mSeparatorsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public int getCount() {
        return mData.size();
    }

    public DrawerRow getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textHolder = null;

        ViewHolder holder = null;

        int type = getItemViewType(position);
//        System.out.println("getView " + position + " " + convertView + " type = " + type);
        switch (type) {
            case TYPE_ITEM:
                convertView = mInflater.inflate(R.layout.drawer_row, null);
//                    holder = (TextView)convertView.findViewById(R.id.text);
                holder = new ViewHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
                holder.imageButton = (ImageButton) convertView.findViewById(R.id.btnImage);
                convertView.setTag(holder);
                DrawerRow rowItem = getItem(position);
                holder.txtName.setText(rowItem.getName());
                holder.imageButton.setImageResource(rowItem.getImageId());
                break;
            case TYPE_SEPARATOR:
                convertView = mInflater.inflate(R.layout.drawer_header_item, null);
                textHolder = (TextView)convertView.findViewById(R.id.textSeparator);
                textHolder.setText(mData.get(position).getName());
                convertView.setTag(textHolder);
                break;
        }
        final ViewHolder finalHolder = holder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = finalHolder.txtName.getText().toString();
                SQLiteHandler db = new SQLiteHandler(mContext);
                List<Partner> partners = db.getPartnersBySpecificParametar(name);
                Partner partner = partners.get(0);
                Intent clubIntent = new Intent(mContext, ClubActivity.class);
//                String partner_id = "" + partner.getId();
                clubIntent.putExtra("partner_id", partner.getId());
                clubIntent.putExtra("partner_name", partner.getName());
                clubIntent.putExtra("partner_created_at", partner.getCreated_at());
                clubIntent.putExtra("partner_layout_img_url", partner.getLayout_img_url());
                mContext.startActivity(clubIntent);
                ((Activity) mContext).finish();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageButton imageButton;
        TextView txtName;
    }
}
