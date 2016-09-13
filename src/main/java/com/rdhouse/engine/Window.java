package com.rdhouse.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by RDHouse on 12-9-2016.
 */
public class Window {

    private long windowHandle;

    private int width;
    private int height;
    private String title;

    private boolean vSync = false;
    private boolean resized = false;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public void init() {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        // Configure ExerciseWindow
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the ExerciseWindow
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Resize callback
        glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        });

        // Get the resolution of primary moniter
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center the window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        // Enable V-sync
        if (vSync) glfwSwapInterval(1);
        else glfwSwapInterval(0);

        // Show the window
        glfwShowWindow(windowHandle);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void destroy() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

}
