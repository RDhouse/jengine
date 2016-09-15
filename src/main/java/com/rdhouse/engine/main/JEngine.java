package com.rdhouse.engine.main;

import com.rdhouse.engine.input.MouseInput;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

/**
 * Created by RDHouse on 12-9-2016.
 */
public class JEngine implements Runnable {

    private Thread gameThread;
    private Window window;
    private GameLogic gameLogic;

    private MouseInput mouseInput;

    private boolean running = false;

    public JEngine(GameLogic game) {
        gameThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(800, 600, "Game", true);
        mouseInput = new MouseInput();
        gameLogic = game;
    }

    public void start() {
        running = true;
        gameThread.start();
    }


    public void joinThread() {
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            init();
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    private void loop() {
        long lastTime = System.nanoTime();
        double ns = 1_000_000_000.0 / 60;
        float delta = 0.0f;
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
                handleInput();
            }

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000 ) {
                timer += 1000;
                System.out.printf("%d ups, %s fps\n", updates, frames);
                updates = 0;
                frames = 0;
            }
        }
    }

    public void init() throws Exception {
        window.init();
        mouseInput.init(window);
        gameLogic.init(window);
    }

    public void handleInput() {
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            running = false;
        }
        mouseInput.input(window);
        gameLogic.handleInput(window, mouseInput);
    }

    public void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    public void render() {
        gameLogic.render(window);
        window.update();
    }

    public void cleanUp() {
        gameLogic.cleanUp();
        window.destroy();
    }
}
