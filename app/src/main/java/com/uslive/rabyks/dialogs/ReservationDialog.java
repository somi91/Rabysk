package com.uslive.rabyks.dialogs;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uslive.rabyks.R;

/**
 * Created by milos on 6/4/2015.
 */
public class ReservationDialog extends DialogFragment implements Button.OnClickListener {

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

//    private EditText mEditText;
    private Button btnPotvrdi;
    private Button btnOdustani;

    public ReservationDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reservation_dialog, container);
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);

        btnPotvrdi = (Button) view.findViewById(R.id.btnPotvrdi);
        btnOdustani = (Button) view.findViewById(R.id.btnOdustani);
        getDialog().setTitle("NAPRAVI REZERVACIJU");

        // Show soft keyboard automatically
//        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        mEditText.setOnEditorActionListener(this);

        btnPotvrdi.setOnClickListener(this);
        btnOdustani.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        EditNameDialogListener activity = (EditNameDialogListener) getActivity();
        if(btnPotvrdi.getId() == id){
            activity.onFinishEditDialog(" !!! OK !!!");
            this.dismiss();
        } else{
            activity.onFinishEditDialog(" !!! CANCLE !!!");
            this.dismiss();
        }

    }


}
