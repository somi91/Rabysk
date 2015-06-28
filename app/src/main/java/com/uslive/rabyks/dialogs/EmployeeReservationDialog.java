package com.uslive.rabyks.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uslive.rabyks.R;

/**
 * Created by milos on 6/8/2015.
 */
public class EmployeeReservationDialog extends DialogFragment implements Button.OnClickListener {

    public interface EditDialogListener {
        void onFinishDialog(boolean success, int partnerId, int objectId, boolean stateReservation, boolean delete);
    }

    //    private EditText mEditText;
    private Button btnPotvrdi;
    private Button btnOdustani;
    private ImageButton btnClear;
    private ImageButton btnMake;
    private ImageButton btnDelete;
    private TextView txtState;
    private boolean free;
    private boolean delete;

    private int partnerId;
    private int objectId;

    public EmployeeReservationDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_reservation_dialog, container);
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);

        btnPotvrdi = (Button) view.findViewById(R.id.btnPotvrdi);
        btnOdustani = (Button) view.findViewById(R.id.btnOdustani);
        btnClear = (ImageButton) view.findViewById(R.id.btnClearReservation);
        btnMake = (ImageButton) view.findViewById(R.id.btnMakeReservation);
        btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        txtState = (TextView) view.findViewById(R.id.txtState);
        delete = false;
        getDialog().setTitle("NAPRAVI REZERVACIJU");

        Bundle mArgs = getArguments();
        if(mArgs != null){
            partnerId = mArgs.getInt("partnerId");
            objectId = mArgs.getInt("objectId");
            free = mArgs.getBoolean("free");
        }
        // Show soft keyboard automatically
//        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        mEditText.setOnEditorActionListener(this);

        btnPotvrdi.setOnClickListener(this);
        btnOdustani.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnMake.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        EditDialogListener activity = (EditDialogListener) getActivity();
        if(btnPotvrdi.getId() == id){
            activity.onFinishDialog(true, partnerId, objectId, free, delete);
            this.dismiss();
        } else if(btnClear.getId() == id) {
            free = true;
            txtState.setText("Potvrdi da bi obelezio slobodan sto");
            Log.i("Oslobodjen sto broj ", " " + objectId);

        } else if(btnMake.getId() == id){
            free = false;
            txtState.setText("Potvrdi da bi obelezio zauzet sto");
            Log.i("Zayzet sto broj ", " " + objectId);

        } else if(btnDelete.getId() == id){
            delete = true;
            txtState.setText("Potvrdi da bi obrisao sto");
        } else{
            activity.onFinishDialog(false, partnerId, objectId, free, delete);
            this.dismiss();
        }
    }
}
