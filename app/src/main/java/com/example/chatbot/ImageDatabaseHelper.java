package com.example.chatbot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "image.db";
    private static final int DATABASE_VERSION = 1;

    public ImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_IMAGES_TABLE = "CREATE TABLE " +
                ImageContract.ImageEntry.TABLE_NAME + " (" +
                ImageContract.ImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ImageContract.ImageEntry.COLUMN_IMAGE_URI + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ImageContract.ImageEntry.TABLE_NAME);
        onCreate(db);
    }
}

