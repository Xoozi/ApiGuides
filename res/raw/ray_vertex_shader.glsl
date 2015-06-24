#version 300 es
uniform     mat4    u_Matrix;
in   vec4    a_Position;


void main()
{
    gl_Position = u_Matrix * a_Position;
    gl_PointSize = 20.0;
}
