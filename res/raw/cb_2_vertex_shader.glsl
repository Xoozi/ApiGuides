#version 300 es
uniform     mat4    u_Matrix;
in          vec4    a_Position;
in          vec4    a_Color;


out         vec4    v_Color;

void main()
{
    gl_Position = u_Matrix * a_Position;
    gl_PointSize = 20.0;
    v_Color = a_Color;
}
