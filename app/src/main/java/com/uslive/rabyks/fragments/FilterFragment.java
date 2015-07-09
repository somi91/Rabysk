package com.uslive.rabyks.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.uslive.rabyks.R;
import com.uslive.rabyks.models.PartnerType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marezina on 8.7.2015.
 */
public class FilterFragment extends Fragment {
//    private CheckBox kafana;
//    private CheckBox klub;
//    private CheckBox restoran;
//    private CheckBox kafic;
    private Button btnFiltriraj;
    private LinearLayout linearLayout;

    private static final String DESCRIBABLE_KEY = "describable_key";
    private ArrayList array;
    private ArrayList<CheckBox> listOfCheckBoxex;

    private int[] searchQuery;

    public interface FinishFilterFragment{
        void onFinishFilterFragment(int[] searchQuery);
    }

    public static FilterFragment newInstance(ArrayList describable) {
        FilterFragment fragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        View view = inflater.inflate(R.layout.filter_fragment, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.placeHolderForCheckBox);

        btnFiltriraj = (Button) view.findViewById(R.id.btnCheckBoxSave);
        btnFiltriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtriraj();
            }
        });
        listOfCheckBoxex = new ArrayList<>();
        array = (ArrayList) getArguments().getSerializable(DESCRIBABLE_KEY);
        for (int i = 0; i < array.size(); i++){
            PartnerType partnerType = (PartnerType) array.get(i);
            CheckBox cb = new CheckBox(getActivity().getApplicationContext());
            cb.setText(partnerType.getName());
            cb.setId(partnerType.getId());
            cb.setTextColor(Color.BLACK);
            listOfCheckBoxex.add(cb);
            linearLayout.addView(cb);
        }
        return view;
    }

    private void Filtriraj() {
        searchQuery = new int[4];
        int br = 0;
        FinishFilterFragment activity = (FinishFilterFragment) getActivity();
        for(int i = 0; i < listOfCheckBoxex.size(); i++){
            CheckBox cb = listOfCheckBoxex.get(i);
            if(cb.isChecked()){
                for(int j = 0; j < listOfCheckBoxex.size(); j++){
                    if(searchQuery[j] == 0){
                        searchQuery[j] = cb.getId();
                        break;
                    }
                }
            }else
                br++;
        }
        if(br == 4) {
            getFragmentManager().popBackStackImmediate();
        } else {
            activity.onFinishFilterFragment(searchQuery);
            getFragmentManager().popBackStackImmediate();
        }
    }


}
