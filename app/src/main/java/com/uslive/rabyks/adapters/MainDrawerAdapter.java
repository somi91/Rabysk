package com.uslive.rabyks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uslive.rabyks.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by marezina on 18.6.2015.
 */
public class MainDrawerAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mPlanetTitles;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

    public MainDrawerAdapter(Context context, String[] array) {
        mPlanetTitles = array;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        IteratorThrowData(mPlanetTitles);
    }

    public void IteratorThrowData(String[] data) {
        for (int i = 0; i < data.length; i++) {
            if( i == 1 ){
                addSeparatorItem("Favorites");
            } else if ( i == 4 ) {
                addSeparatorItem("Gazda");
            } else if ( i == 6 ){
                addSeparatorItem("Konobar");
            }
            addItem(data[i]);
        }
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final String item) {
        mData.add(item);
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

    public String getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView holder = null;
        int type = getItemViewType(position);
        System.out.println("getView " + position + " " + convertView + " type = " + type);
        if (convertView == null) {
            switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.drawer_item, null);
                    holder = (TextView)convertView.findViewById(R.id.text);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.drawer_header_item, null);
                    holder = (TextView)convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (TextView)convertView.getTag();
        }
        holder.setText(mData.get(position));
        return convertView;
    }
}
