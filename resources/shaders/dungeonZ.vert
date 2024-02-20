#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;

uniform mat4 matrix;

uniform int numCols;
uniform int numRows;

uniform vec3 tilePos;
uniform vec2 texOff;

out vec2 vs_texCoord;

void main() 
{
	gl_Position = matrix * vec4(tilePos + position * 0.5, 1.0);

	float x = (texCoord.x / numCols + texOff.x);
    float y = (texCoord.y / numRows + texOff.y);

    vs_texCoord = vec2(x, y);
}