package com.uslive.rabyks.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.uslive.rabyks.AsyncTasks.GetPartners;
import com.uslive.rabyks.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import com.uslive.rabyks.adapters.ImageAdapter;
import com.uslive.rabyks.adapters.MainDrawerAdapter;
import com.uslive.rabyks.helpers.SQLiteHandler;
import com.uslive.rabyks.helpers.SessionManager;
import com.uslive.rabyks.models.Club;


public class MainActivity extends ActionBarActivity {

    private SQLiteHandler db;
    private SessionManager session;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetPartners(getApplicationContext()).execute("", "", "");

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

//        GetSetDataForGrid();
        /**
         * Dovuci iz sqlite-a najnoviji datum partnera i posalji na server pa
         * proveri da li treba raditi update sqlite-a? (da li ima novih partnera sa novijim timestampom)
         *
         * Za svakog partnera koji je nov napravi novi FetchPartnerData task (ubacuje partnera u sqlite)
         *
         * Pa tek onda iscitaj sve podatke iz sqlite i prikazi u main_view
         *
         */

        // Search and initial Grid View
        db = new SQLiteHandler(getApplicationContext());
        List<Club> clubs = db.getAllClubs();
        gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this, clubs);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Club club = (Club) gridview.getItemAtPosition(position);
                Intent clubIntent = new Intent(getApplicationContext(), ClubActivity.class);
                String club_id = "" + club.get_id();
                clubIntent.putExtra("club_id", club_id);
                clubIntent.putExtra("club_name", club.get_name());
                clubIntent.putExtra("club_uid", club.get_uid());
                clubIntent.putExtra("club_url", club.get_url());
                clubIntent.putExtra("club_created_at", club.get_created_at());
                startActivity(clubIntent);
                finish();

//                adapter.getItem(position);
//                Toast.makeText(getBaseContext(), "" + club.get_name(), Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    public void GetSetDataForGrid(){
        db = new SQLiteHandler(getApplicationContext());
        //Part for inserting pictures in SQLite
        Bitmap image = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_0);
        Bitmap image1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_1);
        Bitmap image2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_2);
        Bitmap image3 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_3);
        Bitmap image4 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_4);
        Bitmap image5 = BitmapFactory.decodeResource(getResources(),
                R.drawable.sample_5);

        // convert bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();

        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte image1InByte[] = stream1.toByteArray();

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        image2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
        byte image2InByte[] = stream2.toByteArray();

        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        image3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
        byte image3InByte[] = stream3.toByteArray();

        ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
        image4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
        byte image4InByte[] = stream4.toByteArray();

        ByteArrayOutputStream stream5 = new ByteArrayOutputStream();
        image5.compress(Bitmap.CompressFormat.JPEG, 100, stream5);
        byte image5InByte[] = stream5.toByteArray();

        db.addClub(new Club("kuce", "url_do_slike", imageInByte, "uuid_slike", "20.05.2015"));
        db.addClub(new Club("kuce1", "url_do_slike_1", image1InByte, "uuid_slike_1", "21.05.2015"));
        db.addClub(new Club("kuce2", "url_do_slike_2", image2InByte, "uuid_slike_2", "22.05.2015"));
        db.addClub(new Club("pace", "url_do_slike_3", image3InByte, "uuid_pace", "23.05.2015"));
        db.addClub(new Club("mace", "url_do_mace", image4InByte, "uuid_mace", "01.06.2015"));
        db.addClub(new Club("mace1", "url_do_mace1", image5InByte, "uuid_mace1", "01.06.2015"));
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
            public boolean onQueryTextChange(String query) {
                // this is your adapter that will be filtered
                List<Club> clubs = db.getClubsBySpecificParametar(query);
                for (Club club : clubs) {

                }
                gridview.setAdapter(new ImageAdapter(getApplicationContext(), clubs));
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Club searchResult = null;
                List<Club> clubs = db.getClubsBySpecificParametar(query);
                for (Club club : clubs) {
                    if(club.get_name().equals(query)){
                        searchResult = club;
                    }

                }
                if(searchResult != null){
                    gridview.setAdapter(new ImageAdapter(getApplicationContext(), clubs));
                }else {
                    Toast.makeText(getBaseContext(), "No results on that query!!!", Toast.LENGTH_SHORT).show();
                }

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
            case R.id.logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        return super.onOptionsItemSelected(item);
    }
}
