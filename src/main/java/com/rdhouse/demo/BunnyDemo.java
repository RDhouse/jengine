package com.rdhouse.demo;

import com.rdhouse.engine.graph.Mesh;
import com.rdhouse.engine.graph.OBJLoader;
import com.rdhouse.engine.graph.ShaderProgram;
import com.rdhouse.engine.graph.Transformation;
import com.rdhouse.engine.input.MouseInput;
import com.rdhouse.engine.main.Camera;
import com.rdhouse.engine.main.GameLogic;
import com.rdhouse.engine.main.JEngine;
import com.rdhouse.engine.main.Window;
import com.rdhouse.engine.model.GameObject;
import com.rdhouse.engine.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by RDHouse on 9-3-2017.
 */
public class BunnyDemo implements GameLogic {

    private static final float FOV = (float) Math.toRadians(90);
    private static final float Z_NEAR = 0.01f; // keep as small as possible
    private static final float Z_FAR = 1000.0f; // keep as big as possible

    private Vector3f cameraInc;
    private Camera camera;

    private static final float MOUSE_SENS = 0.2f;
    private static final float CAMERA_STEP = 0.05f;

    private Transformation transformation;
    private ShaderProgram shader;
    private GameObject bunny;


    @Override
    public void init(Window window) throws Exception {
        transformation = new Transformation();
        camera = new Camera();
        cameraInc = new Vector3f();

        Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/bunny.obj");
        bunny = new GameObject(mesh);

        bunny.setScale(0.5f);
        bunny.setPosition(0, 0, -2);
        // create shader
        shader = new ShaderProgram();
        shader.createVertexShader(Utils.loadResource("src/main/resources/shaders/bunny_vs.glsl"));
        shader.createVertexShader(Utils.loadResource("src/main/resources/shaders/bunny_fs.glsl"));
        shader.link();

        // create uniforms for modelView and projection matrices and texture
        shader.createUniform("projectionMatrix");
        shader.createUniform("modelViewMatrix");

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
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
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



        shader.bind();

        // Update projection-matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view-matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Update the model-view-matrix
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(bunny, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);

        // Render the mesh
        bunny.getMesh().render();

        shader.unbind();
    }

    @Override
    public void cleanUp() {
        if (shader != null) {
            shader.cleanup();
        }
        bunny.getMesh().cleanUp();
    }

    public static void main(String[] args) {
        JEngine engine = new JEngine(new BunnyDemo());
        engine.start();
    }
}
