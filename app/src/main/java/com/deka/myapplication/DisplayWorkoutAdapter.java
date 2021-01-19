package com.deka.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayWorkoutAdapter extends RecyclerView.Adapter<DisplayWorkoutAdapter.ViewHolder> {


    private ArrayList<String> exercises;
    private ArrayList<String> info;

    public DisplayWorkoutAdapter(ArrayList<String> exercises,ArrayList<String> info){
        this.exercises = exercises;
        this.info = info;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_workout,parent,false);
        return new ViewHolder(cv);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView exerciseTextView = cardView.findViewById(R.id.exercise_display_text_view);
        exerciseTextView.setText(exercises.get(position));
        TextView infoTextView = cardView.findViewById(R.id.info_text_View);
        infoTextView.setText(info.get(position));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AddExerciseActivity.class);
                Activity context = (Activity) v.getContext();
                intent.putExtra(AddExerciseActivity.DATE_USED,context.getIntent().getExtras().getString("selectedDate")+"");
                intent.putExtra(AddExerciseActivity.EXERCISE_NAME,exercises.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}
