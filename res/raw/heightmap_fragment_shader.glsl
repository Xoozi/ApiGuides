#version 300 es
precision mediump float;

//in  vec4 v_Color;  效果很吊
in  vec3 v_Color;


out vec4 gl_FragColor;


void main()
{
    gl_FragColor = vec4(v_Color, 1.0);
}
