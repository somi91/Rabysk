package com.uslive.rabyks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uslive.rabyks.R;
import com.uslive.rabyks.adapters.WaiterRemoveAdapter;
import com.uslive.rabyks.models.RowWaiterRemove;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milos on 6/16/2015.
 */
public class ClubOwnerDetail extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.club_owner_details, container, false);

        return view;
    }
}
