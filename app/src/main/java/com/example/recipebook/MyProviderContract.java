package com.example.recipebook;

import android.net.Uri;

public class MyProviderContract {
    public static final String AUTHORITY = "com.example.recipebook.MyContentProvider";

    public static final Uri R_URI = Uri.parse("content://"+AUTHORITY+"/recipes");
    public static final Uri I_URI = Uri.parse("content://"+AUTHORITY+"/ingredients");
    public static final Uri RI_URI = Uri.parse("content://"+AUTHORITY+"/recipe_ingredients");
    public static final Uri RI_C_URI = Uri.parse("content://"+AUTHORITY+"/recipe_count_ingredients");
    public static final Uri ALL_URI = Uri.parse("content://"+AUTHORITY+"/");

    public static final String _ID = "_id";

    public static final String NAME = "name";
    public static final String INGNAME = "ingname";
    public static final String RATING = "rating";
    public static final String INSTRUCTIONS = "instructions";

    public static final String R_ID = "recipe_id";
    public static final String I_ID = "ingredient_id";

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/MyContentProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/MyContentProvider.data.text";

}
