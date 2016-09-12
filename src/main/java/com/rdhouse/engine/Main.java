package com.rdhouse.engine;

/**
 * Created by RDHouse on 12-9-2016.
 */
public class Main {

    public static void main(String[] args) {
        GameLogic simpleGame = new SimpleGame();
        JEngine jEngine = new JEngine(simpleGame);
        jEngine.start();
    }
}
