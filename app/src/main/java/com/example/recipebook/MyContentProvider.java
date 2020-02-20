package com.example.recipebook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class MyContentProvider extends ContentProvider {

    private DBHelper dbHelper = null;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "recipes", 1);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "recipes/#", 2);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "ingredients", 3);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "ingredients/#", 4);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "recipe_ingredients", 5);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "recipe_count_ingredients", 6);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "*", 7);
    }

    public MyContentProvider() {
        DBHelper dbHelper;
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DBHelper(this.getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        String contentType;
        if (uri.getLastPathSegment() == null) {
            contentType = MyProviderContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = MyProviderContract.CONTENT_TYPE_SINGLE;
        }
        return contentType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        switch(uriMatcher.match(uri)) {
            case 1:
                tableName = "recipes";
                break;
            case 3:
                tableName = "ingredients";
                break;
            default:
                tableName = "recipe_ingredients";
                break;
        }

        long id = db.insert(tableName, null, values);
        db.close();
        Uri xUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(xUri, null);

        return xUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch(uriMatcher.match(uri)) {
            case 1:
                return db.query("recipes", projection, selection, selectionArgs, null, null, sortOrder);
            case 2:
                selection = "_ID = " + uri.getLastPathSegment();
                return db.query("recipes", projection, selection, selectionArgs, null, null, sortOrder);
            case 3:
                return db.query("ingredients", projection, selection, selectionArgs, null, null, sortOrder);
            case 4:
                selection = "_ID = " + uri.getLastPathSegment();
                return db.query("ingredients", projection, selection, selectionArgs, null, null, sortOrder);
            case 5:
                String qRI = "SELECT r._id as recipe_id, r.name, ri.ingredient_id, i.name FROM recipes r JOIN recipe_ingredients ri ON (r._id = ri.recipe_id) JOIN ingredients i ON (ri.ingredient_id = i._id) WHERE r._id = ?";
                return db.rawQuery(qRI, selectionArgs);
            case 6:
                //selection = "_ID = " + uri.getLastPathSegment();
                return db.query("recipe_ingredients", projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        switch(uriMatcher.match(uri)) {
            case 1:
                tableName = "recipes";
                break;
            case 3:
                tableName = "ingredients";
                break;
            default:
                tableName = "recipe_ingredients";
                break;
        }

        db.delete(tableName, selection, selectionArgs);
        db.close();

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        db.update("recipes", values, selection, selectionArgs);
        db.close();

        return 0;
    }
}