package com.deka.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GymNerdsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "GymNerds";
    private static final int DB_VERSION = 1;
    public GymNerdsDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       onFirstTimeCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Insert Exercise
    public void onFirstTimeCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE EXERCISE(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "NAME TEXT,"
                + "CATEGORY TEXT);");
        insertExercise(db,"Flat Bench Press","Chest");
        insertExercise(db,"Squats","Legs");
        insertExercise(db,"Incline Bench Press","Chest");
        db.execSQL("CREATE TABLE WORKOUT_DETAILS(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "EXERCISE TEXT,"
                + "WEIGHT INTEGER,"
                + "REPS INTEGER,"
                + "CURDATE DATE);");
    }

    private static void insertExercise(SQLiteDatabase db,String name,String category){
        ContentValues exercises = new ContentValues();
        exercises.put("NAME",name);
        exercises.put("CATEGORY",category);
        db.insert("EXERCISE",null,exercises);
    }
}
