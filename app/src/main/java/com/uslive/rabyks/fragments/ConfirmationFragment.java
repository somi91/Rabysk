package com.uslive.rabyks.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uslive.rabyks.R;
import com.uslive.rabyks.Services.ProximityReciever;
import com.uslive.rabyks.helpers.SQLiteHandler;

import java.util.Date;

/**
 * Created by marezina on 26.6.2015.
 */


public class ConfirmationFragment extends Fragment {
    public ConfirmationFragment() {
    }

    private MyCount timerCount;
    private TextView txtTimeValue;
    private TextView txtPartnerName;
    private TextView txtUsername;
    private TextView txtDate;
    private ImageView imgConfirmationLogo;

    LocationManager lm;

    //Defining Latitude & Longitude (NBG)
    double lat=44.816218 ,long1=20.413793;
    //Defining Radius
    float radius=300;

    ProximityReciever proximityReciever;

    //Intent Action
    String ACTION_FILTER = "com.uslive.rabyks.MAIN";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmation_fragment, container, false);

        txtTimeValue = (TextView) view.findViewById(R.id.txtTimerValue);
        txtPartnerName = (TextView) view.findViewById(R.id.txtPartnerName);
        txtUsername = (TextView) view.findViewById(R.id.txtUsername);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        imgConfirmationLogo = (ImageView) view.findViewById(R.id.imgConfirmationLogo);

        Bundle args = getArguments();
        txtPartnerName.setText(args.getString("partnerName"));
        byte[] img = args.getByteArray("logoImg");
        Bitmap image = BitmapFactory.decodeByteArray(img, 0, img.length);
        imgConfirmationLogo.setImageBitmap(image);
        Date date = new Date();
        txtDate.setText(""+date);
        Long createdAt = args.getLong("createdAt");
        Long expiresAt = args.getLong("expiresAt");
        Log.i("Razlika moja racunica", ""+ (expiresAt - createdAt));
        timerCount = new MyCount(15 * 1000, 1000);
        timerCount.start();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //i'm registering my Receiver First
        proximityReciever = new ProximityReciever();
        getActivity().registerReceiver(proximityReciever, new IntentFilter(ACTION_FILTER));

        //i'm calling ther service Location Manager
        lm=(LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        //for debugging...
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);

        //Setting up My Broadcast Intent
        Intent i= new Intent(ACTION_FILTER);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity().getApplicationContext(), -1, i, 0);

        //setting up proximituMethod
        lm.addProximityAlert(lat, long1, radius, 5 * 60 * 1000, pi);

        return view;
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //some script here
            LinearLayout cardReservation = (LinearLayout) getActivity().findViewById(R.id.cardReservation);
            cardReservation.setBackgroundColor(Color.RED);
            txtTimeValue.setText(R.string.timerVal);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            SQLiteHandler db = new SQLiteHandler(getActivity().getApplicationContext());
            db.updateReservationTable();
            getFragmentManager().popBackStackImmediate();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //some script heres
            txtTimeValue.setText("00:00:" + (millisUntilFinished / 1000));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(proximityReciever);
    }
}
