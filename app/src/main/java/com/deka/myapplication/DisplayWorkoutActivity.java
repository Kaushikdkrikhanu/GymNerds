package com.deka.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayWorkoutActivity extends AppCompatActivity {
    public static final String DATE = "selectedDate";
    Cursor workoutCursor;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout);

        updateRecycler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateRecycler();
    }

    private void updateRecycler(){
        ArrayList<String> exercises = new ArrayList<>();
        ArrayList<String> info = new ArrayList<>();
        int[] weights = new int[100];
        int[] reps = new int[100];


        RecyclerView displayWorkoutRecycler = findViewById(R.id.displayWorkoutFragmentRecyclerView);

        SQLiteOpenHelper gymNerdsDatabaseHelper = new GymNerdsDatabaseHelper(this);
        try{
            SQLiteDatabase db = gymNerdsDatabaseHelper.getReadableDatabase();
            workoutCursor = db.rawQuery("SELECT _id, WEIGHT, REPS, EXERCISE, CURDATE FROM " +
                            "WORKOUT_DETAILS WHERE CURDATE = ?"+"ORDER BY EXERCISE ASC",
                    new String[]{getIntent().getExtras().getString("selectedDate")});
            int i = 0;
            int j = 0;
            String exercise = " ";
            while (workoutCursor.moveToNext()){
                if(exercise.equals(workoutCursor.getString(3))){
                    weights[i] = workoutCursor.getInt(1);
                    reps[i] = workoutCursor.getInt(2);
                    i++;
                }
                else {
                    if(i>0){
                        for(int k = 1;k < i;k++){
                            info.set(j,info.get(j).concat(weights[k] + "               " + reps[k] + "\n"));

                        }
                        j++;
                    }
                    exercise = workoutCursor.getString(3);
                    exercises.add(exercise);
                    weights[0] = workoutCursor.getInt(1);
                    reps[0] = workoutCursor.getInt(2);
                    info.add(weights[0] + "               " + reps[0] + "\n");

                    i=1;
                }

            }
            for(int k = 1;k < i;k++){
                info.set(j,info.get(j).concat(weights[k] + "               " + reps[k] + "\n"));
            }
            DisplayWorkoutAdapter adapter = new DisplayWorkoutAdapter(exercises,info);
            displayWorkoutRecycler.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            displayWorkoutRecycler.setLayoutManager(layoutManager);

        } catch (SQLiteException e) {

            Toast.makeText(this,getString(R.string.SQLiteException),Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        workoutCursor.close();
    }


}