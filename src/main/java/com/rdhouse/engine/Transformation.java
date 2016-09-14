package com.rdhouse.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by rutgerd on 14-9-2016.
 */
public class Transformation {

    private final Matrix4f projectionMatrix;
    private final Matrix4f worldMatrix;

    public Transformation() {
        projectionMatrix = new Matrix4f();
        worldMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getWorldMatrix(Vector3f offSet, Vector3f rotation, float scale) {
        worldMatrix.identity().translate(offSet).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }
}
