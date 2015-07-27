package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glUniform3f;

import com.xoozi.apiguides.R;

/**
 * 逐顶点环境光照着色程序
 */
public class CB_5_PerV_Ambient_ShaderProgram extends ShaderProgram {

    private static final String U_MATERIAL_AMBIENT = "u_MaterialAmbient";
    private static final String U_LIGHT_AMBIENT    = "u_LightAmbient";

    private final int _uMatrixLocation;
    private final int _uMaterialLocation;
    private final int _uLightLocation;

    private final int _aPositionLocation;

    public CB_5_PerV_Ambient_ShaderProgram(Context context){
        super(context, R.raw.cb_5_1_pv_ambient_vertex_shader, R.raw.cb_5_1_pv_ambient_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);
        _uMaterialLocation          = glGetUniformLocation(_program, U_MATERIAL_AMBIENT);
        _uLightLocation             = glGetUniformLocation(_program, U_LIGHT_AMBIENT);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
    }

    public void setMatrix(float []matrix){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);
    }

    public void setMaterial(float r, float g, float b){
        glUniform3f(_uMaterialLocation, r, g, b);
    }

    public void setLight(float r, float g, float b){
        glUniform3f(_uLightLocation, r, g, b);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }
}
