#version 300 es
layout(std140) uniform Transformation{
    mat4 ModelMatrix;
    mat4 ViewMatrix;
    mat4 RotationMatrix;
    mat4 ProjectionMatrix;
}wtf;

uniform     mat4    u_ModelMatrix;
uniform     mat4    u_ViewMatrix;
uniform     mat4    u_RotationMatrix;
uniform     mat4    u_ProjectionMatrix;
uniform     int     u_TransformationFlag;

in   vec4    a_Position;
in   vec4    a_Color;

out         vec4    v_Color;

void main()
{
    if(0 == u_TransformationFlag){
        gl_Position = u_ProjectionMatrix * u_RotationMatrix * u_ViewMatrix * u_ModelMatrix * a_Position;
    }else{
        gl_Position = wtf.ProjectionMatrix * wtf.RotationMatrix * wtf.ViewMatrix * wtf.ModelMatrix * a_Position;
    }
    gl_PointSize = 20.0;
    v_Color = a_Color;
}
