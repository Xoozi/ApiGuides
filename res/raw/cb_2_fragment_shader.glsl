#version 300 es
precision mediump float;

in vec4    v_Color;

out vec4    gl_FragColor;

void main()
{
    gl_FragColor = v_Color;
}
