package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniform1i;

import com.xoozi.apiguides.R;

/**
 * 逐顶点漫反射光照着色程序
 */
public class CB_5_Perv_Diffuse_ShaderProgram extends ShaderProgram {

    private static final String U_MATERIAL_AMBIENT      = "u_MaterialAmbient";
    private static final String U_LIGHT_AMBIENT         = "u_LightAmbient";
    private static final String U_LIGHT_VECTOR          = "u_LightVector";
    private static final String U_LIGHT_MODE            = "u_LightMode";
    private static final String U_LIGHT_POSITION        = "u_LightPosition";

    private static final String A_NORMAL = "a_Normal";

    private final int _u_MVP_MatrixLocation;
    private final int _uMaterialLocation;
    private final int _uLightAmbientLocation;
    private final int _uLightVectorLocation;
    private final int _uLightModeLocation;
    private final int _uLightPositionLocation;

    private final int _aPositionLocation;
    private final int _aNormalLocation;

    public CB_5_Perv_Diffuse_ShaderProgram(Context context){
        super(context, R.raw.cb_5_2_pv_diffuse_vertex_shader, R.raw.cb_5_2_pv_diffuse_fragment_shader);

        _u_MVP_MatrixLocation       = glGetUniformLocation(_program, U_MATRIX);
        _uMaterialLocation          = glGetUniformLocation(_program, U_MATERIAL_AMBIENT);
        _uLightAmbientLocation      = glGetUniformLocation(_program, U_LIGHT_AMBIENT);
        _uLightVectorLocation       = glGetUniformLocation(_program, U_LIGHT_VECTOR);
        _uLightModeLocation         = glGetUniformLocation(_program, U_LIGHT_MODE);
        _uLightPositionLocation     = glGetUniformLocation(_program, U_LIGHT_POSITION);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
        _aNormalLocation            = glGetAttribLocation(_program, A_NORMAL);
    }

    public void setMatrix(float []mvpMatrix){
        glUniformMatrix4fv(_u_MVP_MatrixLocation, 1, false, mvpMatrix, 0);
    }

    public void setMaterial(float r, float g, float b){
        glUniform3f(_uMaterialLocation, r, g, b);
    }

    public void setLightAmbient(float r, float g, float b){
        glUniform3f(_uLightAmbientLocation, r, g, b);
    }

    public void setLightVector(float x, float y, float z){
        glUniform3f(_uLightVectorLocation, x, y, z);
    }

    public void setLightPosition(float x, float y, float z){
        glUniform3f(_uLightPositionLocation, x, y, z);
    }

    public void setLightMode(int mode){
        glUniform1i(_uLightModeLocation, mode);
    }


    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }
    public int getNormalAttributeLocation(){
        return _aNormalLocation;
    }
}
