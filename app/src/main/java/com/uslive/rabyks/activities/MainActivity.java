package com.uslive.rabyks.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uslive.rabyks.AsyncTasks.GetLatestPartners;
import com.uslive.rabyks.AsyncTasks.GetPartners;
import com.uslive.rabyks.AsyncTasks.GetTypes;
import com.uslive.rabyks.AsyncTasks.GetUserRights;
import com.uslive.rabyks.AsyncTasks.OnGetFilterTypesCompleted;
import com.uslive.rabyks.AsyncTasks.OnTaskCompletedUpdateGridView;
import com.uslive.rabyks.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uslive.rabyks.Services.ProximityReciever;
import com.uslive.rabyks.adapters.ImageAdapter;
import com.uslive.rabyks.adapters.MainDrawerAdapter;
import com.uslive.rabyks.fragments.ClubOwnerWaiter;
import com.uslive.rabyks.fragments.ConfirmationFragment;
import com.uslive.rabyks.fragments.FilterFragment;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.helpers.SessionManager;
import com.uslive.rabyks.models.DrawerRow;
import com.uslive.rabyks.models.Partner;
import com.uslive.rabyks.models.PartnerType;
import com.uslive.rabyks.models.Reservation;
import com.uslive.rabyks.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements OnTaskCompletedUpdateGridView, FilterFragment.FinishFilterFragment, OnGetFilterTypesCompleted{

    private SQLiteHandler db;
    private SessionManager session;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    GridView gridview;

    private ProgressBar bar;
    private Reservation res = null;

    private GetTypes getTypes;

    private GetUserRights getUserRights;

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    // Declaring a Location Manager
    protected LocationManager locationManager;

    private Context mContext;
    private LinearLayout filterLayout;
    private Button filter;
    private ArrayList<PartnerType> partnerTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        boolean networkAccess = isNetworkAvailable();

//        DESIGN actionbar
        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#830E93")));


        if(!networkAccess){
//          TODO handle situation when network is disabled
            Log.i("INTERNET???", "KONACNO NEMA NETA");
        }

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//        TODO sta da radim sa session managementom
//        session = new SessionManager(getApplicationContext());
//
//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }

        db = new SQLiteHandler(getApplicationContext());

        res = db.getReservation();
        if (res != null && res.isCurrentStatus()) {
            List<Partner> partners = db.getPartnersBySpecificParameter(res.getName());
            Partner partnerForReservation = partners.get(0);
            CheckLiveReservations(partnerForReservation.getLogo_url_bytes());
        }

        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        filterLayout = (LinearLayout) findViewById(R.id.filter);

        getTypes = new GetTypes(getApplicationContext(), this);
        getTypes.execute();

        // Da li ima usera u lokalnoj bazi?
        User user  = db.getUser();
        // Da li postoji user u remote bazi
        if(user != null) {
            // Ako postoji da li ima visa prava
            getUserRights = new GetUserRights(getApplicationContext(), db, user);
            getUserRights.execute();
        }

        /**
         * Dovuci iz sqlite-a najnoviji datum partnera i posalji na server pa
         * proveri da li treba raditi update sqlite-a? (da li ima novih partnera sa novijim timestampom)
         *
         * Za svakog partnera koji je nov napravi novi FetchPartnerData task (ubacuje partnera u sqlite)
         *
         * Pa tek onda iscitaj sve podatke iz sqlite i prikazi u main_view
         *
         */

        Long timestampForLastPartner = db.getTimestampOfLastPartner();
        if(timestampForLastPartner == null || timestampForLastPartner == 0){
            Log.i("Nothing in database", "First insert in sqlite for partners");
            new GetPartners(getApplicationContext(), this, bar, filterLayout).execute("", "", "");
        }else{
            new GetLatestPartners(getApplicationContext(), this, bar, filterLayout).execute(db.getTimestampOfLastPartner().toString());
        }

        // Search and initial Grid View
        gridview = (GridView) findViewById(R.id.gridview);
//        ImageAdapter adapter = new ImageAdapter(this, clubs);
//        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Partner partner = (Partner) gridview.getItemAtPosition(position);
                Intent clubIntent = new Intent(getApplicationContext(), ClubActivity.class);
//                String partner_id = "" + partner.getId();
                clubIntent.putExtra("partner_id", partner.getId());
                clubIntent.putExtra("partner_name", partner.getName());
                clubIntent.putExtra("partner_created_at", partner.getCreated_at());
                clubIntent.putExtra("partner_layout_img_url", partner.getLayout_img_url());
                startActivity(clubIntent);
                finish();

//                adapter.getItem(position);
//                Toast.makeText(getBaseContext(), "" + club.get_name(), Toast.LENGTH_SHORT).show();
            }
        });

