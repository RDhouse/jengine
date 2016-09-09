package com.rdhouse.engine;

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
        running = false;
        try {
            gameLoopThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {

    }

    private void loop() {

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
    }

}
