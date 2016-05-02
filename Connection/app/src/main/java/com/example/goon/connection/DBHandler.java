package com.example.goon.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniela on 29/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBHandler";


    private static final String TABLE_ACTIVITY = "avtivityTable";


    public static final String TABLE_USER = "userTable";
    //userTable column
    public static final String USER_ID = "userID";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_EMAIL = "userEmail";
    public static final String AGE = "age";
    public static final String CITIZEN = "citizen";
    public static final String TAGS = "tags";

    private static final String TABLE_CHANNEL = "chanelTable";
    public static final String CHANNEL_ID = "channelID";
    public static final String CHANNEL_NAME = "channelName";
    public static final String CHANNEL_DESCRIPTION = "channelDescription";
    private static final String CHANEL_TAGS = "chanelTags";



    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        createTableUser(db);
        createTableChannel(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        createTableUser(db);
        createTableChannel(db);
    }
    private void createTableUser(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_ID + " INTEGER PRIMARY KEY, "
                + USERNAME + " TEXT NOT NULL, "
                + PASSWORD + " TEXT NOT NULL, "
                + USER_EMAIL + " TEXT NOT NULL, "
                + AGE + " INTEGER, "
                + CITIZEN + " TEXT NOT NULL, "
                + TAGS + " TEXT" + ");";

        db.execSQL(CREATE_USER_TABLE);
    }
    private void createTableChannel(SQLiteDatabase db){
        String CREATE_CHANNEL_TABLE= "CREATE TABLE "+TABLE_CHANNEL + "("
                + CHANNEL_ID +" INTEGER PRIMARY KEY, "
                + CHANNEL_NAME + " TEXT NOT NULL, "
                + CHANNEL_DESCRIPTION+ " TEXT" +");";
        db.execSQL(CREATE_CHANNEL_TABLE);
    }
    private void deleteTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

    }



    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        this.onUpgrade(mdb,DATABASE_VERSION,DATABASE_VERSION+1 );
        ContentValues values = new ContentValues();
        values.put(DBHandler.USER_ID, user.getUserId());
        values.put(DBHandler.USERNAME, user.getUsername());
        values.put(DBHandler.PASSWORD, user.getPassword());
        values.put(DBHandler.USER_EMAIL, user.getEmail());
        values.put(DBHandler.AGE, user.getAge());
        values.put(DBHandler.CITIZEN, user.getCitizen());
        values.put(DBHandler.TAGS, String.valueOf(user.getTags()));
// Inserting Row
        mdb.insert(DBHandler.TABLE_USER,null,values);

    }

    public void addChannel(Channel channel) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHandler.CHANNEL_ID, channel.getIdChannel());
        values.put(DBHandler.CHANNEL_NAME, channel.getName());
        values.put(DBHandler.CHANNEL_DESCRIPTION, channel.getDescription());

// Inserting Row
        mdb.insert(DBHandler.TABLE_CHANNEL,null,values);

    }


    public User getUser(int id) {
        SQLiteDatabase mdb = this.getReadableDatabase();
        Cursor cursor = mdb.query(TABLE_USER, new String[]{USER_ID,
                        USERNAME, PASSWORD, USER_EMAIL, AGE, CITIZEN, TAGS}, USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6));
// return user
        return contact;
    }

    public Channel getChannel(int id) {
        SQLiteDatabase mdb = this.getReadableDatabase();
        Cursor cursor = mdb.query(TABLE_CHANNEL, new String[]{CHANNEL_ID,
                        CHANNEL_NAME, CHANNEL_DESCRIPTION}, CHANNEL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Channel channel = new Channel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
// return Channel
        return channel;
    }

    // Updating an user
    public int updateUser(User user) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, user.getUsername());
        values.put(PASSWORD, user.getPassword());
        values.put(USER_EMAIL, user.getEmail());
        values.put(AGE, user.getAge());
        values.put(CITIZEN, user.getCitizen());

// updating row
        return mdb.update(TABLE_USER, values, USER_ID + "= ?",
                new String[]{String.valueOf(user.getUserId())});
    }

    // Updating an user
    public int updateTags(Integer id) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        User user = getUser(id);
        values.put(TAGS, String.valueOf(user.getTags()));
// updating row
        return mdb.update(TABLE_USER, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }

    // Deleting an user
    public void deleteUser(User user) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        mdb.delete(TABLE_USER, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        mdb.close();
    }

     public Boolean countUsers() {
         Boolean empty;
         int sum=0;

         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+TABLE_USER, null);
         if ((cursor.getCount())>sum ) {
             empty = false;
         }else{
             empty = true;
         }

         cursor.close();


         return empty;


     }
}