//        mPlanetTitles = getResources().getStringArray(R.array.favorites_array);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mPlanetTitles));

        mDrawerList.setAdapter(new MainDrawerAdapter(this));

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
        filter = (Button) findViewById(R.id.btnFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterFragment();
            }
        });
    }

    private void openFilterFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle filterArgs = new Bundle();

        FilterFragment fragment = FilterFragment.newInstance(partnerTypes);
//        FilterFragment filterFragment = new FilterFragment();

//        filterArgs.putString("partnerId", "sd");
//        filterArgs.putSerializable("describable_key", (Serializable) partnerTypes);
//        filterFragment.setArguments(filterArgs);

        transaction.replace(R.id.mainview, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            showSettingsAlert();
        } else {
            this.canGetLocation = true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Fetching user details from sqlite
//        HashMap<String, String> user = db.getUserDetails();

//        String name = user.get("name");
//        String email = user.get("email");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.name_of_user);

        // Search
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String query) {
                // this is your adapter that will be filtered
                List<Partner> partners = db.getPartnersBySpecificParameter(query);
//                for (Partner partner : partners) {
//
//                }
                gridview.setAdapter(new ImageAdapter(getApplicationContext(), partners));
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Partner searchResult = null;
                List<Partner> partners = db.getPartnersBySpecificParameter(query);
                for (Partner partner : partners) {
                    if(partner.getName().equals(query)){
                        searchResult = partner;
                    }

                }
                if(searchResult != null){
                    gridview.setAdapter(new ImageAdapter(getApplicationContext(), partners));
                }else {
                    Toast.makeText(getBaseContext(), "No results on that query!!!", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

//        item.setTitle(name);
        return super.onCreateOptionsMenu(menu);
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
    public void onTaskCompletedUpdateGridView() {
        db = new SQLiteHandler(getApplicationContext());
        List<Partner> partners = db.getAllPartners();
        gridview.setAdapter(new ImageAdapter(getApplicationContext(), partners));
    }

    @Override
    public void onFinishFilterFragment(int[] searchQuery) {

        int[] query = searchQuery;
        List<Partner> partners = db.getPartnerByType(searchQuery);
        if(partners.size() == 0){
            partners = db.getAllPartners();
        }
//                for (Partner partner : partners) {
//
//                }
        gridview.setAdapter(new ImageAdapter(getApplicationContext(), partners));
    }

    @Override
    public void OnGetFilterTypesCompleted(String array) {
        partnerTypes = new ArrayList<>();
        if(array != null) {
            try {
                JSONArray jsonArray = new JSONArray(array);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        PartnerType pt = new PartnerType();
                        pt.setId(obj.getInt("id"));
                        pt.setName(obj.getString("type"));
                        partnerTypes.add(pt);
                    }
            } catch (JSONException e) {
            }
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            DrawerRow row = (DrawerRow) parent.getItemAtPosition(position);
            selectItem(position, row.getName());
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position, String name) {

        int counter = 0;
        if ( position >= 1 && position <= 4) {
            counter = 1;
        } else if (position >= 5 && position <= 7) {
            counter = 2;
        } else if (position >= 8 ) {
            counter = 3;
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
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        // Handle presses on the action bar items
        switch (id) {
            case R.id.login:
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                
                // TODO da li se ovde gasi aktiviti ili ide do return? posledice?
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }

    private void CheckLiveReservations(byte[] logo) {
        Bundle args = new Bundle();
        args.putString("partnerName", res.getName());
        args.putInt("partnerId", res.getPartnerId());
        args.putInt("objectId", res.getObjectId());
        args.putString("type", res.getType());
        args.putLong("createdAt", res.getCreatedAt());
        args.putLong("expiresAt", res.getExpiresAt());
        args.putByteArray("logoImg", logo);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ConfirmationFragment confirmationFragment = new ConfirmationFragment();
        confirmationFragment.setArguments(args);
        transaction.replace(R.id.mainview, confirmationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        Log.i("onBackPressed ", "Pritisnuto back dugme!!!!!!!!");
        // super.onBackPressed(); // Comment this super call to avoid calling finish()
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
