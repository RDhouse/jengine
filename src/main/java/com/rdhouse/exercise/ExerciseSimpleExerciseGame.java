package com.rdhouse.exercise;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class ExerciseSimpleExerciseGame implements ExerciseGameLogicIntf {

    private int direction = 0;

    private float color = 0.0f;

    private final ExerciseRenderer exerciseRenderer;

    public ExerciseSimpleExerciseGame() {
        exerciseRenderer = new ExerciseRenderer();
    }
    @Override
    public void init() throws Exception {
        exerciseRenderer.init();
    }
    @Override
    public void input(ExerciseWindow exerciseWindow) {
        if ( exerciseWindow.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( exerciseWindow.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
    }
    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
    }
    @Override
    public void render(ExerciseWindow exerciseWindow) {
        if ( exerciseWindow.isResized() ) {
            glViewport(0, 0, exerciseWindow.getWidth(), exerciseWindow.getHeight());
            exerciseWindow.setResized(false);
        }
        exerciseWindow.setClearColor(color, color, color, 0.0f);
        exerciseRenderer.clear();
    }
}
