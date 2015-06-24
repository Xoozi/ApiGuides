#version 300 es
precision mediump float;

uniform sampler2D   u_TextureUnit;

in vec3    v_Color;
in float   v_ElapsedTime;

out vec4 gl_FragColor;
void main()
{
    gl_FragColor = vec4(v_Color / (v_ElapsedTime + 0.0000000001), 1.0) * 
                    texture(u_TextureUnit, gl_PointCoord);
}
