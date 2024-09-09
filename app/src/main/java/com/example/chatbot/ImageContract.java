package com.example.chatbot;

import android.net.Uri;
import android.provider.BaseColumns;

public class ImageContract {

    public static final String CONTENT_AUTHORITY = "com.example.imageupload.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_IMAGES = "images";

    public static class ImageEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMAGES).build();

        public static final String TABLE_NAME = "images";
        public static final String COLUMN_IMAGE_URI = "image_uri";
    }
}
