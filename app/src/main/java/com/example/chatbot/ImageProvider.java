package com.example.chatbot;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ImageProvider extends ContentProvider {

    private static final int IMAGES = 100;
    private static final int IMAGE_ID = 101;

    private ImageDatabaseHelper dbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ImageContract.CONTENT_AUTHORITY, ImageContract.PATH_IMAGES, IMAGES);
        uriMatcher.addURI(ImageContract.CONTENT_AUTHORITY, ImageContract.PATH_IMAGES + "/#", IMAGE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ImageDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case IMAGES:
                cursor = db.query(ImageContract.ImageEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case IMAGE_ID:
                selection = ImageContract.ImageEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ImageContract.ImageEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case IMAGES:
                long id = db.insert(ImageContract.ImageEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case IMAGES:
                rowsDeleted = db.delete(ImageContract.ImageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case IMAGE_ID:
                selection = ImageContract.ImageEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ImageContract.ImageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}