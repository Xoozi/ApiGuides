#version 300 es
uniform     mat4    u_Matrix;
uniform     vec3    u_MaterialAmbient;
uniform     vec3    u_LightAmbient;
uniform     vec3    u_LightVector;
in          vec4    a_Position;
in          vec3    a_Normal;


out         vec4    v_FinalColor;


vec3        materialColor;
vec3 getAmbientLighting();
vec3 getDirectionalLighting();

void main()
{
    materialColor    = u_MaterialAmbient * u_LightAmbient;

    vec3    color    = getAmbientLighting();
    color           += getDirectionalLighting();
    v_FinalColor     = vec4(color, 0.0);
    gl_Position      = u_Matrix * a_Position;
}

vec3 getAmbientLighting()
{
    return materialColor * 0.3;
}

vec3 getDirectionalLighting()
{
    vec3 lightVector = normalize(u_LightVector);
    vec3 normalVector = normalize(a_Normal);
    return materialColor  * 0.8 *
            max(dot(normalVector, lightVector), 0.0);
}
