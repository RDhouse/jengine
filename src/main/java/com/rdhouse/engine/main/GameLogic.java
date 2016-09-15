package com.rdhouse.engine.main;

import com.rdhouse.engine.input.MouseInput;

/**
 * Created by RDHouse on 12-9-2016.
 */
public interface GameLogic {

    void init(Window window) throws Exception;

    void handleInput(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);

    void render(Window window);

    void cleanUp();

}
