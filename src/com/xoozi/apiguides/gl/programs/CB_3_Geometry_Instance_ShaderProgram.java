package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;

import com.xoozi.apiguides.R;

public class CB_3_Geometry_Instance_ShaderProgram extends ShaderProgram {

    private final int _uMatrixLocation;
    private final int _aMatrixLocation;
    private final int _aColorLocation;
    private final int _aPositionLocation;


    public CB_3_Geometry_Instance_ShaderProgram(Context context){
        super(context, R.raw.geometry_instance_vertex_shader, R.raw.geometry_instance_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);
        _aMatrixLocation            = glGetAttribLocation(_program, A_MATRIX);
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

    public int getMatrixAttributeLocation(){
        return _aMatrixLocation;
    }
}
