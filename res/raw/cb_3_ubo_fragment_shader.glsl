#version 300 es
precision mediump float;

in vec4    v_Color;

out vec4    gl_FragColor;

void main()
{
    gl_FragColor = vec4(v_Color.x, v_Color.y, v_Color.z, 1.0);
}
