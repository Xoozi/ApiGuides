#version 300 es
uniform     mat4    u_Matrix;
uniform     vec3    u_MaterialAmbient;
uniform     vec3    u_LightAmbient;
in          vec4    a_Position;


out         vec4    v_FinalColor;
void main()
{
    vec3    ambient = u_MaterialAmbient * u_LightAmbient;
    v_FinalColor    = vec4(ambient, 1.0);
    gl_Position     = u_Matrix * a_Position;
}
