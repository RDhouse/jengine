package com.rdhouse.exercise;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class ExerciseMain {

    public static void main(String[] args) {
        try {
            boolean vSync = false;
            ExerciseGameLogicIntf gameLogic = new ExerciseSimpleExerciseGame();
            ExerciseGameEngine engine = new ExerciseGameEngine("Title", 600, 480, vSync, gameLogic);
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
