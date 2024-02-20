#version 330

uniform vec3 color;

layout (location=0) out vec4 fs_color;

uniform sampler2D noiseTex;
uniform sampler2D solTex;

in vec2 vs_texCoord;
in vec2 vs_animCoord;

void main() {
	vec3 test = color;
	vec4 sol = texture(solTex, vs_texCoord);
	float scale = texture(noiseTex, vs_texCoord + vs_animCoord).x + 0.3;

	if(sol.a == 0) {
		fs_color = vec4(vec3(0.18359375, 0.50390625, 0.2109375) * scale, 1.0);
    } else {
    	fs_color = vec4(sol.xyz * scale, 1.0);
    }
    
}