package com.rdhouse.demo;

import com.rdhouse.engine.graph.Mesh;
import com.rdhouse.engine.graph.ShaderProgram;
import com.rdhouse.engine.graph.Transformation;
import com.rdhouse.engine.input.MouseInput;
import com.rdhouse.engine.main.GameLogic;
import com.rdhouse.engine.main.JEngine;
import com.rdhouse.engine.main.Window;
import com.rdhouse.engine.model.GameObject;
import com.rdhouse.engine.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by rutgerd on 14-9-2016.
 */
public class CubeDemo implements GameLogic {

    private Transformation transformation;
    private ShaderProgram shaderProgram;

    private GameObject[] gameObjects= null;

    private static final float FOV = (float) Math.toRadians(90);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private int displayXInc = 0;
    private int displayYInc = 0;
    private int displayZInc = 0;
    private int scaleInc = 0;

    @Override
    public void init(Window window) throws Exception {
        transformation = new Transformation();

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/fragment.frag"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        float[] positions = new float[]{
                //	VO
                -0.5f, 0.5f, 0.5f,
                //	V1
                -0.5f, -0.5f, 0.5f,
                //	V2
                0.5f, -0.5f, 0.5f,
                //	V3
                0.5f, 0.5f, 0.5f,
                //	V4
                -0.5f, 0.5f, -0.5f,
                //	V5
                0.5f, 0.5f, -0.5f,
                //	V6
                -0.5f, -0.5f, -0.5f,
                //	V7
                0.5f, -0.5f, -0.5f,
        };

        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[]{
                //	Front	face
                0, 1, 3, 3, 1, 2,
                //	Top	Face
                4, 0, 3, 5, 4, 3,
                //	Right	face
                3, 2, 7, 5, 3, 7,
                //	Left	face
                0, 1, 6, 4, 0, 6,
                //	Bottom	face
                6, 1, 2, 7, 6, 2,
                //	Back	face
                4, 6, 7, 5, 4, 7,
        };

        Mesh mesh = new Mesh(positions, colors, indices, null);
        GameObject gameObject = new GameObject(mesh);
        gameObject.setPosition(0, 0, -2);
        gameObjects = new GameObject[] { gameObject };
    }

    @Override
    public void handleInput(Window window, MouseInput mouseInput) {

        displayYInc = 0;
        displayXInc = 0;
        displayZInc = 0;
        scaleInc = 0;

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displayYInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displayYInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displayXInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displayXInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displayZInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displayZInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        for (GameObject object : gameObjects) {
            // Update position
            Vector3f objectPos = object.getPosition();
            float x = objectPos.x + displayXInc * 0.01f;
            float y = objectPos.y + displayYInc * 0.01f;
            float z = objectPos.z + displayZInc * 0.01f;
            object.setPosition(x, y, z);

            // Update scale
            float scale = object.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0) {
                scale = 0;
            }
            object.setScale(scale);

            // Update rotation angle
            float rotation = object.getRotation().z + 1.5f;
            if (rotation > 360) {
                rotation = 0;
            }
            object.setRotation(rotation, rotation, rotation);
        }
    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projectionMatrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Render each gameObject
        for (GameObject gameObject : gameObjects) {
            // Set the worldMatrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameObject.getPosition(),
                    gameObject.getRotation(),
                    gameObject.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            // Render the mesh for this game object
            gameObject.getMesh().render();
        }

        shaderProgram.unbind();
    }

    @Override
    public void cleanUp() {
        if (shaderProgram != null)  {
            shaderProgram.cleanUp();
        }
        for (GameObject gameObject: gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }

    public static void main(String[] args) {
        try {
            GameLogic game = new CubeDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
