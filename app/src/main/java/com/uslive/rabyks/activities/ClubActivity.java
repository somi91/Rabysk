package com.uslive.rabyks.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uslive.rabyks.R;
import com.uslive.rabyks.services.GPSTracker;
import com.uslive.rabyks.adapters.MainDrawerAdapter;
import com.uslive.rabyks.dialogs.EmployeeReservationDialog;
import com.uslive.rabyks.fragments.ClubOwnerDetail;
import com.uslive.rabyks.fragments.ClubOwnerWaiter;
import com.uslive.rabyks.fragments.EditPosition;
import com.uslive.rabyks.dialogs.ReservationDialog;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClubActivity extends ActionBarActivity implements ReservationDialog.EditNameDialogListener, EmployeeReservationDialog.EditDialogListener {

    int sdk = android.os.Build.VERSION.SDK_INT;

    private static final String TAG = "ClubActivity";

    private TextView club_id;
    private TextView club_name;
    private TextView club_uuid;
    private TextView club_url;
    private TextView club_created_at;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private RelativeLayout clubActivityRelativeLayout;
    float scale;

    GPSTracker gps;

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    private Thread thrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        clubActivityRelativeLayout = (RelativeLayout) findViewById(R.id.clubActivityRelativeLayout);
        scale = getApplicationContext().getResources().getDisplayMetrics().density;

        TextView club_id = (TextView) findViewById(R.id.id);
        TextView club_name = (TextView) findViewById(R.id.name);
        TextView club_uuid = (TextView) findViewById(R.id.uuid);
        TextView club_url = (TextView) findViewById(R.id.url);
        TextView club_created_at = (TextView) findViewById(R.id.created_at);

        Intent myIntent = getIntent(); // gets the previously created intent
        club_id.setText(myIntent.getStringExtra("club_id"));
        club_name.setText(myIntent.getStringExtra("club_name"));
        club_uuid.setText(myIntent.getStringExtra("club_uuid"));
        club_url.setText(myIntent.getStringExtra("club_url"));
        club_created_at.setText(myIntent.getStringExtra("club_created_at"));

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mTitle = mDrawerTitle = getTitle();
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

        Button btn1 = new Button(getApplicationContext());
        setButton(btn1, 50, 100, Color.GREEN);

        Button btn2 = new Button(getApplicationContext());
        setButton(btn2, 100, 200, Color.RED);

        Button btn3 = new Button(getApplicationContext());
        setButton(btn3, 200, 120, Color.GREEN);

        Button btn4 = new Button(getApplicationContext());
        setButton(btn4, 200, 220, Color.RED);

        setBackGroundImage();
//        doBindService();
        Button btnPopup1 = (Button) findViewById(R.id.btnPopup1);
        Button btnPopup2 = (Button) findViewById(R.id.btnPopup2);

        btnPopup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

//        doBindService();
    }

    public void setBackGroundImage(){

        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            clubActivityRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sample_0, null) );
        } else {
            clubActivityRelativeLayout.setBackground(getResources().getDrawable(R.drawable.sample_0, null));
        }
    }

    public void setButton(Button btn, final int x, final int y, int color){

        final int pixelsX = (int) (x * scale + 0.5f);
        final int pixelsY = (int) (y * scale + 0.5f);

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
        clubActivityRelativeLayout.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "x: "+ pixelsX +" y: " + pixelsY, Toast.LENGTH_LONG).show();
            }
        });

    }

    void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ReservationDialog editNameDialog = new ReservationDialog();
        editNameDialog.show(fm, "reservation_name");
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
    public void onFinishEditDialog(String inputText) {
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
                transaction.replace(R.id.your_placeholder, new EditPosition());
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

    private View.OnClickListener stopListener = new View.OnClickListener() {
        public void onClick(View v){
        }
    };

    private View.OnClickListener sendMessage = new View.OnClickListener() {
        public void onClick(View v){
            out.println("rezervacija:dragstor:1:1:1");
        }
    };

//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            thrd = new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        sock = new Socket("10.0.2.2", 4444);
//                        out = new PrintWriter(sock.getOutputStream(), true);
//                        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//                        out.println("club:dragstor");
//                        String data = null;
//                        data = in.readLine();
//                        Log.i(TAG, data);
//                        while (!Thread.interrupted()) {
//                            data = in.readLine();
//                            Log.i(TAG, "BRATE KAKO PROLAZIS");
//                            if(data != null) Log.i(TAG, "STIGLO NESTO " + data);
////                            if (data != null)
////                                runOnUiThread(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        // do something in ui thread with the data var
////                                    }
////                                });
//                        }
//                    } catch (IOException e) {
//                        Log.e(TAG, "onResume error inside thread! " + e.getMessage());
//                    }
//                }
//            });
//            thrd.start();
//        } catch (Exception e) {
//            Log.e(TAG, "onResume error! " + e.getMessage());
//        }
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        out.println("bye:dragstor");
//        try {
//            if (thrd != null)
//                thrd.interrupt();
//            if (sock != null) {
//                sock.getOutputStream().close();
//                sock.getInputStream().close();
//                sock.close();
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "onPause error! " + e.getMessage());
//        }
//        thrd = null;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            out.println("bye:dragstor");
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
    public void onFinishDialog(String inputText) {
        Toast.makeText(getApplicationContext(), "Poruka, " + inputText, Toast.LENGTH_SHORT).show();
    }

}
