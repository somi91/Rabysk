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
import com.uslive.rabyks.R;
import com.uslive.rabyks.adapters.WaiterRemoveAdapter;
import com.uslive.rabyks.models.RowWaiterRemove;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milos on 6/16/2015.
 */
public class ClubOwnerWaiter extends Fragment implements OnAddWaiterCompleted{
    EditText waiterName;
    ImageButton btnAddWaiter;

    public static final String[] titles = new String[] { "Strawberry",
            "Banana", "Orange", "Mixed" };
    public static final Integer[] images = { R.drawable.ic_action_remove,
            R.drawable.ic_action_remove, R.drawable.ic_action_remove, R.drawable.ic_action_remove };

    ListView listView;
    List<RowWaiterRemove> rowItems;
    AddWaiter addWaiter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.club_owner_waiter, container, false);

        waiterName = (EditText) view.findViewById(R.id.txtName);
        btnAddWaiter = (ImageButton) view.findViewById(R.id.btnAddWaiter);
        btnAddWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWaiter(waiterName.getText().toString());
            }
        });

//        scale = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;

        rowItems = new ArrayList<RowWaiterRemove>();
        for (int i = 0; i < titles.length; i++) {
            RowWaiterRemove item = new RowWaiterRemove(images[i], titles[i]);
            rowItems.add(item);
        }

        listView = (ListView) view.findViewById(R.id.list);
        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);

        addWaiter = new AddWaiter(getActivity().getApplicationContext(), this);

        return view;
    }

    private void AddWaiter(String s) {

        addWaiter.execute(s);
    }

    @Override
    public void OnAddWaiterCompleted() {
        // update rowItems list

        WaiterRemoveAdapter adapter = new WaiterRemoveAdapter(getActivity().getApplicationContext(), rowItems);
        listView.setAdapter(adapter);
    }
}
