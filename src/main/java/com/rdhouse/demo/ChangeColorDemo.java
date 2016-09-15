package com.rdhouse.demo;

import com.rdhouse.engine.main.GameLogic;
import com.rdhouse.engine.main.JEngine;
import com.rdhouse.engine.input.MouseInput;
import com.rdhouse.engine.main.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

/**
 * Created by RDHouse on 12-9-2016.
 */
public class ChangeColorDemo implements GameLogic {

    private int direction = 0;

    private float color = 0.0f;

    @Override
    public void init(Window window) throws Exception {

    }

    @Override
    public void handleInput(Window window, MouseInput mouseInput) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
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

    public static void main(String[] args) {
        try {
            GameLogic game = new ChangeColorDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
