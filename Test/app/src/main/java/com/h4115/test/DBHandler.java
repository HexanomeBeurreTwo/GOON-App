package com.h4115.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBHandler";

    private static final String TABLE_CHANNEL = "channelTable";
    private static final String TABLE_HAPPENING = "happeningTable";
    private static final String TABLE_USER = "userTable";
    private static final String TABLE_CIRCLE = "circleTable";

    private static final String CHANNEL_ID = "channelId";
    private static final String CHANNEL_NAME = "channelName";
    private static final String CHANNEL_DESCRIPTION = "channelDescription";
    private static final String CHANNEL_TAGS = "channelTags";

    private static final String HAPPENING_ID = "happeningId";
    private static final String HAPPENING_NAME = "happeningName";
    private static final String HAPPENING_DESCRIPTION = "happeningDescription";
    private static final String HAPPENING_LATITUDE = "happeningLatitude";
    private static final String HAPPENING_LONGITUDE = "happeningLongitude";
    private static final String HAPPENING_TEMPORARY = "happeningTemporary";
    private static final String HAPPENING_TAGS = "happeningTags";

    private static final String USER_ID = "userID";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String USER_EMAIL = "userEmail";
    private static final String AGE = "age";
    private static final String CITIZEN = "citizen";
    private static final String TAGS = "tags";

    private static final String CIRCLE_ID = "circleId";
    private static final String CIRCLE_LATITUDE = "circleLatitude";
    private static final String CIRCLE_LONGITUDE = "circleLongitude";

    private static int nbCircles = 0;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        createTables(db);
    }

    private void createTables(SQLiteDatabase db){
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY, "
                + USERNAME + " TEXT NOT NULL, "
                + PASSWORD + " TEXT NOT NULL, "
                + USER_EMAIL + " TEXT NOT NULL, "
                + AGE + " INTEGER, "
                + CITIZEN + " TEXT NOT NULL, "
                + TAGS + " TEXT);";

        String CREATE_CHANNEL_TABLE = "CREATE TABLE " + TABLE_CHANNEL + "("
                + CHANNEL_ID + " INTEGER PRIMARY KEY, "
                + CHANNEL_NAME + " TEXT NOT NULL, "
                + CHANNEL_DESCRIPTION + " TEXT NOT NULL, "
                + CHANNEL_TAGS + " TEXT);";

        String CREATE_HAPPENING_TABLE = "CREATE TABLE " + TABLE_HAPPENING + "("
                + HAPPENING_ID + " INTEGER PRIMARY KEY, "
                + HAPPENING_NAME + " TEXT NOT NULL, "
                + HAPPENING_DESCRIPTION + " TEXT NOT NULL, "
                + HAPPENING_LATITUDE + " DOUBLE NOT NULL, "
                + HAPPENING_LONGITUDE + " DOUBLE NOT NULL, "
                + HAPPENING_TEMPORARY + " BOOLEAN NOT NULL, "
                + HAPPENING_TAGS + " TEXT);";

        String CREATE_CIRCLE_TABLE = "CREATE TABLE " + TABLE_CIRCLE + "("
                + CIRCLE_ID + " INTEGER PRIMARY KEY, "
                + CIRCLE_LATITUDE + " DOUBLE NOT NULL, "
                + CIRCLE_LONGITUDE + " DOUBLE NOT NULL);";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CHANNEL_TABLE);
        db.execSQL(CREATE_HAPPENING_TABLE);
        db.execSQL(CREATE_CIRCLE_TABLE);
    }

    private void deleteTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAPPENING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CIRCLE);
    }

    public void addUser(User user) {
        if(user == null){
            SQLiteDatabase mdb = this.getWritableDatabase();
            mdb.execSQL("DELETE FROM " + TABLE_USER);
            mdb.close();
            return;
        }

        if(getUser() == null || !user.equals(getUser())) {
            SQLiteDatabase mdb = this.getWritableDatabase();
            mdb.execSQL("DELETE FROM " + TABLE_USER);

            this.onUpgrade(mdb, DATABASE_VERSION, DATABASE_VERSION + 1);
            ContentValues values = new ContentValues();
            values.put(DBHandler.USER_ID, user.getUserId());
            values.put(DBHandler.USERNAME, user.getUsername());
            values.put(DBHandler.PASSWORD, user.getPassword());
            values.put(DBHandler.USER_EMAIL, user.getEmail());
            values.put(DBHandler.AGE, user.getAge());
            values.put(DBHandler.CITIZEN, user.getCitizen());
            values.put(DBHandler.TAGS, String.valueOf(user.getTags()));
            mdb.insert(DBHandler.TABLE_USER, null, values);
            mdb.close();
        }
    }

    public User getUser() {
        SQLiteDatabase mdb = this.getReadableDatabase();

        Cursor cursor = mdb.query(TABLE_USER, new String[]{"*"}, null, null, null, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                User contact = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6));
                mdb.close();
                return contact;
            }
            else {
                mdb.close();
                return null;
            }
        }
        mdb.close();
        return null;
    }

    public void addChannels(ArrayList<Channel> channels) {

        SQLiteDatabase mdb = this.getWritableDatabase();
        mdb.execSQL("DELETE FROM " + TABLE_CHANNEL);

        if(channels == null) return;

        for(Channel channel : channels) {
            ContentValues values = new ContentValues();
            values.put(DBHandler.CHANNEL_ID, channel.getIdChannel());
            values.put(DBHandler.CHANNEL_NAME, channel.getName());
            values.put(DBHandler.CHANNEL_DESCRIPTION, channel.getDescription());
            values.put(DBHandler.CHANNEL_TAGS, channel.getTags());
            mdb.insert(DBHandler.TABLE_CHANNEL, null, values);
        }

        mdb.close();
    }

    public ArrayList<Channel> getChannels(){

        SQLiteDatabase mdb = this.getReadableDatabase();
        ArrayList<Channel> channelsToReturn = new ArrayList<>();

        Cursor cursor = mdb.query(TABLE_CHANNEL, new String[]{"*"}, null, null, null, null, null, null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Channel channel = new Channel(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                channelsToReturn.add(channel);
            }
            mdb.close();
            return channelsToReturn;
        }
        return null;
    }

    public void addHappenings(ArrayList<Happening> happenings) {

        SQLiteDatabase mdb = this.getWritableDatabase();
        mdb.execSQL("DELETE FROM " + TABLE_HAPPENING);

        if(happenings == null) return;

        for(Happening happening : happenings) {

            ContentValues values = new ContentValues();
            values.put(DBHandler.HAPPENING_ID, happening.getId());
            values.put(DBHandler.HAPPENING_NAME, happening.getName());
            values.put(DBHandler.HAPPENING_DESCRIPTION, happening.getDescription());
            values.put(DBHandler.HAPPENING_LATITUDE, happening.getLatitude());
            values.put(DBHandler.HAPPENING_LONGITUDE, happening.getLongitude());
            values.put(DBHandler.HAPPENING_TEMPORARY, happening.getTemporary());
            values.put(DBHandler.HAPPENING_TAGS, happening.getTags());
            mdb.insert(DBHandler.TABLE_HAPPENING, null, values);
        }

        mdb.close();
    }

    public ArrayList<Happening> getHappenings(){

        SQLiteDatabase mdb = this.getReadableDatabase();
        ArrayList<Happening> happeningsToReturn = new ArrayList<>();

        Cursor cursor = mdb.query(TABLE_HAPPENING, new String[]{"*"}, null, null, null, null, null, null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Happening happening = new Happening(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)), Double.parseDouble(cursor.getString(4)), Boolean.parseBoolean(cursor.getString(5)), cursor.getString(6));
                happeningsToReturn.add(happening);
            }
            mdb.close();
            return happeningsToReturn;
        }
        return null;
    }

    public void addCircle(LatLng center){
        SQLiteDatabase mdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHandler.CIRCLE_ID, nbCircles);
        values.put(DBHandler.CIRCLE_LATITUDE, center.latitude);
        values.put(DBHandler.CIRCLE_LONGITUDE, center.longitude);
        mdb.insert(DBHandler.TABLE_CIRCLE, null, values);
        mdb.close();
        nbCircles++;
    }

    public ArrayList<LatLng> getCircles(){

        SQLiteDatabase mdb = this.getReadableDatabase();
        ArrayList<LatLng> centersToReturn = new ArrayList<>();

        Cursor cursor = mdb.query(TABLE_CIRCLE, new String[]{"*"}, null, null, null, null, null, null);

        if(cursor != null) {
            nbCircles = 0;
            while(cursor.moveToNext()) {
                cursor.getString(0);
                LatLng center = new LatLng(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
                centersToReturn.add(center);
                nbCircles++;
            }
            mdb.close();
            return centersToReturn;
        }
        return null;
    }
}
