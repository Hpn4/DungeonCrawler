#version 330

#define NUM_COLS 8
#define NUM_ROWS 4

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

uniform vec2 texOff;
uniform vec2 pos;

uniform float scale;
uniform mat4 matrix;

out vec2 vs_texCoord;

void main()
{
    gl_Position = matrix * vec4(vec2(pos + position * vec2(scale)) * vec2(3, 3), 0.0, 1.0);

    float x = texCoord.x / NUM_COLS + texOff.x;
    float y = texCoord.y / NUM_ROWS + texOff.y;

    vs_texCoord = vec2(x, y);
}