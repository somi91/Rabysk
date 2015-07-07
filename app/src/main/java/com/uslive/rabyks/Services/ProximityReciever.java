package com.uslive.rabyks.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by marezina on 6.7.2015.
 */
public class ProximityReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Key for determining whether user is leaving or entering
        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        //Gives whether the user is entering or leaving in boolean form
        boolean state = intent.getBooleanExtra(key, false);

        if(state){
            // Call the Notification Service or anything else that you would like to do here
            Log.i("MyTag", "Welcome to my Area");
            Toast.makeText(context, "Welcome to my Area", Toast.LENGTH_SHORT).show();
        }else{
            //Other custom Notification
            Log.i("MyTag", "Thank you for visiting my Area,come back again !!");
            Toast.makeText(context, "Thank you for visiting my Area,come back again !!", Toast.LENGTH_SHORT).show();
        }
    }
}
