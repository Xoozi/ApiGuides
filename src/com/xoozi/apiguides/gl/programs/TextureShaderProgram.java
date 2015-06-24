package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;

import com.xoozi.apiguides.R;

/**
 * 纹理着色程序
 */
public class TextureShaderProgram extends ShaderProgram{

    private final   int _uMatrixLocation;
    private final   int _uTextureUnitLocation;

    private final   int _aPositionLocation;
    private final   int _aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context){
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);
        _uTextureUnitLocation       = glGetUniformLocation(_program, U_TEXTURE_UNIT);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
        _aTextureCoordinatesLocation= glGetAttribLocation(_program, A_TEXTURE_COORDINATES);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float []matrix, int textureId){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);

        /**
         * 设置0号纹理单元为活动的纹理单元
         */
        glActiveTexture(GL_TEXTURE0);

        /**
         * 绑定纹理到 当前活动的纹理单元
         */
        glBindTexture(GL_TEXTURE_2D, textureId);

        /**
         * 设置纹理单元Uniform值为0号
         */
        glUniform1i(_uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }

    public int  getTextureCoordinatesAttributeLocation(){
        return _aTextureCoordinatesLocation;
    }
}
