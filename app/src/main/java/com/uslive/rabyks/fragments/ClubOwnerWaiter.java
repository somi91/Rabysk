package com.uslive.rabyks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.uslive.rabyks.AsyncTasks.AddWaiter;
import com.uslive.rabyks.AsyncTasks.GetWaiters;
import com.uslive.rabyks.AsyncTasks.OnAddWaiterCompleted;
import com.uslive.rabyks.AsyncTasks.OnGetWaitersCompleted;
import com.uslive.rabyks.AsyncTasks.OnRemoveWaiterCompleted;
import com.uslive.rabyks.AsyncTasks.RemoveWaiter;
import com.uslive.rabyks.R;
import com.uslive.rabyks.adapters.WaiterRemoveAdapter;
import com.uslive.rabyks.models.RowWaiterRemove;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milos on 6/16/2015.
 */
public class ClubOwnerWaiter extends Fragment implements OnAddWaiterCompleted, OnRemoveWaiterCompleted, OnGetWaitersCompleted{
    private EditText waiterName;
    private EditText password;
    private ImageButton btnAddWaiter;

//    public static final String[] titles = new String[] { "Strawberry",
//            "Banana", "Orange", "Mixed" };
//    public static final Integer[] images = { R.drawable.ic_action_remove,
//            R.drawable.ic_action_remove, R.drawable.ic_action_remove, R.drawable.ic_action_remove };

    private ListView listView;
    private List<RowWaiterRemove> rowItems;
    private AddWaiter addWaiter;
    private RemoveWaiter removeWaiter;
    private int partnerId;
    private GetWaiters getWaiters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.club_owner_waiter, container, false);

        Bundle mArgs = getArguments();
        if(mArgs != null) {
            partnerId = mArgs.getInt("partnerId");
        }

        waiterName = (EditText) view.findViewById(R.id.txtName);
        password = (EditText) view.findViewById(R.id.txtPassword);
        btnAddWaiter = (ImageButton) view.findViewById(R.id.btnAddWaiter);
        btnAddWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWaiter(waiterName.getText().toString(), password.getText().toString());
            }
        });

//        scale = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;

        getWaiters = new GetWaiters(getActivity().getApplicationContext(), this);
        getWaiters.execute(partnerId+"");

        rowItems = new ArrayList<RowWaiterRemove>();
//        for (int i = 0; i < titles.length; i++) {
//            RowWaiterRemove item = new RowWaiterRemove(images[i], titles[i]);
//            rowItems.add(item);
//        }

        listView = (ListView) view.findViewById(R.id.list);
        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems, ClubOwnerWaiter.this);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);

        return view;
    }

    private void AddWaiter(String n, String p) {

        addWaiter = new AddWaiter(getActivity().getApplicationContext(), this);
        addWaiter.execute(n, p, partnerId+"");
    }

    public void RemoveWaiter(String n) {

        removeWaiter = new RemoveWaiter(getActivity().getApplicationContext(), this);
        removeWaiter.execute(n);
    }

    @Override
    public void OnAddWaiterCompleted(String array) {
        // update rowItems list
//        Log.i("On add waiter", array);
        getWaiters = new GetWaiters(getActivity().getApplicationContext(), this);
        getWaiters.execute(partnerId+"");
    }

    @Override
    public void OnRemoveWaiterCompleted(String array) {
//        Log.i("On remove waiter", array);
//        JSONArray jsonArray;
//        try {
//            jsonArray = new JSONArray(array);
//            RowWaiterRemove item;
//            for (int i = 0; i < jsonArray.length(); i++) {
//                item = new RowWaiterRemove(R.drawable.ic_action_remove, jsonArray.getJSONObject(i).getString("email"));
//                rowItems.add(item);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems, ClubOwnerWaiter.this);
//        listView.setAdapter(adapter);

        getWaiters = new GetWaiters(getActivity().getApplicationContext(), this);
        getWaiters.execute(partnerId + "");
    }

    @Override
    public void OnGetWaitersCompleted(JSONArray jsonArray) {
        Log.i("On get waiters", "stigli su");
        List<RowWaiterRemove> waiters = new ArrayList<RowWaiterRemove>();
        try {
            RowWaiterRemove item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new RowWaiterRemove(R.drawable.ic_action_remove, jsonArray.getJSONObject(i).getString("email"));
                waiters.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), waiters, ClubOwnerWaiter.this);
        listView.setAdapter(adapter);
    }
}