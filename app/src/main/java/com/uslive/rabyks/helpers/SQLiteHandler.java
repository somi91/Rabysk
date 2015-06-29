package com.uslive.rabyks.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uslive.rabyks.models.Club;
import com.uslive.rabyks.models.Reservation;

/**
 * Created by marezina on 19.5.2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "rabyks";

    // Login table name
    private static final String TABLE_USER = "user";
    // Club table name
    private static final String TABLE_CLUB = "club";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    // Club Table Columns names
    private static final String KEY_CLUB_ID = "id";
    private static final String KEY_CLUB_NAME = "name";
    private static final String KEY_CLUB_URL = "url";
    private static final String KEY_CLUB_IMG = "image";
    private static final String KEY_CLUB_UID = "uid";
    private static final String KEY_CLUB_CREATED_AT = "created_at";

    // Reservation table
    private static final String TABLE_RESERVATION = "reservation";

    private static final String RESERVATION_ID = "id";
    private static final String RESERVATION_CLUB_NAME = "name";
    private static final String RESERVATION_CREATED_AT = "created_at";
    private static final String RESERVATION_EXPIRES_AT = "expires_at";
    private static final String RESERVATION_CURRENT_STATUS = "current_status";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_CLUB_TABLE = "CREATE TABLE " + TABLE_CLUB + "("
                + KEY_CLUB_ID + " INTEGER PRIMARY KEY," + KEY_CLUB_NAME + " TEXT,"
                + KEY_CLUB_URL + " INTEGER UNIQUE," + KEY_CLUB_IMG + " BLOB,"
                + KEY_CLUB_UID + " INTEGER," + KEY_CLUB_CREATED_AT + " TIMESTAMP" + ")";
        db.execSQL(CREATE_CLUB_TABLE);

        String CREATE_TABLE_RESERVATION = "CREATE TABLE " + TABLE_RESERVATION + "("
                + RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + RESERVATION_CLUB_NAME + " TEXT,"
                + RESERVATION_CREATED_AT + " TIMESTAMP," + RESERVATION_EXPIRES_AT + " TIMESTAMP,"
                + RESERVATION_CURRENT_STATUS + " NUMERIC" + ")";
        db.execSQL(CREATE_TABLE_RESERVATION);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLUB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
        Log.i(TAG, "New user inserted into sqlite: " + id + " date: " + created_at);
    }

    /**
     * Storing club details in database
     * */
    public void addClub(Club club) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLUB_NAME, club.get_name()); // Name
        values.put(KEY_CLUB_URL, club.get_url()); // Email
        values.put(KEY_CLUB_IMG, club.get_image()); // Image
        values.put(KEY_CLUB_UID, club.get_uid()); // Email
        values.put(KEY_CLUB_CREATED_AT, club.get_created_at()); // Created At

        // Inserting Row
        long id = db.insert(TABLE_CLUB, null, values);
        Log.d(TAG, "New club inserted into sqlite: " + id);
        Log.i(TAG, "New club inserted into sqlite: " + id + " date: " + club.get_created_at());

        db.close(); // Closing database connection

    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting clubs data from database
     **/
    public List<Club> getAllClubs() {
        List<Club> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM "+ TABLE_CLUB +" ORDER BY name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Club club = new Club();
                club.set_id(Integer.parseInt(cursor.getString(0)));
                club.set_name(cursor.getString(1));
                club.set_url(cursor.getInt(2));
                club.set_image(cursor.getBlob(3));
                club.set_uid(cursor.getInt(4));
                club.set_created_at(cursor.getLong(5));
                // Adding contact to list
                contactList.add(club);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close inserting data from database
        db.close();
        // return contact list
        return contactList;
    }

    // Search for clubs by some value
    public List<Club> getClubsBySpecificParametar(String value) {
        List<Club> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "Select * FROM club WHERE name LIKE '"+ value +"%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Club club = new Club();
                club.set_id(Integer.parseInt(cursor.getString(0)));
                club.set_name(cursor.getString(1));
                club.set_url(cursor.getInt(2));
                club.set_image(cursor.getBlob(3));
                club.set_uid(cursor.getInt(4));
                club.set_created_at(cursor.getLong(5));
                // Adding contact to list
                contactList.add(club);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close inserting data from database
        db.close();
        // return contact list
        return contactList;
    }

    /**
     * Getting user login status return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Getting club status return true if rows are there in table
     * */
    public int getClubRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CLUB;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Getting timestamp of last added partner
     * */
    public Long getTimestampOfLastPartner() {

        String getQuery = "SELECT " + KEY_CLUB_CREATED_AT + " FROM " + TABLE_CLUB + " ORDER BY " + KEY_CLUB_CREATED_AT + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getQuery, null);
        Long timestamp = null;
        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            timestamp = cursor.getLong(0);
            cursor.close();
        }else{
            db.close();
        }
        db.close();
        return timestamp;
    }

    public void updateTimestampClub(String clubName)
    {
        Long tsLong = System.currentTimeMillis()/1000;

        ContentValues cv = new ContentValues();
        cv.put("created_at", tsLong);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CLUB, cv, KEY_CLUB_NAME +"='"+clubName+"'", null);
        db.close();
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteClubs() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CLUB, null, null);
        db.close();

        Log.d(TAG, "Deleted all clubs info from sqlite");
    }

    public void updateReservationTable()
    {
        ContentValues cv = new ContentValues();
        cv.put(RESERVATION_CURRENT_STATUS, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_RESERVATION, cv, RESERVATION_CURRENT_STATUS + "=1", null);
        db.close();
    }

    public void addReservation(String club_name, Long reservation_duration) {
        SQLiteDatabase db = this.getWritableDatabase();

        Long tsLong = System.currentTimeMillis()/1000;
        Long expires_at = tsLong + reservation_duration;
        ContentValues values = new ContentValues();
        values.put(RESERVATION_CLUB_NAME, club_name);
        values.put(RESERVATION_CREATED_AT, tsLong);
        values.put(RESERVATION_EXPIRES_AT, expires_at);
        values.put(RESERVATION_CURRENT_STATUS, 1);

        // Inserting Row
        long id = db.insert(TABLE_RESERVATION, null, values);
        Log.i(TAG, "New reservation inserted into sqlite: " + id + " created at: " + tsLong);
        Log.i(TAG, "New reservation expires at:" + expires_at);

        db.close(); // Closing database connection

    }

    public Reservation getReservation() {

        String getQuery = "SELECT * FROM " + TABLE_RESERVATION + " WHERE " + RESERVATION_CURRENT_STATUS +"=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getQuery, null);
        Reservation reservation = null;
        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();

            reservation = new Reservation();

            reservation.setId(cursor.getInt(cursor.getColumnIndex("id")));
            reservation.setName(cursor.getString(cursor.getColumnIndex("name")));
            reservation.setCreatedAt(cursor.getLong(cursor.getColumnIndex("created_at")));
            reservation.setExpiresAt(cursor.getLong(cursor.getColumnIndex("expires_at")));
            reservation.setCurrentStatus(cursor.getInt(cursor.getColumnIndex("current_status"))>0);

            cursor.close();
        }else{
            db.close();
        }
        db.close();
        return reservation;
    }

    public void deleteReservations() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_RESERVATION, null, null);
        db.close();

        Log.d(TAG, "Deleted all reservations info from sqlite");
    }

}
