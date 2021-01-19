package com.deka.myapplication;

public class ExerciseCategory {
    private String name;

    private ExerciseCategory(String name){
        this.name = name;
    }

    public static final ExerciseCategory[] exerciseCategories = new ExerciseCategory[]{
            new ExerciseCategory("Chest"),
            new ExerciseCategory("Biceps"),
            new ExerciseCategory("Triceps"),
            new ExerciseCategory("Shoulders"),
            new ExerciseCategory("Legs"),
            new ExerciseCategory("Back"),
            new ExerciseCategory("Abs"),
            new ExerciseCategory("Cardio")};

    public String getName() {
        return name;
    }

    public String toString(){
        return this.name;
    }
}
