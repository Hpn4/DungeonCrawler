#version 330

in vec2 vs_texCoord;

layout (location=0) out vec4 fragColor;

uniform sampler2D texWeapon;

void main()
{
	fragColor = texture(texWeapon, vs_texCoord);
}