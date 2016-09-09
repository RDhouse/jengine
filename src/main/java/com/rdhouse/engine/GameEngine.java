package com.rdhouse.engine;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class GameEngine implements Runnable {

    private Thread gameLoopThread;

    private Window window;

    private GameLogicIntf gameLogic;

    private boolean running = false;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, GameLogicIntf gameLogic) {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(width, height, windowTitle, vSync);
        this.gameLogic = gameLogic;
    }

    @Override
    public void run() {
        try {
            init();
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        running = true;
        gameLoopThread.start();

    }

    public void stop() {
        try {
            gameLoopThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        window.init();
    }

    private void loop() {
        GL.createCapabilities();

        long lastTime = System.nanoTime();
        float delta = 0.0f;
        double ns = 1_000_000_000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update(delta);
                updates++;
                delta--;
                input();
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.printf("%d ups, %s fps\n", updates, frames);
                updates = 0;
                frames = 0;
            }
            if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
                running = false;
            }
        }
        window.destroy();
    }

    protected void input() {
        gameLogic.input(window);
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
        stop();
    }

}
