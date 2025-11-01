package com.example.assignment3_sqldatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class PokeContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.assignment3_sqldatabase.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PokeDBHelper.TABLE_NAME);

    private static final int POKEMON = 1;
    private static final int POKEMON_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, PokeDBHelper.TABLE_NAME, POKEMON);
        uriMatcher.addURI(AUTHORITY, PokeDBHelper.TABLE_NAME + "/#", POKEMON_ID);
    }

    private PokeDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new PokeDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case POKEMON:
                cursor = db.query(PokeDBHelper.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case POKEMON_ID:
                String id = uri.getLastPathSegment();
                cursor = db.query(PokeDBHelper.TABLE_NAME, projection,
                        PokeDBHelper.COLUMN_ID + "=?",
                        new String[]{id}, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        if (uriMatcher.match(uri) != POKEMON) {
            throw new IllegalArgumentException("Invalid URI for insert: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = PokeDBHelper.COLUMN_NATIONAL_NUMBER + "=? AND " +
                PokeDBHelper.COLUMN_NAME + "=? AND " +
                PokeDBHelper.COLUMN_SPECIES + "=? AND " +
                PokeDBHelper.COLUMN_LEVEL + "=?";
        assert values != null;
        String[] args = new String[]{
                values.getAsString(PokeDBHelper.COLUMN_NATIONAL_NUMBER),
                values.getAsString(PokeDBHelper.COLUMN_NAME),
                values.getAsString(PokeDBHelper.COLUMN_SPECIES),
                values.getAsString(PokeDBHelper.COLUMN_LEVEL)
        };
        Cursor cursor = db.query(PokeDBHelper.TABLE_NAME, null, selection, args, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return null;
        }
        cursor.close();

        long id = db.insert(PokeDBHelper.TABLE_NAME, null, values);
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case POKEMON:
                count = db.delete(PokeDBHelper.TABLE_NAME, selection, selectionArgs);
                break;

            case POKEMON_ID:
                String id = uri.getLastPathSegment();
                count = db.delete(PokeDBHelper.TABLE_NAME,
                        PokeDBHelper.COLUMN_ID + "=?",
                        new String[]{id});
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (count > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
