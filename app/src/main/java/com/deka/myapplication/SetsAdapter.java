package com.deka.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder>{

    private ArrayList<Integer> weights;
    private ArrayList<Integer> reps;
    Cursor cursor;
    static int pos;
    int selectedPosition = -1;


    public SetsAdapter(ArrayList<Integer> weights,ArrayList<Integer> reps){
        this.weights = weights;
        this.reps = reps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_sets,parent,false);
        return new ViewHolder(cv);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if(selectedPosition==position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#b3d9ff"));
        }
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        final CardView cardView  = holder.cardView;
        TextView weightsTextView = cardView.findViewById(R.id.weights_text);
        weightsTextView.setText(weights.get(position).toString());
        TextView repsTextView = cardView.findViewById(R.id.reps_text);
        repsTextView.setText(reps.get(position).toString());




        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = AddExerciseActivity.id.get(position);
                GymNerdsDatabaseHelper gymNerdsDatabaseHelper = new GymNerdsDatabaseHelper(v.getContext());
                try {
                    SQLiteDatabase db = gymNerdsDatabaseHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT _id, WEIGHT, REPS FROM WORKOUT_DETAILS WHERE _id = ? ",
                            new String[]{id+""});
                    cursor.moveToFirst();
                    int weight = cursor.getInt(1);
                    int reps = cursor.getInt(2);
                    View view = v.getRootView();
                    EditText editWeightText = view.findViewById(R.id.edit_weight);
                    editWeightText.setText(weight+"");
                    EditText editSetText = view.findViewById(R.id.edit_reps);
                    editSetText.setText(reps+"");
                    pos = position;
                }
                catch (SQLiteException e) {
                    Toast.makeText(v.getContext(),""+R.string.SQLiteException, Toast.LENGTH_SHORT).show();
                }
                cursor.close();

                selectedPosition = position;
                notifyDataSetChanged();


            }
        });
    }

    @Override
    public int getItemCount() {
        return reps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;

        }
    }


}
