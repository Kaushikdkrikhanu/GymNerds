package com.deka.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ExerciseActivity extends AppCompatActivity {

    public static final String EXERCISE_CATEGORY = "type";
    public static final String DATE = "date";
    CursorAdapter cursorAdapter;
    Cursor intentCursor;
    Cursor nameCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        SQLiteOpenHelper gymNerdDatabaseHelper = new GymNerdsDatabaseHelper(this);
        try {
            String type = getIntent().getExtras().getString(EXERCISE_CATEGORY);
            SQLiteDatabase db = gymNerdDatabaseHelper.getReadableDatabase();
            nameCursor = db.query("EXERCISE",
                    new String[]{"_id","NAME"},
                    "CATEGORY = ?",
                    new String[]{type},
                    null,null,null);

            cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    nameCursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            ListView exerciseList = findViewById(R.id.exercise_list);
            exerciseList.setAdapter(cursorAdapter);
            exerciseList.setOnItemClickListener(onExerciseClickListener);
        }
        catch (SQLiteException e){
            Toast.makeText(this,getString(R.string.SQLiteException), Toast.LENGTH_SHORT).show();
        }


    }

    AdapterView.OnItemClickListener onExerciseClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ExerciseActivity.this,AddExerciseActivity.class);
            intentCursor = cursorAdapter.getCursor();
            while(position>1){
                intentCursor.moveToNext();
                position--;
            }
            String exercise = intentCursor.getString(1);
            intent.putExtra(AddExerciseActivity.EXERCISE_NAME,exercise);
            intent.putExtra(AddExerciseActivity.DATE_USED,getIntent().getExtras().getString("date"));
            startActivity(intent);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClickNewExercise(View view) {
        Intent intent = new Intent(this,CreateNewExerciseActivity.class);
        startActivity(intent);
    }
}