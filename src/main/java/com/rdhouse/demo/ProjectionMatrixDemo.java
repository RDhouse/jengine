package com.rdhouse.demo;

import com.rdhouse.engine.*;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Created by rutgerd on 14-9-2016.
 */
public class ProjectionMatrixDemo implements GameLogic {

    private static final float FOV = (float) Math.toRadians(90.0);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private ShaderProgram shaderProgram;
    private Mesh mesh;
    private Matrix4f projectionMatrix;

    @Override
    public void init(Window window) throws Exception {
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/fragment.frag"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");

        float[] positions = new float[] {
                -0.5f, 0.5f, -3.05f,
                -0.5f, -0.5f, -3.05f,
                0.5f, -0.5f, -3.05f,
                0.5f, 0.5f, -3.05f
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        };

        int[] indices = new int[]{0, 1, 3, 3, 1, 2};

        mesh = new Mesh(positions, colors, indices);
    }

    @Override
    public void handleInput(Window window) {

    }

    @Override
    public void update(float interval) {

    }

    @Override
    public void render(Window window) {

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Draw the Mesh
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        //glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    @Override
    public void cleanUp() {

    }

    public static void main(String[] args) {
        try {
            GameLogic game = new ProjectionMatrixDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
