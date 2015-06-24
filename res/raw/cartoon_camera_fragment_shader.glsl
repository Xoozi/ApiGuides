#version 300 es
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_TextureUnit;
in     vec2        v_TextureCoordinates;
out vec4 fFragColor;
void main( )
{
    float uMagTol = 4.;
    float uQuantize = 1.01;
    ivec2 ires  = textureSize(u_TextureUnit, 0);
    float ResS  = float(ires.s);
    float ResT  = float(ires.t);

    vec3 rgb    = texture(u_TextureUnit, v_TextureCoordinates).rgb;
    vec2 stp0   = vec2(1./ResS, 0.);
    vec2 st0p   = vec2(0., 1./ResT);
    vec2 stpp   = vec2(1./ResS, 1./ResT);
    vec2 stpm   = vec2(1./ResS, -1./ResT);

    //const vec3 w= vec3(0.2125, 0.7154, 0.0721);
    const vec3 w= vec3(0.5, 0.5, 0.5);
    float i00   = dot(texture(u_TextureUnit, v_TextureCoordinates).rgb, w);
    float im1m1 = dot(texture(u_TextureUnit, v_TextureCoordinates - stpp).rgb, w);
    float ip1p1 = dot(texture(u_TextureUnit, v_TextureCoordinates + stpp).rgb, w);
    float im1p1 = dot(texture(u_TextureUnit, v_TextureCoordinates - stpm).rgb, w);
    float ip1m1 = dot(texture(u_TextureUnit, v_TextureCoordinates + stpm).rgb, w);
    float im10  = dot(texture(u_TextureUnit, v_TextureCoordinates - stp0).rgb, w);
    float ip10  = dot(texture(u_TextureUnit, v_TextureCoordinates + stp0).rgb, w);
    float i0m1  = dot(texture(u_TextureUnit, v_TextureCoordinates - st0p).rgb, w);
    float i0p1  = dot(texture(u_TextureUnit, v_TextureCoordinates + st0p).rgb, w);


    float h= -1.*im1p1-2.*i0p1-1.*ip1p1+1.*im1m1+2.*i0m1+1.*ip1m1;
    float v= -1.*im1m1-2.*im10-1.*im1p1+1.*ip1m1+2.*ip10+1.*ip1p1;
    float mag = length( vec2( h, v ) );	

	
    if(mag > uMagTol){
        fFragColor = vec4( 0., 0., 0., 1. );
    }else{	
        rgb.rgb *= uQuantize;
        rgb.rgb += vec3( .5, .5, .5 );
        ivec3 intrgb = ivec3( rgb.rgb );
        rgb.rgb = vec3( intrgb ) / uQuantize;
        fFragColor = vec4( rgb, 1. );
    }
}
