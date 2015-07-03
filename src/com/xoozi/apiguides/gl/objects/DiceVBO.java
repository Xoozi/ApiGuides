package com.xoozi.apiguides.gl.objects;


import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.glDrawElements;

import com.xoozi.apiguides.gl.data.IndexBuffer;
import com.xoozi.apiguides.gl.data.VertexBuffer;
import com.xoozi.apiguides.gl.programs.ShaderProgram;

public class DiceVBO {
    private static final int POSITION_COMPONENT_COUNT = 3;

    private final float[] _colors = new float[] { 0, 0, 0, //v0
            0, 0, 1, //v1
            0, 1, 0, //v2
            0, 1, 1, //v3

            1, 0, 0, //v4
            1, 0, 1, //v5
            1, 1, 0, //v6
            1, 1, 1 //v7
    };
    private final short[] _indices = new short[] { 0, 3, 1, 3, 2, 1, 7, 4, 6,
            4, 5, 6, 4, 0, 5, 0, 1, 5, 3, 7, 2, 7, 6, 2, 1, 2, 5, 2, 6, 5, 3,
            0, 7, 0, 4, 7 };
    private final float[] _vertices;

    private final VertexBuffer  _vertexBuffer;
    private final VertexBuffer  _colorBuffer;
    private final IndexBuffer   _indexBuffer;


    public DiceVBO(float size){
        _vertices = new float[] { 
            -size, -size, size, //v0
            -size, size, size, //v1
            size, size, size, //v2
            size, -size, size, //v3

            -size, -size, -size, //v4
            -size, size, -size, //v5
            size, size, -size, //v6
            size, -size, -size, //v7
    };
        _vertexBuffer = new VertexBuffer(_vertices);

        _colorBuffer  = new VertexBuffer(_colors);

        _indexBuffer  = new IndexBuffer(_indices);
    }

    public void bindData(ShaderProgram colorProgram){
        _vertexBuffer.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                                            POSITION_COMPONENT_COUNT, 0);
        _colorBuffer.setVertexAttribPointer(0, colorProgram.getColorAttributeLocation(),
                                            POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _indexBuffer.getBufferId());
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
