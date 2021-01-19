package com.deka.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class AddExerciseActivity extends AppCompatActivity {

    public static final String EXERCISE_NAME = "exercise";
    public static final String DATE_USED = "date";
    Cursor workoutCursor;
    public static ArrayList<Integer> id;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        EditText editWeightText = findViewById(R.id.edit_weight);
        EditText editSetsText = findViewById(R.id.edit_reps);
        editWeightText.setText(String.format(Locale.getDefault(),"%d",0));
        editSetsText.setText(String.format(Locale.getDefault(),"%d",0));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("exercise"));
        //Setting up the Recycler View
        RecyclerView workoutRecycler = findViewById(R.id.workout_recycler);
        updateRecycleView(workoutRecycler);




    }

    public void onClickDecrementWeight(View view) {
        EditText editWeightText = findViewById(R.id.edit_weight);
        int weight = Integer.parseInt(String.valueOf(editWeightText.getText()));
        if(weight>0){
            weight = weight - 5;
        }
        editWeightText.setText(String.format(Locale.getDefault(),"%d",weight));
    }

    public void onClickDecrementReps(View view) {
        EditText editSetsText = findViewById(R.id.edit_reps);
        int sets = Integer.parseInt(String.valueOf(editSetsText.getText()));
        if(sets>0){
            sets--;
        }
        editSetsText.setText(String.format(Locale.getDefault(),"%d",sets));
    }

    public void onClickIncrementReps(View view) {
        EditText editSetsText = findViewById(R.id.edit_reps);
        int sets = Integer.parseInt(String.valueOf(editSetsText.getText()));
        sets++;
        editSetsText.setText(String.format(Locale.getDefault(),"%d",sets));
    }

    public void onClickIncrementWeights(View view) {
        EditText editWeightText = findViewById(R.id.edit_weight);
        int weight = Integer.parseInt(String.valueOf(editWeightText.getText()));
        weight = weight + 5;
        editWeightText.setText(String.format(Locale.getDefault(),"%d",weight));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickAdd(View view) {
        SQLiteOpenHelper gymNerdDatabaseHelper = new GymNerdsDatabaseHelper(this);
        try{
            SQLiteDatabase db = gymNerdDatabaseHelper.getWritableDatabase();
            EditText editWeightText = findViewById(R.id.edit_weight);
            EditText editRepsText = findViewById(R.id.edit_reps);
            int weight = Integer.parseInt(String.valueOf(editWeightText.getText()));
            int reps = Integer.parseInt(String.valueOf(editRepsText.getText()));
            String exercise = Objects.requireNonNull(getIntent().getExtras()).getString(EXERCISE_NAME);
            if(!(weight==0||reps==0)){
                insertWorkout(db,exercise,weight,reps);
                RecyclerView workoutRecycler = findViewById(R.id.workout_recycler);
                updateRecycleView(workoutRecycler);
            }


        } catch (SQLiteException e) {
            Toast.makeText(this,getString(R.string.SQLiteException), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void insertWorkout(SQLiteDatabase db, String exercise, int weight, int reps){
        ContentValues workout = new ContentValues();
        workout.put("EXERCISE",exercise);
        workout.put("WEIGHT",weight);
        workout.put("REPS",reps);
        workout.put("CURDATE",getIntent().getExtras().getString("date") );
        db.insert("WORKOUT_DETAILS",null,workout);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateRecycleView(RecyclerView workoutRecycler){
        ArrayList<Integer> weights = new ArrayList<>();
        ArrayList<Integer> reps = new ArrayList<>();
        id = new ArrayList<>();
        String exercise = Objects.requireNonNull(getIntent().getExtras()).getString(EXERCISE_NAME);
        SQLiteOpenHelper gymNerdDatabaseHelper = new GymNerdsDatabaseHelper(this);
        try {
            SQLiteDatabase db = gymNerdDatabaseHelper.getReadableDatabase();
            workoutCursor = db.rawQuery("SELECT _id, WEIGHT, REPS FROM WORKOUT_DETAILS WHERE EXERCISE = ? " +
                            "AND CURDATE = ?",
                     new String[]{exercise, getIntent().getExtras().getString("date")});
            while(workoutCursor.moveToNext()){
                id.add(workoutCursor.getInt(0));
                weights.add(workoutCursor.getInt(1));
                reps.add(workoutCursor.getInt(2));
            }
            SetsAdapter adapter = new SetsAdapter(weights,reps);
            workoutRecycler.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            workoutRecycler.setLayoutManager(layoutManager);



        } catch (SQLiteException e) {
            Toast.makeText(this,getString(R.string.SQLiteException), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        workoutCursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickUpdate(View view) {
       try{
           int iD = id.get(SetsAdapter.pos);
           EditText editSetsText = findViewById(R.id.edit_reps);
           int sets = Integer.parseInt(String.valueOf(editSetsText.getText()));
           EditText editWeightText = findViewById(R.id.edit_weight);
           int weight = Integer.parseInt(String.valueOf(editWeightText.getText()));


           GymNerdsDatabaseHelper gymNerdsDatabaseHelper = new GymNerdsDatabaseHelper(this);
           try {

               SQLiteDatabase db = gymNerdsDatabaseHelper.getWritableDatabase();
               workoutCursor = db.rawQuery("UPDATE WORKOUT_DETAILS SET WEIGHT = ?," +
                       "REPS = ? WHERE _id = ?",new String[]{weight+"",sets+"",iD+""});
               workoutCursor.moveToFirst();
               RecyclerView workoutRecycler = findViewById(R.id.workout_recycler);
               updateRecycleView(workoutRecycler);
           }
           catch (SQLiteException e){
               Toast.makeText(this, R.string.SQLiteException+"", Toast.LENGTH_SHORT).show();
           }
       }
       catch (IndexOutOfBoundsException e){
           Toast.makeText(this,getString(R.string.notSelected), Toast.LENGTH_SHORT).show();
       }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickDelete(View view) {
        try {
            int iD = id.get(SetsAdapter.pos);
            GymNerdsDatabaseHelper gymNerdsDatabaseHelper = new GymNerdsDatabaseHelper(this);
            try {

                SQLiteDatabase db = gymNerdsDatabaseHelper.getWritableDatabase();
                workoutCursor = db.rawQuery("DELETE FROM WORKOUT_DETAILS " +
                        "WHERE _id = ?",new String[]{iD+""});
                workoutCursor.moveToFirst();
                RecyclerView workoutRecycler = findViewById(R.id.workout_recycler);
                updateRecycleView(workoutRecycler);
            }
            catch (SQLiteException e){
                Toast.makeText(this, R.string.SQLiteException+"", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IndexOutOfBoundsException e){
            Toast.makeText(this,getString(R.string.notSelected), Toast.LENGTH_SHORT).show();
        }
    }
}