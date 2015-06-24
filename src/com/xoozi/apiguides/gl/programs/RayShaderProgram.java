package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glUniform4f;

import com.xoozi.apiguides.R;

/**
 * 射线着色程序
 */
public class RayShaderProgram extends ShaderProgram {

    private final int _uMatrixLocation;
    private final int _uColorLocation;

    private final int _aPositionLocation;

    public RayShaderProgram(Context context){
        super(context, R.raw.ray_vertex_shader, R.raw.ray_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);
        _uColorLocation             = glGetUniformLocation(_program, U_COLOR);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float []matrix, float a, float r, float g, float b){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(_uColorLocation, r, g, b, a);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }
}
