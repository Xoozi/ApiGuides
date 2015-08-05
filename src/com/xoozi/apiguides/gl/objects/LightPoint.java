package com.xoozi.apiguides.gl.objects;


import static android.opengl.GLES30.GL_POINTS;
import static android.opengl.GLES30.glDrawArrays;

import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.programs.ShaderProgram;

public class LightPoint {
    private static final int POSITION_COMPONENT_COUNT = 3;

    private final VertexArray _vertexArray;
    private final float[]     _vertex = new float[3];

    public LightPoint(float x, float y, float z){
        _vertex[0] = x;
        _vertex[1] = y;
        _vertex[2] = z;
        _vertexArray = new VertexArray(_vertex);
    }

    public void bindData(ShaderProgram program){
        _vertexArray.setVertexAttributePointer(0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        glDrawArrays(GL_POINTS,0, 1);
    }

    public float getX(){
        return _vertex[0];
    }

    public float getY(){
        return _vertex[1];
    }

    public float getZ(){
        return _vertex[2];
    }

    public void setX(float x){
        _vertex[0] = x;
    }

    public void setY(float y){
        _vertex[1] = y;
    }

    public void setZ(float z){
        _vertex[2] = z;
    }

    public void update(){
        _vertexArray.updateBuffer(_vertex, 0, 3);
    }
}
