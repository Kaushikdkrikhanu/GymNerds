package com.deka.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateNewExerciseActivity extends AppCompatActivity {
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_exercise);

    }

    private void updateList(){
        EditText editTextName = findViewById(R.id.editTextName);
        String name = editTextName.getText().toString();
        Spinner spinner = findViewById(R.id.spinner);
        String category = spinner.getSelectedItem().toString();
        GymNerdsDatabaseHelper gymNerdsDatabaseHelper = new GymNerdsDatabaseHelper(this);
        try {
            SQLiteDatabase db = gymNerdsDatabaseHelper.getWritableDatabase();
            cursor = db.rawQuery("SELECT _id, NAME FROM EXERCISE ORDER BY NAME ASC",new String[]{});
            boolean flag = true;
            while(cursor.moveToNext()){
                if((cursor.getString(1).toLowerCase()).equals(name.toLowerCase())){
                    flag = false;
                }
            }
            if(name==null||category==null){
                Toast.makeText(this,"Add Exercise Details",Toast.LENGTH_SHORT).show();
            }
            else if(flag){
                insertExercise(db,name,category);
                Toast.makeText(this, "Exercise Added", Toast.LENGTH_SHORT).show();
            }
            else if(!flag){
                Toast.makeText(this, "Exercise Already Exists", Toast.LENGTH_SHORT).show();
            }

        }
        catch (SQLiteException e){
            Toast.makeText(this,getString(R.string.SQLiteException), Toast.LENGTH_SHORT).show();
        }
    }
    private static void insertExercise(SQLiteDatabase db, String name, String category){
        ContentValues exercises = new ContentValues();
        exercises.put("NAME",name);
        exercises.put("CATEGORY",category);
        db.insert("EXERCISE",null,exercises);
    }

    public void onClickSave(View view) {
        updateList();
    }
}