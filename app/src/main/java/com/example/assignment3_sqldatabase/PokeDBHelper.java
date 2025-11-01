package com.example.assignment3_sqldatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PokeDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pokemon_tracker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "pokemon";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NATIONAL_NUMBER = "national_number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SPECIES = "species";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_HP = "hp";
    public static final String COLUMN_ATTACK = "attack";
    public static final String COLUMN_DEFENSE = "defense";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NATIONAL_NUMBER + " INTEGER," +
            COLUMN_NAME + " TEXT," +
            COLUMN_SPECIES + " TEXT," +
            COLUMN_GENDER + " TEXT," +
            COLUMN_HEIGHT + " REAL," +
            COLUMN_WEIGHT + " REAL," +
            COLUMN_LEVEL + " INTEGER," +
            COLUMN_HP + " INTEGER," +
            COLUMN_ATTACK + " INTEGER," +
            COLUMN_DEFENSE + " INTEGER" +
            ");";

    public PokeDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
