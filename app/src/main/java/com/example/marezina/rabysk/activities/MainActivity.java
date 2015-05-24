package com.example.marezina.rabysk.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marezina.rabysk.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clubs.ImageAdapter;
import clubs.SearchClubAdapter;
import helper.SQLiteHandler;
import helper.SessionManager;
import models.Club;


public class MainActivity extends ActionBarActivity {

    private SQLiteHandler db;
    private SessionManager session;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    ArrayList<Club> imageArry = new ArrayList<Club>();
    SearchClubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getBaseContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mPlanetTitles));
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

        //Part for inserting pictures in SQLite
        Bitmap image = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_0);
        Bitmap image1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_1);
        Bitmap image2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_2);
        Bitmap image3 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_3);
        // convert bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        image1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte image1InByte[] = stream.toByteArray();
        image2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte image2InByte[] = stream.toByteArray();
        image3.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte image3InByte[] = stream.toByteArray();

        db = new SQLiteHandler(getApplicationContext());
        db.addClub(new Club("kuce", "url_do_slike", imageInByte, "uuid_slike", "20.05.2015"));
        db.addClub(new Club("kuce1", "url_do_slike_1", image1InByte, "uuid_slike_1", "21.05.2015"));
        db.addClub(new Club("kuce2", "url_do_slike_2", image2InByte, "uuid_slike_2", "22.05.2015"));
        db.addClub(new Club("kuce3", "url_do_slike_3", image3InByte, "uuid_slike_3", "23.05.2015"));



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
    public boolean onCreateOptionsMenu(Menu menu) {
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

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
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.

                // Reading all contacts from database

                Club searchResult = null;

                Club cl = db.getClubByString(query);
                if(cl != null){
                    String value = "ID:" + cl.get_id() + " Name: " + cl.get_name() + " Url: " + cl.get_url()
                            + " Image: " + cl.get_image() + " Uid: " + cl.get_uid() + " Created_at: " + cl.get_created_at();
                    Toast.makeText(getBaseContext(), value, Toast.LENGTH_LONG).show();
                }

                List<Club> clubs = db.getAllClubs();
                for (Club club : clubs) {
                    if(club.get_name() == query ){
                        searchResult = club;
                    }
                    String log = "ID:" + club.get_id() + " Name: " + club.get_name() + " Url: " + club.get_url()
                            + " Image: " + club.get_image() + " Uid: " + club.get_uid() + " Created_at: " + club.get_created_at();

                    // Writing Clubs to log
                    Log.d("Result: ", log);
                    //add contacts data in arrayList
                    imageArry.add(club);

                }
//                adapter = new SearchClubAdapter(getBaseContext(), R.layout.search_list, imageArry);
//                ListView dataList = (ListView) findViewById(R.id.list);
//                dataList.setAdapter(adapter);
                if(searchResult != null){
                    String value = "ID:" + searchResult.get_id() + " Name: " + searchResult.get_name() + " Url: " + searchResult.get_url()
                            + " Image: " + searchResult.get_image() + " Uid: " + searchResult.get_uid() + " Created_at: " + searchResult.get_created_at();
                    Toast.makeText(getBaseContext(), value, Toast.LENGTH_LONG).show();
                    return true;
                }

                Toast.makeText(getBaseContext(), "No results on that query!!!", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        item.setTitle(name);

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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
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
            case R.id.logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}
