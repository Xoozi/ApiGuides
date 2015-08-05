#version 300 es
uniform     int     u_LightMode;
uniform     mat4    u_Matrix;
uniform     vec3    u_MaterialAmbient;
uniform     vec3    u_LightAmbient;
uniform     vec3    u_LightVector;
uniform     vec3    u_LightPosition;
in          vec4    a_Position;
in          vec3    a_Normal;


out         vec4    v_FinalColor;


vec3        materialColor;
vec3        getAmbientLighting();
vec3        getDirectionalLighting();
vec3        getPointLighting();
void        getLightMode();

int flag0, flag1, flag2, flag3;

void main()
{
    materialColor    = u_MaterialAmbient * u_LightAmbient;

    getLightMode();
    vec3    color    = vec3(0.0, 0.0, 0.0);
    if(1 == flag0)
        color           += getAmbientLighting();
    if(1 == flag1)
        color           += getDirectionalLighting();
    if(1 == flag2)
        color           += getPointLighting();
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

vec3 getPointLighting()
{
    
    vec3 toPointLight = u_LightPosition
                            - a_Position;
    float distan = length(toPointLight);
    toPointLight = normalize(toPointLight);
    vec3 normalVector = normalize(a_Normal);
    return materialColor  * 0.8*
            max(dot(normalVector, toPointLight), 0.0);
}

void getLightMode()
{
    int b1 = 16;
    int b2 = b1 * 16;
    int b3 = b2 * 16;
    flag3  = u_LightMode / b3;
    flag2  = (u_LightMode - b3*flag3)/b2;
    flag1  = (u_LightMode - b3*flag3 - b2*flag2)/b1;
    flag0  = (u_LightMode - b3*flag3 - b2*flag2 - b1*flag1);
}
