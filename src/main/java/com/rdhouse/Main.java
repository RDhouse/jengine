package com.rdhouse;

import com.rdhouse.engine.GameEngine;
import com.rdhouse.engine.GameLogicIntf;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = false;
            GameLogicIntf gameLogic = new SimpleGame();
            GameEngine engine = new GameEngine("Title", 600, 480, vSync, gameLogic);
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
