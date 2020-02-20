package com.example.recipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "recipeDB", null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recipes (\n" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "name VARCHAR(128) NOT NULL,\n" +
                "instructions VARCHAR(128) NOT NULL,\n" +
                "rating INTEGER);");

        db.execSQL("CREATE TABLE ingredients (\n" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "name VARCHAR(128) NOT NULL);");

        db.execSQL("CREATE TABLE recipe_ingredients (\n" +
                "recipe_id INT NOT NULL,\n" +
                "ingredient_id INT NOT NULL,\n" +
                "CONSTRAINT fk1 FOREIGN KEY (recipe_id) REFERENCES recipes (_id),\n" +
                "CONSTRAINT fk2 FOREIGN KEY (ingredient_id) REFERENCES ingredients (_id),\n" +
                "CONSTRAINT _id PRIMARY KEY (recipe_id, ingredient_id) );");


        db.execSQL("INSERT INTO recipes (name, instructions, rating)" +
                "VALUES" +
                "('Milky Cheese', 'Mix milk with cheese.', 1)," +
                "('Cheesy Milk', 'Mix cheese with milk.', 5)," +
                "('Spicy cheese', 'Put spice on cheese.', 2);");

        db.execSQL("INSERT INTO ingredients (name)" +
                "VALUES" +
                "('Milk')," +
                "('Cheese')," +
                "('Spice');");

        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id)" +
                "VALUES" +
                "(1, 1)," +
                "(1, 2)," +
                "(2, 2)," +
                "(2, 1)," +
                "(3, 2)," +
                "(3, 3);");

        Log.d("G53MDP", "Finished db initialisation");
    }
}
