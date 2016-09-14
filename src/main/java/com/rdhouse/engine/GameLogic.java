package com.rdhouse.engine;

/**
 * Created by RDHouse on 12-9-2016.
 */
public interface GameLogic {

    void init(Window window) throws Exception;

    void handleInput(Window window);

    void update(float interval);

    void render(Window window);

    void cleanUp();

}
