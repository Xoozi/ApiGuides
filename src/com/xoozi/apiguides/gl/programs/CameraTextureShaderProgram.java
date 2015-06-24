package com.xoozi.apiguides.gl.programs;

import android.content.Context;
import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glUniformMatrix4fv;

import com.xoozi.apiguides.R;

/**
 * 相机纹理着色程序
 * android的OpenGL可以用 samplerExternalOES 而非 sampler2D
 * 来从相机驱动直接获取纹理单元, 所以就不用我们自己传递了
 * 只需要传递顶点坐标和纹理坐标
 */
public class CameraTextureShaderProgram extends ShaderProgram {

    private final int _uMatrixLocation;

    private final int _aPositionLocation;
    private final int _aTextureCoordinatesLocation;

    private final int _textureId;

    public CameraTextureShaderProgram(Context context, int textureId){
        super(context, R.raw.camera_vertex_shader, R.raw.cartoon_camera_fragment_shader);

        _uMatrixLocation            = glGetUniformLocation(_program, U_MATRIX);

        _aPositionLocation          = glGetAttribLocation(_program, A_POSITION);
        _aTextureCoordinatesLocation= glGetAttribLocation(_program, A_TEXTURE_COORDINATES);

        _textureId = textureId;
    }

    /**
     * 设置必要的Uniform
     */
    public void setUniforms(float []matrix){
        glUniformMatrix4fv(_uMatrixLocation, 1, false, matrix, 0);

        /**
         * 设置0号纹理单元为活动的纹理单元
         */
        glActiveTexture(GL_TEXTURE0);

        /**
         * 绑定纹理到 当前活动的纹理单元
         * 这里绑定特殊的单元
         */
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, _textureId);
    }
    public int getPositionAttributeLocation(){
        return _aPositionLocation;
    }

    public int  getTextureCoordinatesAttributeLocation(){
        return _aTextureCoordinatesLocation;
    }
}







