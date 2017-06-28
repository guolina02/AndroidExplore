package com.gln.androidexplore.chapter2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gln.androidexplore.MyApplication;

/**
 * Created by guolina on 2017/6/26.
 */
public class IPCDbManager {

    private static final String DB_NAME = "ipc_provider.db";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE_BOOK = "book";

    public static final String PARAM_BOOK_TITLE = "title";
    public static final String PARAM_BOOK_PRICE = "price";
    public static final String PARAM_BOOK_WRITER = "writer";
    public static final String PARAM_BOOK_PUBLISHER = "publisher";

    private static IPCDbManager sInstance;
    private SQLiteDatabase mDb;

    private IPCDbManager(Context context) {
        if (mDb == null) {
            mDb = new MyDbHelper(context, DB_NAME, null, DB_VERSION).getWritableDatabase();
        }
    }

    public static IPCDbManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (IPCDbManager.class) {
                if (sInstance == null) {
                    sInstance = new IPCDbManager(context);
                }
            }
        }
        return sInstance;
    }

    public void insert(ContentValues values) {
        if (values == null) {
            return;
        }
        mDb.insert(DB_TABLE_BOOK, null, values);
    }

    public int update(ContentValues values, String selection, String[] selectionArgs) {
        if (values == null) {
            return 0;
        }

        return mDb.update(DB_TABLE_BOOK, values, selection, selectionArgs);
    }

    public int delete(String selection, String[] selectionArgs) {
        return mDb.delete(DB_TABLE_BOOK, selection, selectionArgs);
    }

    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = mDb.query(DB_TABLE_BOOK, projection, selection, selectionArgs, null, null, sortOrder, null);
        return cursor;
    }

    private class MyDbHelper extends SQLiteOpenHelper {

        public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_BOOK
                    + "(_id INTEGER PRIMARY KEY,"
                    + PARAM_BOOK_TITLE + " TEXT,"
                    + PARAM_BOOK_PRICE + " FLOAT,"
                    + PARAM_BOOK_WRITER + " TEXT,"
                    + PARAM_BOOK_PUBLISHER + " TEXT)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "DROP TABLE " + DB_TABLE_BOOK;
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
