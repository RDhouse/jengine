#version 430 core

layout (location = 0) in vec3 position;

out vec3 outColor;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    outColor = vec3(1.0, 1.0, 1.0);
}