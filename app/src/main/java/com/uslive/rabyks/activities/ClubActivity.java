package com.uslive.rabyks.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uslive.rabyks.AsyncTasks.GetLayoutImage;
import com.uslive.rabyks.R;
import com.uslive.rabyks.Services.GPSTracker;
import com.uslive.rabyks.adapters.MainDrawerAdapter;
import com.uslive.rabyks.dialogs.EmployeeReservationDialog;
import com.uslive.rabyks.fragments.ClubOwnerDetail;
import com.uslive.rabyks.fragments.ClubOwnerWaiter;
import com.uslive.rabyks.fragments.EditPosition;
import com.uslive.rabyks.dialogs.ReservationDialog;
import com.uslive.rabyks.helpers.JsonManager;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.models.Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class ClubActivity extends ActionBarActivity implements ReservationDialog.FinishDialogListener, EmployeeReservationDialog.EditDialogListener {

    private int sdk = android.os.Build.VERSION.SDK_INT;

    private static final String TAG = "ClubActivity";

//    private TextView club_id;
    private String club_name;
//    private TextView club_uuid;
//    private TextView club_url;
//    private TextView club_created_at;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private RelativeLayout clubActivityRelativeLayout;
    private float scale;

    private GPSTracker gps;

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    private Thread thrd;
    private int partnerId;
    private String layoutImgUrl;
    private ImageView partnerLayoutImg;
    private JSONArray partnerSetup;
    private JSONObject objectTable;

    private int layoutWidth;
    private int layoutHeight;

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        clubActivityRelativeLayout = (RelativeLayout) findViewById(R.id.clubActivityRelativeLayout);

        scale = getApplicationContext().getResources().getDisplayMetrics().density;

        // data for partner
        Intent myIntent = getIntent(); // gets the previously created intent
        partnerId = myIntent.getIntExtra("partner_id", 0);
        Log.i("PARTNER ID ", partnerId+"");
        setTitle(myIntent.getStringExtra("partner_name"));
        layoutImgUrl = myIntent.getStringExtra("partner_layout_img_url");
        partnerLayoutImg = (ImageView) findViewById(R.id.partnerLayoutImg);

        db = new SQLiteHandler(getApplicationContext());

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mTitle = mDrawerTitle = myIntent.getStringExtra("partner_name");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setAdapter(new MainDrawerAdapter(this, mPlanetTitles));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Button stop = (Button)findViewById(R.id.cancelButton);
//        stop.setOnClickListener(stopListener);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(ClubActivity.this);
                gps.stopUsingGPS();
            }
        });

        Button btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
//        btnSendMessage.setOnClickListener(sendMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GPSTracker(ClubActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    gps.stopUsingGPS();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                    Toast.makeText(getApplicationContext(), "NESTO NE VALJA", Toast.LENGTH_LONG).show();
                }
            }
        });

        setBackGroundImage();
//
//        Button btnPopup1 = (Button) findViewById(R.id.btnPopup1);
//        Button btnPopup2 = (Button) findViewById(R.id.btnPopup2);
//
//        btnPopup1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });

