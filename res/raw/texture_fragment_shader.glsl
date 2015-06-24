#version 300 es
precision mediump float;

uniform     sampler2D   u_TextureUnit;
in     vec2        v_TextureCoordinates;

out vec4 gl_FragColor;
void main()
{
    gl_FragColor = texture(u_TextureUnit, v_TextureCoordinates);
}
