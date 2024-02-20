#version 330

uniform sampler2D sceneTex;

in vec2 vs_texCoord;

out vec3 fs_color;

void main() {
    fs_color = texture(sceneTex, vs_texCoord).xyz;
}