#version 300 es
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_TextureUnit;
in     vec2        v_TextureCoordinates;

out vec4 gl_FragColor;

void main()
{
    gl_FragColor = texture(u_TextureUnit, v_TextureCoordinates);
}
