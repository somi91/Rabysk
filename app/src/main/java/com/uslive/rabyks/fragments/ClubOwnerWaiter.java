package com.uslive.rabyks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.uslive.rabyks.AsyncTasks.AddWaiter;
import com.uslive.rabyks.AsyncTasks.OnAddWaiterCompleted;
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
public class ClubOwnerWaiter extends Fragment implements OnAddWaiterCompleted, OnRemoveWaiterCompleted{
    EditText waiterName;
    EditText password;
    ImageButton btnAddWaiter;

    public static final String[] titles = new String[] { "Strawberry",
            "Banana", "Orange", "Mixed" };
    public static final Integer[] images = { R.drawable.ic_action_remove,
            R.drawable.ic_action_remove, R.drawable.ic_action_remove, R.drawable.ic_action_remove };

    ListView listView;
    List<RowWaiterRemove> rowItems;
    AddWaiter addWaiter;
    RemoveWaiter removeWaiter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.club_owner_waiter, container, false);

        waiterName = (EditText) view.findViewById(R.id.txtName);
        password = (EditText) view.findViewById(R.id.password);
        btnAddWaiter = (ImageButton) view.findViewById(R.id.btnAddWaiter);
        btnAddWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWaiter(waiterName.getText().toString(), password.getText().toString());
            }
        });

//        scale = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;

        rowItems = new ArrayList<RowWaiterRemove>();
        for (int i = 0; i < titles.length; i++) {
            RowWaiterRemove item = new RowWaiterRemove(images[i], titles[i]);
            rowItems.add(item);
        }

        listView = (ListView) view.findViewById(R.id.list);
        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems, ClubOwnerWaiter.this);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);

        addWaiter = new AddWaiter(getActivity().getApplicationContext(), this);
        removeWaiter = new RemoveWaiter(getActivity().getApplicationContext(), this);

        return view;
    }

    private void AddWaiter(String n, String p) {

        removeWaiter.execute(n, p);
    }

    public void RemoveWaiter(String n) {

        removeWaiter.execute(n);
    }

    @Override
    public void OnAddWaiterCompleted(String array) {
        // update rowItems list
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(array);
            RowWaiterRemove item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new RowWaiterRemove(R.drawable.ic_action_remove, jsonArray.getJSONObject(i).getString("name"));
                rowItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems, ClubOwnerWaiter.this);
        listView.setAdapter(adapter);
    }

    @Override
    public void OnRemoveWaiterCompleted(String array) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(array);
            RowWaiterRemove item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new RowWaiterRemove(R.drawable.ic_action_remove, jsonArray.getJSONObject(i).getString("name"));
                rowItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems, ClubOwnerWaiter.this);
        listView.setAdapter(adapter);
    }
}