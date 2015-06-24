#version 300 es
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_TextureUnit;
in     vec2        v_TextureCoordinates;
out vec4 outColor;

void main()
{
    ivec2 vecSize = textureSize(u_TextureUnit, 0);
    float ResS = float(vecSize.s);
    float ResT = float(vecSize.t);
    vec3 irgb = texture(u_TextureUnit, v_TextureCoordinates).rgb;
    
    vec2 stpp = vec2(-1.0,-1.0);
    vec3 c00 = texture(u_TextureUnit, v_TextureCoordinates).rgb;
    vec3 cp1p1 = texture(u_TextureUnit, v_TextureCoordinates+stpp).rgb;
    
    vec3 diffs = c00 - cp1p1;
    float maxV = diffs.r;
    if(abs(diffs.g) > abs(maxV)) maxV = diffs.g;
    if(abs(diffs.b) > abs(maxV)) maxV = diffs.b;
    
    float gray = clamp(maxV + 0.5,0.0,1.0);
    outColor = vec4(gray,gray,gray,1.0);
}
