package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Ingredients extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);


        Log.d("G53MDP", "Displaying all ingredients.");

        ListView list = findViewById(R.id.lstIngredients);
        populateList(list, "name");
    }

    public void populateList(final ListView list, String order) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Cursor c = getContentResolver().query(MyProviderContract.I_URI, null, null, null, order);

        String[] display = new String[] {
                MyProviderContract.NAME,
        };
        int[] to = new int[] {
                R.id.txtItem,
        };
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                this, R.layout.ing_item,
                c,
                display,
                to,
                0);

        list.setAdapter(dataAdapter);
    }

    public void onBtnNewClick(View view) { finish(); }
}
