package com.rdhouse.games;

import com.rdhouse.engine.GameLogic;
import com.rdhouse.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

/**
 * Created by RDHouse on 12-9-2016.
 */
public class ChangeColorGame implements GameLogic {

    private int direction = 0;

    private float color = 0.0f;

    @Override
    public void init() throws Exception {

    }

    @Override
    public void handleInput(Window window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
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
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, color);
    }

    @Override
    public void cleanUp() {

    }
}
