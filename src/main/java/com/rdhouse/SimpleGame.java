package com.rdhouse;

import com.rdhouse.engine.GameLogicIntf;
import com.rdhouse.engine.Renderer;
import com.rdhouse.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class SimpleGame implements GameLogicIntf {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    public SimpleGame() {
        renderer = new Renderer();
    }
    @Override
    public void init() throws Exception {
        renderer.init();
    }
    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
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
    public void render(Window window) {
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();
    }
}
