package com.xoozi.apiguides.gl;

import com.xoozi.apiguides.gl.data.Constants;
import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.programs.CameraTextureShaderProgram;

import android.content.Context;

import static android.opengl.Matrix.setIdentityM;
import static android.opengl.GLES30.GL_TRIANGLE_FAN;
import static android.opengl.GLES30.glDrawArrays;

/**
 * 相机预览渲染
 */
public class CameraRenderer {

    private CameraTextureShaderProgram _cameraProgram;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + 
            TEXTURE_COORDINATES_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT;

    /**
     * 注意要把纹理坐标翻转一下，不然预览图片是镜像
     */
    private static final float[] VERTEX_DATA = {
            //X, Y, S, T
            0f, 0f,     0.5f, 0.5f, 
            -1f, -1f,   1f, 1f, 
            1f, -1f,    1f, 0f, 
            1f, 1f,     0f,0f, 
            -1f, 1f,    0f, 1f, 
            -1f, -1f,   1f, 1f };

    private final VertexArray _vertexArray;
    private final float[] _matrix = new float[16];

    public CameraRenderer(Context context, int textureId){
        _vertexArray = new VertexArray(VERTEX_DATA);
        _cameraProgram = new CameraTextureShaderProgram(context, textureId);
        setIdentityM(_matrix, 0);
    }

    public void draw(){

        _cameraProgram.useProgram();
        _cameraProgram.setUniforms(_matrix);
        _bindData();
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

    private void _bindData(){
        _vertexArray.setVertexAttributePointer(
                0,
                _cameraProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        _vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                _cameraProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }
}
