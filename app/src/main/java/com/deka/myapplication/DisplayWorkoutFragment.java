package com.deka.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class DisplayWorkoutFragment extends Fragment {

    Cursor workoutCursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View layout  =  inflater.inflate(R.layout.fragment_display_workout,container,false);
        updateRecycler(layout);
        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateRecycler(View view){
        ArrayList<String> exercises = new ArrayList<>();
        ArrayList<String> info = new ArrayList<>();
        int[] weights = new int[100];
        int[] reps = new int[100];


        RecyclerView displayWorkoutRecycler = view.findViewById(R.id.displayWorkoutFragmentRecyclerView);

        SQLiteOpenHelper gymNerdsDatabaseHelper = new GymNerdsDatabaseHelper(view.getContext());
        try{
            SQLiteDatabase db = gymNerdsDatabaseHelper.getReadableDatabase();
            workoutCursor = db.rawQuery("SELECT _id, WEIGHT, REPS, EXERCISE, CURDATE FROM " +
                            "WORKOUT_DETAILS WHERE CURDATE = ?"+"ORDER BY EXERCISE ASC",
                    new String[]{String.valueOf(java.time.LocalDate.now())});
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
            DisplayWorkoutFragmentAdapter adapter = new DisplayWorkoutFragmentAdapter(exercises,info);
            displayWorkoutRecycler.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            displayWorkoutRecycler.setLayoutManager(layoutManager);

        } catch (SQLiteException e) {

            Toast.makeText(view.getContext(),getString(R.string.SQLiteException),Toast.LENGTH_SHORT).show();

        }
    }

    public void onDestroy() {
        super.onDestroy();
        workoutCursor.close();
    }
}