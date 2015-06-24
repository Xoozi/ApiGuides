package com.xoozi.apiguides.gl.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glEnableVertexAttribArray;
//import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.glDrawElements;

import com.xoozi.apiguides.gl.data.Constants;
import com.xoozi.apiguides.gl.programs.CB_2_ShaderProgram;

public class DiceIndex {
    private static final int POSITION_COMPONENT_COUNT = 3;


    private final float[]         _vertices = new float[]{
            -0.5f,  -0.5f,  0.5f,   //v0
            -0.5f,  0.5f,   0.5f,   //v1
            0.5f,   0.5f,   0.5f,   //v2
            0.5f,   -0.5f,  0.5f,   //v3

            -0.5f,  -0.5f,  -0.5f,  //v4
            -0.5f,  0.5f,   -0.5f,  //v5
            0.5f,   0.5f,   -0.5f,  //v6
            0.5f,   -0.5f,  -0.5f,  //v7
        };
    private final float[]         _colors = new float[]{
            0,      0,      0,   //v0
            0,      0,      1,   //v1
            0,      1,      0,   //v2
            0,      1,      1,   //v3

            1,      0,      0,   //v4
            1,      0,      1,   //v5
            1,      1,      0,   //v6
            1,      1,      1    //v7
        };
    private final short[]         _indices = new short[]{
            0,3,1, 3,2,1,
            7,4,6, 4,5,6,
            4,0,5, 0,1,5,
            3,7,2, 7,6,2,
            1,2,5, 2,6,5,
            3,0,7, 0,4,7
        };
    private final FloatBuffer     _vertexBuffer;
    private final FloatBuffer     _colorBuffer;
    private final ShortBuffer     _indexBuffer;

    public DiceIndex(float size){
        _vertexBuffer = ByteBuffer
            .allocateDirect(_vertices.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(_vertices);

        _colorBuffer = ByteBuffer
            .allocateDirect(_colors.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(_colors);

        _indexBuffer = ByteBuffer
            .allocateDirect(_indices.length * Constants.BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(_indices);

    }

    public void bindData(CB_2_ShaderProgram colorProgram){
        _vertexBuffer.position(0);
        glVertexAttribPointer(
                colorProgram.getPositionAttributeLocation(), 
                POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, _vertexBuffer);
        glEnableVertexAttribArray(colorProgram.getPositionAttributeLocation());

        _colorBuffer.position(0);
        glVertexAttribPointer(
                colorProgram.getColorAttributeLocation(), 
                POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, _colorBuffer);
        glEnableVertexAttribArray(colorProgram.getColorAttributeLocation());
    }

    public void draw(){
        _indexBuffer.position(0);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, _indexBuffer);
    }
}
