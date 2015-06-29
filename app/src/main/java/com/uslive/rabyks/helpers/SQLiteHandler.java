package com.uslive.rabyks.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uslive.rabyks.models.Partner;
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
    // Partner table name
    private static final String TABLE_PARTNER = "partner";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    // Partner Table Columns names
    private static final String PARTNER_ID = "id";
    private static final String PARTNER_NAME = "name";
    private static final String PARTNER_ADDRESS = "address";
    private static final String PARTNER_NUMBER = "number";
    private static final String PARTNER_LOGO_URL = "logo_url";
    private static final String PARTNER_LOGO_URL_BYTES = "logo_url_bytes";
    private static final String PARTNER_TYPE = "type";
    private static final String PARTNER_WORKING_HOURS = "working_hours";
    private static final String PARTNER_CREATED_AT = "created_at";
    private static final String PARTNER_MODIFIED_AT = "modified_at";

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

        String CREATE_PARTNER_TABLE = "CREATE TABLE " + TABLE_PARTNER + "("
                + PARTNER_ID + " INTEGER PRIMARY KEY," + PARTNER_NAME + " TEXT,"
                + PARTNER_ADDRESS + " TEXT," + PARTNER_NUMBER + " TEXT,"
                + PARTNER_LOGO_URL + " TEXT," + PARTNER_LOGO_URL_BYTES + " BLOB,"
                + PARTNER_TYPE + " INTEGER," + PARTNER_WORKING_HOURS + " TEXT,"
                + PARTNER_CREATED_AT + " INTEGER," + PARTNER_MODIFIED_AT + " INTEGER" + ")";
        db.execSQL(CREATE_PARTNER_TABLE);

        String CREATE_TABLE_RESERVATION = "CREATE TABLE " + TABLE_RESERVATION + "("
                + RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + RESERVATION_CLUB_NAME + " TEXT,"
                + RESERVATION_CREATED_AT + " TIMESTAMP," + RESERVATION_EXPIRES_AT + " INTEGER,"
                + RESERVATION_CURRENT_STATUS + " NUMERIC" + ")";
        db.execSQL(CREATE_TABLE_RESERVATION);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER);

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
    public void addPartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARTNER_ID, partner.getId());
        values.put(PARTNER_NAME, partner.getName());
        values.put(PARTNER_ADDRESS, partner.getAddress());
        values.put(PARTNER_NUMBER, partner.getNumber());
        values.put(PARTNER_LOGO_URL, partner.getLogo_url());
        values.put(PARTNER_LOGO_URL_BYTES, partner.getLogo_url_bytes());
        values.put(PARTNER_TYPE, partner.getType());
        values.put(PARTNER_WORKING_HOURS, partner.getWorking_hours());
        values.put(PARTNER_CREATED_AT, partner.getCreated_at());
        values.put(PARTNER_MODIFIED_AT, partner.getModified_at());

        // Inserting Row
        long id = db.insert(TABLE_PARTNER, null, values);
        Log.d(TAG, "New partner inserted into sqlite: " + id);
        Log.i(TAG, "New partner inserted into sqlite: " + id + " date: " + partner.getCreated_at());

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
     * Getting partners data from database
     **/
    public List<Partner> getAllPartners() {
        List<Partner> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM "+ TABLE_PARTNER +" ORDER BY name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Partner partner = new Partner();
                partner.setId(cursor.getInt(cursor.getColumnIndex("id")));
                partner.setName(cursor.getString(cursor.getColumnIndex("name")));
                partner.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                partner.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                partner.setLogo_url(cursor.getString(cursor.getColumnIndex("logo_url")));
                partner.setLogo_url_bytes(cursor.getBlob(cursor.getColumnIndex("logo_url_bytes")));
                partner.setType(cursor.getInt(cursor.getColumnIndex("type")));
                partner.setWorking_hours(cursor.getString(cursor.getColumnIndex("working_hours")));
                partner.setCreated_at(cursor.getLong(cursor.getColumnIndex("created_at")));
                partner.setModified_at(cursor.getLong(cursor.getColumnIndex("modified_at")));
                contactList.add(partner);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close inserting data from database
        db.close();
        // return contact list
        return contactList;
    }

    // Search for partners by some value
    public List<Partner> getPartnersBySpecificParametar(String value) {
        List<Partner> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "Select * FROM partner WHERE name LIKE '"+ value +"%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Partner partner = new Partner();
                partner.setId(cursor.getInt(cursor.getColumnIndex("id")));
                partner.setName(cursor.getString(cursor.getColumnIndex("name")));
                partner.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                partner.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                partner.setLogo_url(cursor.getString(cursor.getColumnIndex("logo_url")));
                partner.setLogo_url_bytes(cursor.getBlob(cursor.getColumnIndex("logo_url_bytes")));
                partner.setType(cursor.getInt(cursor.getColumnIndex("type")));
                partner.setWorking_hours(cursor.getString(cursor.getColumnIndex("working_hours")));
                partner.setCreated_at(cursor.getLong(cursor.getColumnIndex("created_at")));
                partner.setModified_at(cursor.getLong(cursor.getColumnIndex("modified_at")));
                // Adding contact to list
                contactList.add(partner);
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
     * Getting timestamp of last added partner
     * */
    public Long getTimestampOfLastPartner() {

        String getQuery = "SELECT " + PARTNER_CREATED_AT + " FROM " + TABLE_PARTNER + " ORDER BY " + PARTNER_CREATED_AT + " DESC LIMIT 1";
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

    public void updateTimestampPartner(String partnerName)
    {
        Long tsLong = System.currentTimeMillis()/1000;

        ContentValues cv = new ContentValues();
        cv.put("created_at", tsLong);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PARTNER, cv, PARTNER_NAME +"='"+partnerName+"'", null);
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
    public void deletePartners() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PARTNER, null, null);
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
