package com.example.goon.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Daniela on 29/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBHandler";

    private static final String TABLE_CHANEL = "chanelTable";
    private static final String TABLE_ACTIVITY = "avtivityTable";
    //chanelTable columns
    private static final String CHANEL_ID = "userID";
    private static final String CHANEL_NAME = "chanelName";
    private static final String CHANEL_DESCRIPTION = "chanelDescription";
    private static final String CHANEL_TAGS = "chanelTags";

    public static final String TABLE_USER = "userTable";
    //userTable column
    public static final String USER_ID = "userID";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_EMAIL = "userEmail";
    public static final String AGE = "age";
    public static final String CITIZEN = "citizen";
    public static final String TAGS = "tags";


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
                +USER_ID + " INTEGER PRIMARY KEY, "
                +USERNAME + " TEXT NOT NULL, "
                +PASSWORD + " TEXT NOT NULL, "
                + USER_EMAIL + " TEXT NOT NULL, "
                + AGE + " INTEGER, "
                + CITIZEN + " TEXT NOT NULL, "
                + TAGS + " TEXT" + ");";

        db.execSQL(CREATE_USER_TABLE);
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
         if (cursor != null ) {
             sum=cursor.getCount();
         }
         cursor.close();

         if(sum==0){
             empty = true;
         }else{
             empty = false;
         }
         return empty;


     }
}
