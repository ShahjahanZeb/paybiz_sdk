package com.example.paybizsdk.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseService extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "authLog.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "paymentId";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CURRENCY = "currency";
    private static final String COLUMN_CCNAME = "cardHolderName";
    private static final String COLUMN_ACSURL = "acsURL";

    public DatabaseService(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AMOUNT + " TEXT, " +
                COLUMN_CURRENCY + " TEXT, " +
                COLUMN_CCNAME + " TEXT, " +
                COLUMN_ACSURL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTransaction(String name, String amount, String currency, String ccName, String acsURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_CURRENCY, currency);
        contentValues.put(COLUMN_CCNAME, ccName);
        contentValues.put(COLUMN_ACSURL, acsURL);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getLastTransaction() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        return db.rawQuery(query, null);
    }

    public void deleteLastTransaction() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = (SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME + ")";
        db.execSQL(query);
    }

}
