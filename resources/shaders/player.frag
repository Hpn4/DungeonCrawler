#version 330

in vec2 vs_texCoord;

layout (location=0) out vec4 fragColor;

uniform sampler2D texBody;
uniform bool isDashing;

void main()
{
	if(isDashing)
		fragColor = vec4(1.2, 1, 1.9, 1) * texture(texBody, vs_texCoord);
	else
		fragColor = texture(texBody, vs_texCoord);
}