package com.rdhouse.demo;

import com.rdhouse.engine.input.MouseInput;
import com.rdhouse.engine.main.GameLogic;
import com.rdhouse.engine.main.JEngine;
import com.rdhouse.engine.main.Window;

/**
 * Created by RDHouse on 9-3-2017.
 */
public class WoodyManDemo implements GameLogic {

    @Override
    public void init(Window window) throws Exception {

    }

    @Override
    public void handleInput(Window window, MouseInput mouseInput) {

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

    }

    @Override
    public void render(Window window) {

    }

    @Override
    public void cleanUp() {

    }

    public static void main(String[] args) {
        try {
            GameLogic game = new WoodyManDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
