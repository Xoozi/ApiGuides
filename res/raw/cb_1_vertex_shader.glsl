#version 300 es
uniform     float   u_RadianAngle;
in          vec4    a_Position;
in          vec4    a_Color;


out         vec4    v_Color;

void main()
{
    mat2    rotation = mat2(cos(u_RadianAngle), sin(u_RadianAngle),
                            -sin(u_RadianAngle), cos(u_RadianAngle));
    gl_Position = mat4(rotation) * a_Position;
    v_Color = a_Color;
}
