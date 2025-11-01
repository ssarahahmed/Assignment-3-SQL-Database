package com.example.assignment3_sqldatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DatabaseListActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);

        listView = findViewById(R.id.listview);

        displayData();

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Uri uri = Uri.withAppendedPath(PokeContentProvider.CONTENT_URI, String.valueOf(id));
            int deleted = getContentResolver().delete(uri, null, null);
            if (deleted > 0) {
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                displayData();
            }
            return true;
        });
    }

    private void displayData() {
        Cursor cursor = getContentResolver().query(
                PokeContentProvider.CONTENT_URI,
                null, null, null, null
        );

        if (adapter == null) {
            adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.row,
                    cursor,
                    new String[]{
                            PokeDBHelper.COLUMN_NATIONAL_NUMBER,
                            PokeDBHelper.COLUMN_NAME,
                            PokeDBHelper.COLUMN_SPECIES,
                            PokeDBHelper.COLUMN_LEVEL
                    },
                    new int[]{
                            R.id.tvNationalNumber,
                            R.id.tvName,
                            R.id.tvSpecies,
                            R.id.tvLevel
                    },
                    0
            );
            listView.setAdapter(adapter);
        } else {
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
        }
    }
    private void insertPokemon(int nationalNumber, String name, String species, int level) {
        ContentValues values = new ContentValues();
        values.put(PokeDBHelper.COLUMN_NATIONAL_NUMBER, nationalNumber);
        values.put(PokeDBHelper.COLUMN_NAME, name);
        values.put(PokeDBHelper.COLUMN_SPECIES, species);
        values.put(PokeDBHelper.COLUMN_LEVEL, level);

        Uri result = getContentResolver().insert(PokeContentProvider.CONTENT_URI, values);
        if (result != null) {
            Toast.makeText(this, "Inserted successfully", Toast.LENGTH_SHORT).show();
            displayData();
        } else {
            Toast.makeText(this, "Duplicate entry! Not added.", Toast.LENGTH_SHORT).show();
        }
    }
}
