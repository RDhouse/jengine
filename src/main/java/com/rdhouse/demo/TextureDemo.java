package com.rdhouse.demo;

import com.rdhouse.engine.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by RDHouse on 14-9-2016.
 */
public class TextureDemo implements GameLogic {

    private static final float FOV = (float) Math.toRadians(90);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private int displayXInc = 0;
    private int displayYInc = 0;
    private int displayZInc = 0;
    private int scaleInc = 0;

    private Transformation transformation;

    private ShaderProgram shaderProgram;

    private GameObject[] gameObjects = null;

    @Override
    public void init(Window window) throws Exception {
        transformation = new Transformation();
        // Create the shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/texture_vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/texture_fragment.frag"));
        shaderProgram.link();

        // Create uniforms for world and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");

        // Create the Mesh
        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,
        };

        Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        Mesh mesh = new Mesh(positions, textCoords, indices, texture);
        GameObject object = new GameObject(mesh);
        object.setPosition(0, 0, -2);
        gameObjects = new GameObject[] {object};
    }

    @Override
    public void handleInput(Window window) {
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
    public void update(float interval) {
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
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for(GameObject gameItem : gameObjects) {
            // Set world matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            // Render the mes for this game item
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();

    }

    @Override
    public void cleanUp() {
        if (shaderProgram != null) {
            shaderProgram.cleanUp();
        }
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }

    public static void main(String[] args) {
        try {
            GameLogic game = new TextureDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
