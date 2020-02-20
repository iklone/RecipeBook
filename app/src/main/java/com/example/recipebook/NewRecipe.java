package com.example.recipebook;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewRecipe extends AppCompatActivity {

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        id = getIntent().getLongExtra("id", -1);

        if (id != -1) {
            loadRecipe();
        }
    }

    public void loadRecipe() {
        Log.d("G53MDP", "loading Recipe " + id);
        TextView txtName = findViewById(R.id.txtName);
        SeekBar seekRating = findViewById(R.id.seekRating);
        TextView txtIngredients = findViewById(R.id.txtIngredients);
        TextView txtInstructions = findViewById(R.id.txtInstructions);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDelete = findViewById(R.id.btnDelete);

        txtName.setEnabled(false);
        txtName.setHint("");
        txtName.setTextColor(Color.BLACK);
        txtIngredients.setEnabled(false);
        txtIngredients.setHint("");
        txtIngredients.setTextColor(Color.BLACK);
        txtInstructions.setEnabled(false);
        txtInstructions.setHint("");
        txtInstructions.setTextColor(Color.BLACK);
        btnSave.setText("Save Recipe");
        btnDelete.setVisibility(View.VISIBLE);

        String selection = "_ID = " + id;
        Cursor c = getContentResolver().query(MyProviderContract.R_URI, null, selection, null, null);
        c.moveToFirst();

        txtName.setText(c.getString(c.getColumnIndex(MyProviderContract.NAME)));
        seekRating.setProgress(Integer.parseInt(c.getString(c.getColumnIndex(MyProviderContract.RATING))) - 1);
        txtInstructions.setText(c.getString(c.getColumnIndex(MyProviderContract.INSTRUCTIONS)));

        String ingredientList = "";
        selection = "_id = ?";
        String[] a = {"" + id};
        c = getContentResolver().query(MyProviderContract.RI_URI, null, selection, a, null);
        if (c.moveToFirst()) {
            do {
                if (!c.isFirst()) {
                    ingredientList = ingredientList + "\n";
                }
                String name = c.getString(c.getColumnIndex("ingredients.name"));
                ingredientList = ingredientList + name;
            } while(c.moveToNext());
        }

        txtIngredients.setText(ingredientList);
    }

    //Save changes and return
    public void onBtnSaveClick(View view) {
        if (id == -1) { //create new recipe
            Cursor c;
            Log.d("G53MDP","New recipe");
            ContentValues newValues = new ContentValues();
            String rec = getName();
            newValues.put(MyProviderContract.NAME, rec);
            newValues.put(MyProviderContract.RATING, getRating());
            newValues.put(MyProviderContract.INSTRUCTIONS, getInstructions());

            getContentResolver().insert(MyProviderContract.R_URI, newValues);

            //find rec id
            String selection = "name = \"" + rec + "\"";
            c = getContentResolver().query(MyProviderContract.R_URI, null, selection, null, null);
            c.moveToFirst();
            id = c.getLong(c.getColumnIndex(MyProviderContract._ID));

            String ings = getIngredients();
            String ingredients[] = ings.split("\n", 0);

            int iid;
            for (String ing : ingredients) {
                Log.d("G53MDP", "linking ing: " + ing);

                selection = "name = \"" + ing + "\"";
                c = getContentResolver().query(MyProviderContract.I_URI, null, selection, null, null);

                if (c.getCount() == 0) { //if ing does not exist, insert it
                    newValues = new ContentValues();
                    newValues.put(MyProviderContract.NAME, ing);
                    getContentResolver().insert(MyProviderContract.I_URI, newValues);
                    Log.d("G53MDP", "inserting new ing: " + ing);
                }

                //find ing id
                c = getContentResolver().query(MyProviderContract.I_URI, null, selection, null, null);
                c.moveToFirst();
                iid = c.getInt(c.getColumnIndex(MyProviderContract._ID));

                //insert new r-i link
                newValues = new ContentValues();
                newValues.put(MyProviderContract.R_ID, id);
                newValues.put(MyProviderContract.I_ID, iid);
                getContentResolver().insert(MyProviderContract.RI_URI, newValues);
            }

        } else { //edit old recipe
            Log.d("G53MDP","Edit recipe rating");
            ContentValues newValues = new ContentValues();
            newValues.put(MyProviderContract.RATING, getRating());

            getContentResolver().update(MyProviderContract.R_URI, newValues, "_ID = " + id, null);
        }

        finish();
    }

    //Don't save changes and return
    public void onBtnCancelClick(View view) {
        finish();
    }

    //Delete current recipe and return
    public void onBtnDeleteClick(View view) {
        getContentResolver().delete(MyProviderContract.R_URI, "_ID = " + id, null);

        String ingredientList = "";
        String selection = "recipe_id = " + id;
        Cursor c = getContentResolver().query(MyProviderContract.RI_C_URI, null, selection, null, null);
        Cursor c2;

        getContentResolver().delete(MyProviderContract.RI_C_URI, "recipe_id = " + id, null);

        Log.d("G53MDP", "Deleting recipe with ingredient number: " + c.getCount());
        if (c.moveToFirst()) {
            do {
                String iid = c.getString(c.getColumnIndex(MyProviderContract.I_ID));

                Log.d("G53MDP", "Checking ing: " + iid);
                selection = "ingredient_id = \"" + iid + "\"";
                c2 = getContentResolver().query(MyProviderContract.RI_C_URI, null, selection, null, null);

                if (c2.getCount() == 0) { //if no more ri entries for givin ing, delete
                    getContentResolver().delete(MyProviderContract.I_URI, "_id = " + iid, null);
                    Log.d("G53MDP", "Deleting ing: " + iid);
                }


            } while(c.moveToNext());
        }

        finish();
    }

    public String getName() {
        TextView txtView = findViewById(R.id.txtName);
        return txtView.getText().toString();
    }

    public String getRating() {
        SeekBar seekView = findViewById(R.id.seekRating);
        return String.valueOf(seekView.getProgress() + 1);
    }

    public String getIngredients() {
        TextView txtView = findViewById(R.id.txtIngredients);
        return txtView.getText().toString();
    }

    public String getInstructions() {
        TextView txtView = findViewById(R.id.txtInstructions);
        return txtView.getText().toString();
    }
}
