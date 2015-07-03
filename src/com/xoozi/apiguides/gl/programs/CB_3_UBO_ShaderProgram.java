package com.xoozi.apiguides.gl.programs;

import android.content.Context;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glUniform1i;


import com.xoozi.apiguides.R;
import com.xoozi.apiguides.gl.data.TransformationBlock;

/**
 * 色彩着色程序
 */
public class CB_3_UBO_ShaderProgram extends ShaderProgram {



    private final  String               U_TRANSFORMATION_FLAG = "u_TransformationFlag";

    private final  String               U_MODEL_MATRIX      = "u_ModelMatrix";
    private final  String               U_VIEW_MATRIX       = "u_ViewMatrix";
    private final  String               U_ROTATION_MATRIX   = "u_RotationMatrix";
    private final  String               U_PROJECTION_MATRIX = "u_ProjectionMatrix";

    private final int                   _uTransformationFlagLocation;
    private final int                   _uModelMatrixLocation;
    private final int                   _uViewMatrixLocation;
    private final int                   _uRotationMatrixLocation;
    private final int                   _uProjectionMatrixLocation;

    private final int                   _aColorLocation;
    private final int                   _aPositionLocation;
    private final TransformationBlock   _tb;

    public CB_3_UBO_ShaderProgram(Context context){
        super(context, R.raw.cb_3_ubo_vertex_shader, R.raw.cb_3_ubo_fragment_shader);

        _uTransformationFlagLocation= glGetUniformLocation(_program, U_TRANSFORMATION_FLAG);

        _uModelMatrixLocation       = glGetUniformLocation(_program, U_MODEL_MATRIX);
        _uViewMatrixLocation        = glGetUniformLocation(_program, U_VIEW_MATRIX);
        _uRotationMatrixLocation    = glGetUniformLocation(_program, U_ROTATION_MATRIX);
        _uProjectionMatrixLocation  = glGetUniformLocation(_program, U_PROJECTION_MATRIX);

        _aColorLocation             = glGetAttribLocation(_program, A_COLOR);
        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
        _tb                         = new TransformationBlock(this);
    }


    /**
     * 设置变换矩阵们
     */
    public void updateMatrices(float[] modelMatrix,
                              float[] viewMatrix,
                              float[] rotationMatrix,
                              float[] projectionMatrix){
        _tb.updateMatrices(modelMatrix, viewMatrix, rotationMatrix, projectionMatrix);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float[] modelMatrix,
                            float[] viewMatrix,
                            float[] rotationMatrix,
                            float[] projectionMatrix){
        glUniformMatrix4fv(_uModelMatrixLocation, 1, false, modelMatrix, 0);
        glUniformMatrix4fv(_uViewMatrixLocation, 1, false, viewMatrix, 0);
        glUniformMatrix4fv(_uRotationMatrixLocation, 1, false, rotationMatrix, 0);
        glUniformMatrix4fv(_uProjectionMatrixLocation, 1, false, projectionMatrix, 0);
    }


    public void setTransformationFlag(int flag){
        glUniform1i(_uTransformationFlagLocation, flag);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return _aColorLocation;
    }
}
