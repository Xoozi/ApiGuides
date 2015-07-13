package com.xoozi.apiguides.gl.objects;


import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.glDrawElements;

import com.xoozi.apiguides.gl.data.VertexArrayObject;
import com.xoozi.apiguides.gl.programs.ShaderProgram;

public class DiceVAO {

    private final short[]       _indices = new short[] { 0, 3, 1, 3, 2, 1, 7, 4, 6,
            4, 5, 6, 4, 0, 5, 0, 1, 5, 3, 7, 2, 7, 6, 2, 1, 2, 5, 2, 6, 5, 3,
            0, 7, 0, 4, 7 };
    private final float[]       _vertices;

    private VertexArrayObject   _vao;
    private final int           _indexNum;

    public DiceVAO(ShaderProgram program, float size){
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

        _vao = new VertexArrayObject(program, _vertices, _indices);
        _indexNum = _indices.length;
    }

    public void draw(){
        _vao.bindData();
        glDrawElements(GL_TRIANGLES, _indexNum, GL_UNSIGNED_SHORT, 0);
    }
}
