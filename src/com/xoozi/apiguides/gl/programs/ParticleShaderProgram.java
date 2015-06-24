package com.xoozi.apiguides.gl.programs;

import android.content.Context;

import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glUniform1f;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;

import com.xoozi.apiguides.R;

/**
 * 粒子着色程序
 */
public class ParticleShaderProgram extends ShaderProgram {

    private final int _uMatrixLocation;
    private final int _uTimeLocation;
    private final int _uTextureUnitLocation;

    private final int _aPositionLocation;
    private final int _aColorLocation;
    private final int _aDirectionVectorLocation;
    private final int _aParticleStartTimeLocation;

    public ParticleShaderProgram(Context context){
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);
        _uTimeLocation              = glGetUniformLocation(_program, U_TIME);
        _uTextureUnitLocation       = glGetUniformLocation(_program, U_TEXTURE_UNIT);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
        _aColorLocation             = glGetAttribLocation(_program, A_COLOR);
        _aDirectionVectorLocation   = glGetAttribLocation(_program, A_DIRECTION_VECTOR);
        _aParticleStartTimeLocation = glGetAttribLocation(_program, A_PARTICLE_START_TIME);
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float []matrix, float elapsedTime, int textureId){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(_uTimeLocation, elapsedTime);

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

    public int getColorAttributeLocation(){
        return _aColorLocation;
    }

    public int getDirectionVectorAttributeLocation(){
        return _aDirectionVectorLocation;
    }


    public int getParticleStartTimeAttributeLocation(){
        return _aParticleStartTimeLocation;
    }
}
