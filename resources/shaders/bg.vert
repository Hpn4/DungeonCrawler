#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

uniform float animAngle;

out vec2 vs_texCoord;
out vec2 vs_animCoord;

void main()
{
    gl_Position = vec4(position * vec2(0.774, 1), 0.0, 1.0);
    vs_texCoord = texCoord;
    vs_animCoord = vec2(cos(animAngle) / 10, sin(animAngle) / 5);
}