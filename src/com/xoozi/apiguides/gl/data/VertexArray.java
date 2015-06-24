package com.xoozi.apiguides.gl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES30.GL_FLOAT;

import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glEnableVertexAttribArray;

/**
 * 定点数组
 * 封装将数据从Dalvik虚拟机的堆拷贝到原生堆
 * 以及将定点数据设置到着色器的属性
 */
public class VertexArray{
    private final FloatBuffer   _floatBuffer;

    /**
     * 初始化FloatBuffer
     * 数据放入原生堆
     */
    public VertexArray(float[] vertextData){
        _floatBuffer = ByteBuffer
            .allocateDirect(vertextData.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertextData);
    }

    /**
     * 将原生堆中的数据设置到指定的属性地址
     */
    public void setVertexAttributePointer(int dataOffset,
                                          int attributeLocation,
                                          int componentCount,
                                          int stride){

        _floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT,
                false, stride, _floatBuffer);
        glEnableVertexAttribArray(attributeLocation);        
    }

    /**
     * 更新部分内容
     */
    public void updateBuffer(float[] vertexData, int start, int count){
        _floatBuffer.position(start);
        _floatBuffer.put(vertexData, start, count);
        _floatBuffer.position(0);
    }
}
