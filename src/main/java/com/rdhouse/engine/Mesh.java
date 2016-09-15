package com.rdhouse.engine;

import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by RDHouse on 13-9-2016.
 */
public class Mesh {

    private int vaoId;
    private int vboId;
    private final int vertexCount;
    private Texture texture;
    private List<Integer> vboIdList;

    private Vector3f color;

    public Mesh(float[] positions, float[] colors, int[] indices) {
        vertexCount = indices.length;
        vboIdList = new ArrayList<>();
        color = new Vector3f();

        // Create the VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create pos VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer posBuffer = Utils.createFloatBuffer(positions);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Create the colors VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer colorBuffer = Utils.createFloatBuffer(colors);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Create indices VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = Utils.createIntBuffer(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    public Mesh(float[] positions, float[] textCoords, int[] indices, Texture texture) {
        this.texture = texture;
        vertexCount = indices.length;
        vboIdList = new ArrayList<>();

        // Create the Vertex Array Object
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create the positions vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer verticesBuffer = Utils.createFloatBuffer(positions);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Create the texture vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer textureBuffer = Utils.createFloatBuffer(textCoords);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        // Create the indices vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = Utils.createIntBuffer(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
        vertexCount = indices.length;
        vboIdList = new ArrayList<>();

        // Create the Vertex Array Object VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create the VBO's
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer floatBuffer = Utils.createFloatBuffer(positions);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Create the texture vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer textureBuffer = Utils.createFloatBuffer(textCoords);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        // Create the normals vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer normalsBuffer = Utils.createFloatBuffer(normals);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        // Create the indices vbo
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = Utils.createIntBuffer(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {
        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);

        if (texture != null) {
            // Bind the texture
            texture.bind();
        }

        // Draw the Mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE0, 0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        if (texture != null) {
            texture.cleanUp();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public Vector3f getColor() {
        return color;
    }

}
