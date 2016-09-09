package com.rdhouse.engine;

/**
 * Created by rutgerd on 9-9-2016.
 */
public interface GameLogicIntf {

    void init() throws Exception;

    void input(Window window);

    void update(float interval);

    void render(Window window);
}
