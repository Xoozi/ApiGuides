package com.xoozi.apiguides.gl.objects;

import com.xoozi.apiguides.gl.data.Constants;
import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.programs.TextureShaderProgram;

import static android.opengl.GLES30.GL_TRIANGLE_FAN;
import static android.opengl.GLES30.glDrawArrays;

/**
 * 封装桌子的顶点坐标，以及纹理坐标
 */
public class Table{
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT 
                            + TEXTURE_COORDINATES_COMPONENT_COUNT)
                            * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {

        //X, Y, S, T
        0f,     0f,     0.5f,   0.5f,
        -0.5f,  -0.8f,  0f,     0.9f,
        0.5f,   -0.8f,  1f,     0.9f,
        0.5f,   0.8f,   1f,     0.1f,
        -0.5f,  0.8f,   0f,     0.1f,
        -0.5f,  -0.8f,  0f,     0.9f
    };

    private final   VertexArray _vertexArray;

    public Table(){
        _vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram){
        _vertexArray.setVertexAttributePointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        _vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
