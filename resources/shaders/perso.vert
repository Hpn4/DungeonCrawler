#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

uniform int numCols;
uniform int numRows;

uniform vec2 texOff;
uniform vec2 pos;

uniform float scale;

uniform mat4 matrix;
out vec2 vs_texCoord;

void main()
{
    gl_Position = matrix * vec4(vec2(pos + position * vec2(scale)), 0.0, 1.0);

	float x = (texCoord.x / numCols + texOff.x);
    float y = (texCoord.y / numRows + texOff.y);

    vs_texCoord = vec2(x, y);
}