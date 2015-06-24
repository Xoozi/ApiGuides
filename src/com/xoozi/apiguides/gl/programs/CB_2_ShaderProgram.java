package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;

import com.xoozi.apiguides.R;

/**
 * 色彩着色程序
 */
public class CB_2_ShaderProgram extends ShaderProgram {

    private final int _uMatrixLocation;
    private final int _aColorLocation;
    private final int _aPositionLocation;

    public CB_2_ShaderProgram(Context context){
        super(context, R.raw.cb_2_vertex_shader, R.raw.cb_2_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);
        _aColorLocation             = glGetAttribLocation(_program, A_COLOR);
        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float[] matrix){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return _aColorLocation;
    }
}
