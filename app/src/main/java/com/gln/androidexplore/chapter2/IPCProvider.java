package com.gln.androidexplore.chapter2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.gln.androidexplore.chapter2.db.IPCDbManager;

/**
 * Created by guolina on 2017/6/26.
 */
public class IPCProvider extends ContentProvider {

    public static final String AUTHORITY = "com.gln.androidexplore.chapter2";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private IPCDbManager mDbManager;

    static {
        sUriMatcher.addURI(AUTHORITY, "book", 0);
    }

    @Override
    public boolean onCreate() {
        mDbManager = IPCDbManager.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mDbManager.query(projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mDbManager.insert(values);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mDbManager.delete(selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDbManager.update(values, selection, selectionArgs);
    }
}
