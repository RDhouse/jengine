package com.rdhouse.demo;

import com.rdhouse.engine.graph.Mesh;
import com.rdhouse.engine.graph.ShaderProgram;
import com.rdhouse.engine.input.MouseInput;
import com.rdhouse.engine.main.GameLogic;
import com.rdhouse.engine.main.JEngine;
import com.rdhouse.engine.main.Window;
import com.rdhouse.engine.utils.Utils;

import static org.lwjgl.opengl.GL11.glViewport;


/**
 * Created by rutgerd on 13-9-2016.
 */
public class ColorMeshDemo implements GameLogic {

    ShaderProgram shaderProgram;
    Mesh mesh;

    @Override
    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/fragment.frag"));
        shaderProgram.link();

        shaderProgram.createUniform("color");
        shaderProgram.createUniform("useColor");

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        };

        int[] indices = new int[]{0, 1, 3, 3, 1, 2};

        mesh = new Mesh(vertices, colors, indices);
    }

    @Override
    public void handleInput(Window window, MouseInput mouseInput) {

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        mesh.render();

        shaderProgram.unbind();
    }

    @Override
    public void cleanUp() {
        if (shaderProgram != null)  {
            shaderProgram.cleanup();
        }
        if (mesh != null) {
            mesh.cleanUp();
        }
    }

    public static void main(String[] args) {
        try {
            GameLogic game = new ColorMeshDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