//        doBindService();
    }

    public void setBackGroundImage(){

        GetLayoutImage getLayoutImage = new GetLayoutImage(getApplicationContext(), partnerLayoutImg);
        getLayoutImage.execute(layoutImgUrl);
    }

    public void setButton(Button btn, final int x, final int y, int color, JSONObject obj){

        final int pixelsX = (int) (x * scale + 0.5f);
        final int pixelsY = (int) (y * scale + 0.5f);
        final JSONObject object = obj;

        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( 50 );
        biggerCircle.setIntrinsicWidth( 50);
        biggerCircle.setBounds(new Rect(0, 0, 50, 50));
        biggerCircle.getPaint().setColor(Color.WHITE);

        ShapeDrawable smallerCircle= new ShapeDrawable( new OvalShape());
        smallerCircle.setIntrinsicHeight( 10 );
        smallerCircle.setIntrinsicWidth( 10);
        smallerCircle.setBounds(new Rect(0, 0, 10, 10));
        smallerCircle.getPaint().setColor(color);
        smallerCircle.setPadding(40,40,40,40);
        Drawable[] d = {smallerCircle,biggerCircle};

        LayerDrawable composite1 = new LayerDrawable(d);

        btn = new Button(getApplicationContext());
        btn.setX(pixelsX);
        btn.setY(pixelsY);
        btn.setLayoutParams(new LinearLayout.LayoutParams(140, 140));
        btn.setBackgroundDrawable(composite1);
        btn.setBackground(composite1);
        btn.setText("4");
        btn.setTextColor(Color.BLACK);
        try {
            btn.setId(obj.getInt("objectId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Button finalBtn = btn;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clubActivityRelativeLayout.addView(finalBtn);
                finalBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "x: " + pixelsX + " y: " + pixelsY, Toast.LENGTH_LONG).show();
                        showDialog(object);
                    }
                });
            }
        });
    }

    void showDialog(JSONObject obj) {
        Bundle args = new Bundle();
        boolean free = true;
        try {
            free = obj.getBoolean("availability");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            args.putInt("partnerId", partnerId);
            args.putInt("objectId", obj.getInt("objectId"));
            args.putInt("numberOfSeats", obj.getInt("numberOfSeats"));
            args.putInt("timeOut", obj.getInt("timeOut"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if user is employee in this partner's room
        boolean employee = true;
        FragmentManager fm = getSupportFragmentManager();
//        if(employee){
//            args.putBoolean("free", free);
//            EmployeeReservationDialog employeeReservationDialog = new EmployeeReservationDialog();
//            employeeReservationDialog.setArguments(args);
//            employeeReservationDialog.show(fm, "employeeReservationDialog");
//        }
        args.putBoolean("free", free);
        ReservationDialog reservationDialog = new ReservationDialog();
        reservationDialog.setArguments(args);
        reservationDialog.show(fm, "reservation_name");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_club, menu);

        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onFinishReservationDialog(String inputText, int pId, int oId, int numberOfSeats, int timeOut) {
        if (inputText.equals("OK")) {
            out.println("rezervacija:" + pId + ":" + oId + ":" + numberOfSeats + ":" + timeOut + ":" + "korisnik");
            // TO DO (partner_name, duration of reservation)

            db.deleteReservations();

            db.addReservation(club_name, (long) timeOut * 60);
            Reservation res = db.getReservation();
            Log.i("MOJA PROVERA ", res.getName() + " , " + res.getExpiresAt() );

            SetCardView();
        }
        Toast.makeText(getApplicationContext(), "Poruka, " + inputText, Toast.LENGTH_SHORT).show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        int counter = 0;
        if ( position >= 1 && position <= 4) {
            counter = 1;
        } else if (position >= 5 && position <= 7) {
            counter = 2;
        } else if (position >= 8 ) {
            counter = 3;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
                break;

            case 6:
                transaction.replace(R.id.your_placeholder, new ClubOwnerDetail());
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case 7:
                transaction.replace(R.id.your_placeholder, new ClubOwnerWaiter());
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case 9:
                Bundle mArgs = new Bundle();
                mArgs.putString("partnerSetup", partnerSetup.toString());

                layoutWidth = clubActivityRelativeLayout.getWidth();
                layoutHeight = clubActivityRelativeLayout.getHeight();
                mArgs.putInt("layoutHeight", layoutHeight);
                mArgs.putInt("layoutWidth", layoutWidth);
                mArgs.putInt("partnerId", partnerId);
                mArgs.putString("layoutImgUrl", layoutImgUrl);

                EditPosition editPosition = new EditPosition();
                editPosition.setArguments(mArgs);

                transaction.replace(R.id.your_placeholder, editPosition);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position - counter, true);
        setTitle(mPlanetTitles[position - counter]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            thrd = new Thread(new Runnable() {
                public void run() {
                    try {
                        sock = new Socket(getApplicationContext().getString(R.string.socketAddress), 4444);
                        out = new PrintWriter(sock.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                        out.println("hi:"+partnerId);
                        String data = null;
                        data = in.readLine();
                        Log.i(TAG, data);
                        while (!Thread.interrupted() && (data = in.readLine()) != null) {
                            String[] helper = data.split(":");
                            if(helper[0].equals("rezervacija")) {
                                changeObject(false, helper[1]);
                            }else if(helper[0].equals("oslobodi")){
                                changeObject(true, helper[1]);
                            }else if(helper[1].equals("partnerObjectSetup")){
                                Log.i("Poruka ", " Sigla je poruka za novi partner objekat");
                            }else{
                                try {
                                    partnerSetup = new JSONArray(data);
                                    initialPartnerSetup(partnerSetup);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Log.i(TAG, data);
                            Log.i("JSONARRAY looks like", partnerSetup.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // do something in ui thread with the data var
                                }
                            });
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "onResume error inside thread! " + e.getMessage());
                    }
                }
            });
            thrd.start();
        } catch (Exception e) {
            Log.e(TAG, "onResume error! " + e.getMessage());
        }
    }

    private void changeObject(boolean b, String s) {
        final int objectId = Integer.parseInt(s);
        if(b){
            editColorInObject(objectId, Color.GREEN);
        }else {
            editColorInObject(objectId, Color.RED);
        }
    }

    private void editColorInObject(final int objectId, final int color){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btn = (Button) clubActivityRelativeLayout.findViewById(objectId);
                for(int i = 0; i < partnerSetup.length(); i++){
                    try {
                        JSONObject obj = partnerSetup.getJSONObject(i);
                        if(obj.getInt("objectId") == objectId){
                            setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), color, obj);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initialPartnerSetup(JSONArray partnerSetup) {
        Log.i("initial Partner Setup", partnerSetup.toString());
        for (int i = 0; i < partnerSetup.length(); i++) {
            Button btn = new Button(getApplicationContext());
            try {
                JSONObject obj = partnerSetup.getJSONObject(i);
                if(obj.getString("type").equals("sto")){
                    Log.i("Type sto", obj.getString("type"));
                    if (obj.getBoolean("availability")) {
                        Log.i("availability", obj.getBoolean("availability")+"");
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.GREEN, obj);
                    } else {
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.RED, obj);
                    }
                } else if (obj.getString("type").equals("separe")) {
                    Log.i("Type separe", obj.getString("type"));
                    if (obj.getBoolean("availability")) {
                        Log.i("availability", obj.getBoolean("availability")+"");
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.GREEN, obj);
                    } else {
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.RED, obj);
                    }
                } else {
                    Log.i("Type stajanje", obj.getString("type"));
                    if (obj.getBoolean("availability")) {
                        Log.i("availability", obj.getBoolean("availability")+"");
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.GREEN, obj);
                    } else {
                        setButton(btn, obj.getInt("coordinateX"), obj.getInt("coordinateY"), Color.RED, obj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        out.println("bye:"+partnerId);
        try {
            if (thrd != null)
                thrd.interrupt();
            if (sock != null) {
                sock.getOutputStream().close();
                sock.getInputStream().close();
                sock.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "onPause error! " + e.getMessage());
        }
        thrd = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            out.println("bye:"+partnerId);
            if (thrd != null)
                thrd.interrupt();
            if (sock != null) {
                sock.getOutputStream().close();
                sock.getInputStream().close();
                sock.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "onPause error! " + e.getMessage());
        }
        thrd = null;
    }

    @Override
    public void onFinishDialog(boolean success, int partnerId, int objectId, boolean stateReservation, boolean delete) {
        if (success) {
//            if (!stateReservation) {
//                out.println("rezervacija:" + partnerId + ":" + objectId + ":" + 4 + ":" + 40 + ":" + "konobar");
//            } else if (stateReservation) {
//                out.println("oslobodi:" + partnerId + ":" + objectId);
//            } else if (delete) {
//               // obrisi sto
//            }
            if(delete) {
                Log.i("delete ", "true");
            }else {
                if(!stateReservation) {
                    // rezervisi (setuje zauzeto)
                    out.println("rezervacija:" + partnerId + ":" + objectId + ":" + 4 + ":" + 40 + ":" + "konobar");
                } else {
                    out.println("oslobodi:" + partnerId + ":" + objectId);
                }
            }
        }


        Toast.makeText(getApplicationContext(), "Poruka, " + success, Toast.LENGTH_SHORT).show();
    }

    public void SetCardView() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
