package com.rdhouse.demo;

import com.rdhouse.engine.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by rutgerd on 15-9-2016.
 */
public class ModelsDemo implements GameLogic {


    private static final float FOV = (float) Math.toRadians(90);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private Vector3f cameraInc;
    private Camera camera;

    private static final float MOUSE_SENS = 0.2f;
    private static final float CAMERA_STEP = 0.05f;

    private Transformation transformation;
    private ShaderProgram shaderProgram;
    private GameObject[] gameObjects = null;

    @Override
    public void init(Window window) throws Exception {
        transformation = new Transformation();
        camera = new Camera();
        cameraInc = new Vector3f();
        // Create the shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/model_vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/model_fragment.frag"));
        shaderProgram.link();

        // Create uniforms for world and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

        // Create uniform for default color and the flag that controls it
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");

        // Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/bunny.obj");
        Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/cube.obj");
        Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        mesh.setTexture(texture);
        GameObject obj = new GameObject(mesh);
        obj.setScale(0.5f);
        obj.setPosition(0, 0, -2);
        gameObjects = new GameObject[]{obj};
    }

    @Override
    public void handleInput(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // update camera pos
        camera.movePosition(cameraInc.x * CAMERA_STEP, cameraInc.y * CAMERA_STEP, cameraInc.z * CAMERA_STEP);

        // update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplacementVec();
            camera.moveRotation(rotVec.x * MOUSE_SENS, rotVec.y * MOUSE_SENS, 0);
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

        // Update View Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for(GameObject gameItem : gameObjects) {
            // Set world matrix for this item
//            Matrix4f worldMatrix = transformation.getWorldMatrix(
//                    gameItem.getPosition(),
//                    gameItem.getRotation(),
//                    gameItem.getScale());
//            shaderProgram.setUniform("worldMatrix", worldMatrix);
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            shaderProgram.setUniform("colour", gameItem.getMesh().getColour());
            shaderProgram.setUniform("useColour", gameItem.getMesh().isTextured() ? 0 : 1);

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
            GameLogic game = new ModelsDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
