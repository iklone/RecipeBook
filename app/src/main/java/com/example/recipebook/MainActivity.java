package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private boolean sortByTitle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = findViewById(R.id.lstRecipes);
        populateList(list, "name");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ListView list = findViewById(R.id.lstRecipes);
        Button btn = findViewById(R.id.btnSort);

        sortByTitle = true;
        btn.setText("Sort Recipes by Rating");
        populateList(list, "name");
    }

    public void populateList(final ListView list, String order) {
        Cursor c = getContentResolver().query(MyProviderContract.R_URI, null, null, null, order);

        String[] display = new String[] {
                MyProviderContract.NAME,
                MyProviderContract.RATING,
        };
        int[] to = new int[] {
                R.id.txtItem,
                R.id.txtRating,
        };
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                this, R.layout.db_item,
                c,
                display,
                to,
                0);

        list.setAdapter(dataAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long id) {
                Intent intent = new Intent(MainActivity.this, NewRecipe.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    public void onBtnNewClick(View view) {
        Intent intent = new Intent(MainActivity.this, NewRecipe.class);
        startActivity(intent);
    }

    public void onBtnIngredientsClick(View view) {
        Intent intent = new Intent(MainActivity.this, Ingredients.class);
        startActivity(intent);
    }

    public void onBtnSortClick(View view) {
        ListView list = findViewById(R.id.lstRecipes);
        Button btn = findViewById(R.id.btnSort);

        if (sortByTitle) {
            sortByTitle = false;
            populateList(list, "rating");
            btn.setText("Sort Recipes by Title");
            Log.d("G53MDP", "Sorting by rating.");
        } else {
            sortByTitle = true;
            populateList(list, "name");
            btn.setText("Sort Recipes by Rating");
            Log.d("G53MDP", "Sorting by title.");
        }
    }
}
