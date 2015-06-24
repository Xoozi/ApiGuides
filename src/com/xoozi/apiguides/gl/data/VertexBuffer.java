package com.xoozi.apiguides.gl.data;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_FLOAT;

import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glEnableVertexAttribArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 对OpenGLES 的Vertex Buffer Object的封装
 */
public class VertexBuffer{

    private final int _bufferId;

    public VertexBuffer(float[] vertexData){
        final int buffers[] = new int[1];

        /**
         * 创建VBO对象, 对象句柄由数组参数返回
         */
        glGenBuffers(buffers.length, buffers, 0);

        if(0 == buffers[0]){
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        _bufferId = buffers[0];
        
        /**
         * 将VBO绑定到隐含的管线
         * GL_ARRAY_BUFFER 代表VBO中存放的是顶点位置或颜色数据
         */
        glBindBuffer(GL_ARRAY_BUFFER, _bufferId);

        FloatBuffer vertexArray = ByteBuffer
            .allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData);

        vertexArray.position(0);

        /**
         * 在显存中分配VBO所需的空间,并用Native堆中的数据初始化它
         * GL_ARRAY_BUFFER 代表VBO中存放的是顶点位置或颜色数据
         */
        glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT,
                vertexArray, GL_STATIC_DRAW);

        /**
         * 重要!! 完成后要解除VBO到隐含管线的绑定
         */
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
            int componentCount, int stride){
        /**
         * 将VBO绑定到隐含管线
         */
        glBindBuffer(GL_ARRAY_BUFFER, _bufferId);

        /**
         * 调用glVertexAttribPointer的重载版本
         * 将显存中的VBO传给着色器
         */
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT,
                false, stride, dataOffset);

        /**
         * 启用着色器输入变量
         */
        glEnableVertexAttribArray(attributeLocation);

        /**
         * 重要!! 完成后解除VBO到隐含管线的绑定
         */
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}
