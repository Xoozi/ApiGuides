package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;

import com.xoozi.apiguides.R;

/**
 * 高度图着色程序
 */
public class HeightmapShaderProgram extends ShaderProgram {

    private final int _uMatrixLocation;

    private final int _aPositionLocation;

    public HeightmapShaderProgram(Context context){
        super(context, R.raw.heightmap_vertex_shader, R.raw.heightmap_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float []matrix){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }
}
