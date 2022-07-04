package com.practic.phonebook;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;


class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "contacts.db";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "profile"; // название таблицы в бд
    // названия столбцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_FAMILY = "family";
    static final String COLUMN_PATRONYMIC = "patronymic";
    static final String COLUMN_PHONE = "phone";
    static final String COLUMN_AVATAR = "avatar";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_FAMILY + " TEXT, " + COLUMN_PATRONYMIC
                + " TEXT, " + COLUMN_PHONE + " TEXT, " + COLUMN_AVATAR + " BLOB  );");
        // добавление начальных данных
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}