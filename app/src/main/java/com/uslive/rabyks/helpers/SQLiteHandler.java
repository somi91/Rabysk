package com.uslive.rabyks.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uslive.rabyks.models.Partner;
import com.uslive.rabyks.models.Reservation;
import com.uslive.rabyks.models.User;
import com.uslive.rabyks.models.UserPartner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marezina on 19.5.2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "rabyks";

    private static final String TABLE_USER = "user";

    private static final String TABLE_PARTNER = "partner";

    private static final String TABLE_RESERVATION = "reservation";

    private static final String TABLE_USER_PARTNER = "userPartner";

    private static final String USER_ID = "id";
    private static final String USER_NUMBER = "number";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ROLE = "role";

    private static final String PARTNER_ID = "id";
    private static final String PARTNER_NAME = "name";
    private static final String PARTNER_ADDRESS = "address";
    private static final String PARTNER_NUMBER = "number";
    private static final String PARTNER_LOGO_URL = "logo_url";
    private static final String PARTNER_LOGO_URL_BYTES = "logo_url_bytes";
    private static final String PARTNER_LAYOUT_IMG_URL = "layout_img_url";
    private static final String PARTNER_TYPE = "type";
    private static final String PARTNER_WORKING_HOURS = "working_hours";
    private static final String PARTNER_CREATED_AT = "created_at";
    private static final String PARTNER_MODIFIED_AT = "modified_at";

    private static final String RESERVATION_ID = "id";
    private static final String RESERVATION_CLUB_NAME = "name";
    private static final String RESERVATION_PARTNER_ID = "partnerId";
    private static final String RESERVATION_OBJECT_ID = "objectId";
    private static final String RESERVATION_TYPE = "type";
    private static final String RESERVATION_CREATED_AT = "created_at";
    private static final String RESERVATION_EXPIRES_AT = "expires_at";
    private static final String RESERVATION_CURRENT_STATUS = "current_status";

    private static final String USER_PARTNER_USER_ID = "userId";
    private static final String USER_PARTNER_PARTNER_ID = "partnerId";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY," + USER_NUMBER + " TEXT,"
                + USER_EMAIL + " TEXT UNIQUE," + USER_PASSWORD + " TEXT,"
                + USER_ROLE + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_PARTNER_TABLE = "CREATE TABLE " + TABLE_PARTNER + "("
                + PARTNER_ID + " INTEGER PRIMARY KEY," + PARTNER_NAME + " TEXT,"
                + PARTNER_ADDRESS + " TEXT," + PARTNER_NUMBER + " TEXT,"
                + PARTNER_LOGO_URL + " TEXT," + PARTNER_LOGO_URL_BYTES + " BLOB," + PARTNER_LAYOUT_IMG_URL + " TEXT,"
                + PARTNER_TYPE + " TEXT," + PARTNER_WORKING_HOURS + " TEXT,"
                + PARTNER_CREATED_AT + " INTEGER," + PARTNER_MODIFIED_AT + " INTEGER" + ")";
        db.execSQL(CREATE_PARTNER_TABLE);

        String CREATE_TABLE_RESERVATION = "CREATE TABLE " + TABLE_RESERVATION + "("
                + RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + RESERVATION_CLUB_NAME + " TEXT,"
                + RESERVATION_PARTNER_ID + " INTEGER," + RESERVATION_OBJECT_ID + " INTEGER,"
                + RESERVATION_TYPE + " TEXT," + RESERVATION_CREATED_AT + " TIMESTAMP,"
                + RESERVATION_EXPIRES_AT + " INTEGER," + RESERVATION_CURRENT_STATUS + " NUMERIC" + ")";
        db.execSQL(CREATE_TABLE_RESERVATION);

        String CREATE_USER_PARTNER_TABLE = "CREATE TABLE " + TABLE_USER_PARTNER + "("
                + USER_PARTNER_USER_ID + " INTEGER," + USER_PARTNER_PARTNER_ID + " INTEGER, PRIMARY KEY ("
                + USER_PARTNER_USER_ID + ", " + USER_PARTNER_PARTNER_ID + "))";
        db.execSQL(CREATE_USER_PARTNER_TABLE);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PARTNER);
        // Create tables again
        onCreate(db);
    }

    public User getUser() {

        User user = null;
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
            user.setNumber(cursor.getString(cursor.getColumnIndex(USER_NUMBER)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(USER_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndex(USER_ROLE)));
        }

        cursor.close();
        db.close();
        return user;
    }

    public long addUser(int id, String number, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, id);
        values.put(USER_NUMBER, number);
        values.put(USER_EMAIL, email);
        values.put(USER_PASSWORD, password);
        values.put(USER_ROLE, role);

        long rowId = db.insert(TABLE_USER, null, values);
        db.close();
        Log.d(TAG, "New user inserted with row id: " + rowId);
        return rowId;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getId());
        values.put(USER_NUMBER, user.getNumber());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_ROLE, user.getRole());

        // zbog null svi redovi ce se update-ovati ali jer ima jedan nema veze
        int numberOfUpdatedUsers = db.update(TABLE_USER, values, USER_ID + "=" + user.getId(), null);
        db.close();
        Log.d(TAG, "Number of updated users: " + numberOfUpdatedUsers);
        return numberOfUpdatedUsers;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public int getUserRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    public void addPartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARTNER_ID, partner.getId());
        values.put(PARTNER_NAME, partner.getName());
        values.put(PARTNER_ADDRESS, partner.getAddress());
        values.put(PARTNER_NUMBER, partner.getNumber());
        values.put(PARTNER_LOGO_URL, partner.getLogo_url());
        values.put(PARTNER_LOGO_URL_BYTES, partner.getLogo_url_bytes());
        values.put(PARTNER_LAYOUT_IMG_URL, partner.getLayout_img_url());
        values.put(PARTNER_TYPE, partner.getType());
        values.put(PARTNER_WORKING_HOURS, partner.getWorking_hours());
        values.put(PARTNER_CREATED_AT, partner.getCreated_at());
        values.put(PARTNER_MODIFIED_AT, partner.getModified_at());

        long id = db.insert(TABLE_PARTNER, null, values);
        Log.i(TAG, "New partner inserted into sqlite: " + id + " date: " + partner.getCreated_at());
        db.close();
    }

    public List<Partner> getAllPartners() {

        List<Partner> contactList = new ArrayList<>();
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
                partner.setLayout_img_url(cursor.getString(cursor.getColumnIndex("layout_img_url")));
                partner.setType(cursor.getString(cursor.getColumnIndex("type")));
                partner.setWorking_hours(cursor.getString(cursor.getColumnIndex("working_hours")));
                partner.setCreated_at(cursor.getLong(cursor.getColumnIndex("created_at")));
                partner.setModified_at(cursor.getLong(cursor.getColumnIndex("modified_at")));
                contactList.add(partner);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return contactList;
    }

    public List<Partner> getPartnersBySpecificParameter(String value) {

        List<Partner> contactList = new ArrayList<>();
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
                partner.setLayout_img_url(cursor.getString(cursor.getColumnIndex("layout_img_url")));
                partner.setType(cursor.getString(cursor.getColumnIndex("type")));
                partner.setWorking_hours(cursor.getString(cursor.getColumnIndex("working_hours")));
                partner.setCreated_at(cursor.getLong(cursor.getColumnIndex("created_at")));
                partner.setModified_at(cursor.getLong(cursor.getColumnIndex("modified_at")));
                // Adding contact to list
                contactList.add(partner);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public List<Partner> getPartnerByType(int[] types) {

        List<Partner> pl = new ArrayList<>();
        String selectQuery = "SELECT * FROM partner";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String allTypes = cursor.getString(cursor.getColumnIndex("type"));
                String[] typesS = allTypes.split(",");
                for (String t : typesS) {
                    for(int i = 0; i < types.length; i++){
                        if (types[i] == Integer.parseInt(t)) {
                            Partner partner = new Partner();
                            partner.setId(cursor.getInt(cursor.getColumnIndex("id")));
                            partner.setName(cursor.getString(cursor.getColumnIndex("name")));
                            partner.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                            partner.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                            partner.setLogo_url(cursor.getString(cursor.getColumnIndex("logo_url")));
                            partner.setLogo_url_bytes(cursor.getBlob(cursor.getColumnIndex("logo_url_bytes")));
                            partner.setLayout_img_url(cursor.getString(cursor.getColumnIndex("layout_img_url")));
                            partner.setType(cursor.getString(cursor.getColumnIndex("type")));
                            partner.setWorking_hours(cursor.getString(cursor.getColumnIndex("working_hours")));
                            partner.setCreated_at(cursor.getLong(cursor.getColumnIndex("created_at")));
                            partner.setModified_at(cursor.getLong(cursor.getColumnIndex("modified_at")));
                            // Adding contact to list
                            if(pl.isEmpty()){
                                pl.add(partner);
                                break;
                            }
                            boolean br = false;
                            for(Partner p : pl){
                                if(p.getId() == partner.getId()){
                                    br = true;
                                }
                            }
                            if(!br){
                                pl.add(partner);
                                break;
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pl;
    }

    public Long getTimestampOfLastPartner() {

        String getQuery = "SELECT " + PARTNER_CREATED_AT + " FROM " + TABLE_PARTNER + " ORDER BY " + PARTNER_CREATED_AT + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getQuery, null);
        Long timestamp = null;
        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            timestamp = cursor.getLong(0);
            cursor.close();
        } else {
            db.close();
        }
        db.close();
        return timestamp;
    }

    public void updateTimestampPartner(String partnerName) {
        Long tsLong = System.currentTimeMillis()/1000;
        ContentValues cv = new ContentValues();
        cv.put("created_at", tsLong);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PARTNER, cv, PARTNER_NAME + "='" + partnerName + "'", null);
        db.close();
    }

    public void deletePartners() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNER, null, null);
        db.close();
        Log.d(TAG, "Deleted all clubs info from sqlite");
    }

    public void updateReservationTable() {
        ContentValues cv = new ContentValues();
        cv.put(RESERVATION_CURRENT_STATUS, 0);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_RESERVATION, cv, RESERVATION_CURRENT_STATUS + "=1", null);
        db.close();
    }

    public void addReservation(String club_name, Long reservation_duration, int partnerId, int objectId, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long tsLong = System.currentTimeMillis()/1000;
        Long expires_at = tsLong + reservation_duration;
        ContentValues values = new ContentValues();
        values.put(RESERVATION_CLUB_NAME, club_name);
        values.put(RESERVATION_PARTNER_ID, partnerId);
        values.put(RESERVATION_OBJECT_ID, objectId);
        values.put(RESERVATION_TYPE, type);
        values.put(RESERVATION_CREATED_AT, tsLong);
        values.put(RESERVATION_EXPIRES_AT, expires_at);
        values.put(RESERVATION_CURRENT_STATUS, 1);

        long id = db.insert(TABLE_RESERVATION, null, values);
        Log.i(TAG, "New reservation inserted into sqlite: " + id + " created at: " + tsLong);
        Log.i(TAG, "New reservation expires at:" + expires_at);
        db.close();
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
            reservation.setPartnerId(cursor.getInt(cursor.getColumnIndex("partnerId")));
            reservation.setObjectId(cursor.getInt(cursor.getColumnIndex("objectId")));
            reservation.setType(cursor.getString(cursor.getColumnIndex("type")));
            reservation.setCreatedAt(cursor.getLong(cursor.getColumnIndex("created_at")));
            reservation.setExpiresAt(cursor.getLong(cursor.getColumnIndex("expires_at")));
            reservation.setCurrentStatus(cursor.getInt(cursor.getColumnIndex("current_status"))>0);
            cursor.close();
        } else {
            db.close();
        }
        db.close();
        return reservation;
    }

    public List<String> getReservations() {
        List<String> reservations = new ArrayList<>();
        String getQuery = "SELECT name, count(DISTINCT " + RESERVATION_CREATED_AT + ") as number FROM " + TABLE_RESERVATION + " GROUP BY name ORDER BY number DESC LIMIT 3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getQuery, null);
        int test;
        if (cursor.getCount() != 0 ) {
            if (cursor.moveToFirst()) {
                do {
                    reservations.add(cursor.getString(cursor.getColumnIndex("name")));
                    test = cursor.getInt(cursor.getColumnIndex("number"));
                    Log.i("BROJ REZERVACIJA ", test + "");
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            db.close();
        }
        db.close();
        return reservations;
    }

    public void deleteReservations() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESERVATION, null, null);
        db.close();
        Log.d(TAG, "Deleted all reservations info from sqlite");
    }

    public long addUserPartner(int userId, int partnerId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_PARTNER_USER_ID, userId);
        values.put(USER_PARTNER_PARTNER_ID, partnerId);

        long rowId = db.insert(TABLE_USER_PARTNER, null, values);
        db.close();
        Log.d(TAG, "New user partner inserted with row id: " + rowId);
        return rowId;
    }

    public List<UserPartner> getUserPartnerByUserId(int userId) {
        List<UserPartner> userPartnerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_PARTNER + " WHERE " + USER_PARTNER_USER_ID + " = " + userId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserPartner userPartner = new UserPartner();
                userPartner.setUserId(cursor.getInt(cursor.getColumnIndex(USER_PARTNER_USER_ID)));
                userPartner.setPartnerId(cursor.getInt(cursor.getColumnIndex(USER_PARTNER_PARTNER_ID)));
                userPartnerList.add(userPartner);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userPartnerList;
    }

    public void removeUserPartnerByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_PARTNER, USER_PARTNER_USER_ID + "=" + userId, null);
        db.close();
    }

}
