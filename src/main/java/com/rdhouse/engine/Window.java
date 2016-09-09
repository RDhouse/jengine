package com.rdhouse.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by rutgerd on 9-9-2016.
 */
public class Window {

    private long windowHandle;

    private int width, height;
    private String title;

    private boolean vSync = false;
    private boolean resized = false;

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if ( windowHandle == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        });

        // Setup a resize callback
        glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        if (vSync) glfwSwapInterval(1);
        else glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(windowHandle);
    }

    public void destroy() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void update() {
        glfwSwapBuffers(windowHandle); // swap the color buffers
        glfwPollEvents();
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

}
