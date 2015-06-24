package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniform1f;

import com.xoozi.apiguides.R;

/**
 * 色彩着色程序
 */
public class CB_1_ShaderProgram extends ShaderProgram {

    private static final String U_RADIANANGLE       = "u_RadianAngle";

    private final int _uRadianAngleLocation;
    private final int _aColorLocation;
    private final int _aPositionLocation;

    public CB_1_ShaderProgram(Context context){
        super(context, R.raw.cb_1_vertex_shader, R.raw.cb_1_fragment_shader);

        _uRadianAngleLocation       = glGetUniformLocation(_program, U_RADIANANGLE);

        _aColorLocation             = glGetAttribLocation(_program, A_COLOR);
        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float radianAngle){
        glUniform1f(_uRadianAngleLocation,radianAngle);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return _aColorLocation;
    }
}
