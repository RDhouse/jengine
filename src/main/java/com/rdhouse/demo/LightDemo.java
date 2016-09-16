package com.rdhouse.demo;

import com.rdhouse.engine.graph.*;
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
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by rutgerd on 16-9-2016.
 */
public class LightDemo implements GameLogic {


    private static final float FOV = (float) Math.toRadians(90);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private Vector3f cameraInc;
    private Camera camera;

    private static final float MOUSE_SENS = 0.2f;
    private static final float CAMERA_STEP = 0.05f;

    private float specularPower;

    private Transformation transformation;
    private ShaderProgram shaderProgram;
    private PointLight pointLight;
    private Vector3f ambientLight;
    private GameObject[] gameObjects = null;

    @Override
    public void init(Window window) throws Exception {
        transformation = new Transformation();
        camera = new Camera();
        cameraInc = new Vector3f();
        specularPower = 10.0f;
        // Create the shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("src/main/resources/shaders/light_vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("src/main/resources/shaders/light_fragment.frag"));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");

        float reflectance = 1f;

        Mesh mesh = OBJLoader.loadMesh("src/main/resources/models/cube.obj");
        Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);

        GameObject gameObject = new GameObject(mesh);
        gameObject.setScale(0.5f);
        gameObject.setPosition(0, 0, -2);
        gameObjects = new GameObject[]{gameObject};

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);




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
        float lightPos = pointLight.getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)) {
            this.pointLight.getPosition().z = lightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            this.pointLight.getPosition().z = lightPos - 0.1f;
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

        // Update Light Uniforms
        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);
        // Get a copy of the light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("pointLight", currPointLight);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for(GameObject gameObject : gameObjects) {
            // Set world matrix for this item
//            Matrix4f worldMatrix = transformation.getWorldMatrix(
//                    gameItem.getPosition(),
//                    gameItem.getRotation(),
//                    gameItem.getScale());
//            shaderProgram.setUniform("worldMatrix", worldMatrix);
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            // Render the mesh for this game item
            shaderProgram.setUniform("material", gameObject.getMesh().getMaterial());

            // Render the mes for this game item
            gameObject.getMesh().render();
        }

        shaderProgram.unbind();

    }

    @Override
    public void cleanUp() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }

    public static void main(String[] args) {
        try {
            GameLogic game = new LightDemo();
            JEngine engine = new JEngine(game);
            engine.start();
            engine.joinThread();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
