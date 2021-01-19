package com.deka.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;


public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        CalendarView calendarView = findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, DisplayWorkoutActivity.class);
                String date;
                month++;
                if(month<10){
                    date = year+"-"+"0"+month+"-"+ ((dayOfMonth<=9)?"0"+dayOfMonth:dayOfMonth);
                }else{
                    date = year+"-"+month+"-"+ ((dayOfMonth<=9)?"0"+dayOfMonth:dayOfMonth);
                }
                intent.putExtra(DisplayWorkoutActivity.DATE,date);

                startActivity(intent);
            }
        });
    }
}