package com.deka.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseCategoryActivity extends AppCompatActivity {
    public static final String DATE = "date" ;
    ArrayAdapter<ExerciseCategory> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_category);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                ExerciseCategory.exerciseCategories);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onCategoryClickListener);
    }

    AdapterView.OnItemClickListener onCategoryClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ExerciseCategoryActivity.this,ExerciseActivity.class);
            ExerciseCategory type = adapter.getItem(position);
            intent.putExtra(ExerciseActivity.EXERCISE_CATEGORY,type.getName());
            intent.putExtra(ExerciseActivity.DATE,getIntent().getExtras().getString("date"));
            startActivity(intent);
        }
    };

    
}