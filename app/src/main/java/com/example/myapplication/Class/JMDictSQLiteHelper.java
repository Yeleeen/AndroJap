package com.example.myapplication.Class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JMDictSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jmdict.db";
    private static final int DATABASE_VERSION = 1;

    public JMDictSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE jmdict (kanji TEXT PRIMARY KEY, definition TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS jmdict");
        onCreate(db);
    }
}