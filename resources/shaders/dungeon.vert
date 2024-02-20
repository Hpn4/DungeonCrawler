#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 tilePos;
layout (location=3) in vec2 texOff;

uniform mat4 matrix;

uniform int numCols;
uniform int numRows;

out vec2 vs_texCoord;

void main() 
{
	gl_Position = matrix * vec4(tilePos + position, 1.0);

	float x = (texCoord.x / numCols + texOff.x);
    float y = (texCoord.y / numRows + texOff.y);

    vs_texCoord = vec2(x, y);
}