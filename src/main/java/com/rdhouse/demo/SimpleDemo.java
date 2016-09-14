package com.rdhouse.demo;

import com.rdhouse.engine.*;

/**
 * Created by rutgerd on 14-9-2016.
 */
public class SimpleDemo implements GameLogic {

    private ShaderProgram shaderProgram;
    private Mesh mesh;

    @Override
    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/simplevertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/simplefragment.frag"));
        shaderProgram.link();

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        };

        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2
        };

        mesh = new Mesh(vertices, colors, indices);


    }

    @Override
    public void handleInput(Window window) {

    }

    @Override
    public void update(float interval) {

    }

    @Override
    public void render(Window window) {
        shaderProgram.bind();
        mesh.render();
        shaderProgram.unbind();
    }

    @Override
    public void cleanUp() {
        if (shaderProgram != null) {
            shaderProgram.cleanUp();
        }
    }

    public static void main(String[] args) {
        try {
            GameLogic game = new SimpleDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
