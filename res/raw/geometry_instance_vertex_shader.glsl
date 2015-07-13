#version 300 es
uniform     mat4    u_Matrix;

in vec4 a_Position;
//in vec4 a_Color;
in mat4 a_Matrix;

out vec4 v_Color;

void main() 
{
    gl_Position = u_Matrix * a_Position;
/*
    if(0 == gl_InstanceID){
        gl_Position = u_Matrix * a_Position;
    }else{
        gl_Position = a_Matrix * a_Position;
    }*/
  
  
    //v_Color     = a_Color;

    v_Color = mix(vec3(0.180, 0.467, 0.153),
                  vec3(0.660, 0.670, 0.680),
                  gl_Position.y);
}
