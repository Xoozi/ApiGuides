#version 300 es
uniform mat4    u_Matrix;
in      vec3    a_Position;
out     vec3    v_Color;


void main()
{
    v_Color = mix(vec3(0.180, 0.467, 0.153),
                  vec3(0.660, 0.670, 0.680),
                  a_Position.y);

    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}
