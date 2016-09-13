package com.rdhouse.exercise;

/**
 * Created by rutgerd on 9-9-2016.
 */
public interface ExerciseGameLogicIntf {

    void init() throws Exception;

    void input(ExerciseWindow exerciseWindow);

    void update(float interval);

    void render(ExerciseWindow exerciseWindow);
}
