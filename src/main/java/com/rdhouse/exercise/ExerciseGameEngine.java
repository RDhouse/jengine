package com.rdhouse.exercise;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class ExerciseGameEngine implements Runnable {

    private Thread gameLoopThread;

    private ExerciseWindow exerciseWindow;

    private ExerciseGameLogicIntf gameLogic;

    private boolean running = false;

    public ExerciseGameEngine(String windowTitle, int width, int height, boolean vSync, ExerciseGameLogicIntf gameLogic) {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        exerciseWindow = new ExerciseWindow(width, height, windowTitle, vSync);
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
        exerciseWindow.init();
    }

    private void loop() {

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
            if (exerciseWindow.isKeyPressed(GLFW_KEY_ESCAPE)) {
                running = false;
            }
        }
        exerciseWindow.destroy();
    }

    protected void input() {
        gameLogic.input(exerciseWindow);
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render(exerciseWindow);
        exerciseWindow.update();
        stop();
    }

}
