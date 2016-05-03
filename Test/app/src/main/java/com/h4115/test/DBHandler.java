package com.h4115.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBHandler";

    private static final String TABLE_CHANEL = "chanelTable";
    private static final String TABLE_ACTIVITY = "activityTable";
    private static final String TABLE_USER = "userTable";

    private static final String CHANEL_ID = "userID";
    private static final String CHANEL_NAME = "chanelName";
    private static final String CHANEL_DESCRIPTION = "chanelDescription";
    private static final String CHANEL_TAGS = "chanelTags";

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

    public void addUser(User user) {

        System.out.println(user.getTags());

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

    public int userSubscriptions(String tags) {
        SQLiteDatabase mdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        User user = getUser();
        values.put(TAGS, tags);
        return mdb.update(TABLE_USER, values, USER_ID + " = ?", new String[]{String.valueOf(user.getUserId())});
    }
}
