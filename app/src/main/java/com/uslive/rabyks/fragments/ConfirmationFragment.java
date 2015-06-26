package com.uslive.rabyks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uslive.rabyks.R;

/**
 * Created by marezina on 26.6.2015.
 */
public class ConfirmationFragment extends Fragment {
    public ConfirmationFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.employee_reservation_dialog, container);

        return view;
    }
}
