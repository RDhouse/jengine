package com.rdhouse.games;

import com.rdhouse.engine.*;

/**
 * Created by rutgerd on 13-9-2016.
 */
public class TriangleGame implements GameLogic {

    ShaderProgram shaderProgram;

    float[] vertices = new float[] {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    @Override
    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/fragment.frag"));
        shaderProgram.link();
    }

    @Override
    public void handleInput(Window window) {

    }

    @Override
    public void update(float interval) {

    }

    @Override
    public void render(Window window) {

    }

    @Override
    public void cleanUp() {

    }

    public static void main(String[] args) {
        GameLogic game = new TriangleGame();
        JEngine engine = new JEngine(game);
        engine.start();
    }
}
