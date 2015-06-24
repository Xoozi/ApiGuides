package com.xoozi.apiguides.gl.data;

import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;

import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBufferData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class IndexBuffer {

    private final int _bufferId;

    public IndexBuffer(short[] vertexData){
        final int buffers[] = new int[1];

        /**
         * 创建VBO对象
         */
        glGenBuffers(buffers.length, buffers, 0);

        if(0 == buffers[0]){
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        _bufferId = buffers[0];
        
        /**
         * 将VBO绑定到隐含的管线
         * GL_ELEMENT_ARRAY_BUFFER 代表VBO中存放的是索引数据
         */
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _bufferId);

        ShortBuffer vertexArray = ByteBuffer
            .allocateDirect(vertexData.length * Constants.BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(vertexData);

        vertexArray.position(0);

        /**
         * 在显存中分配VBO所需的空间,并用Native堆中的数据初始化它
         * GL_ELEMENT_ARRAY_BUFFER 代表VBO中存放的是索引数据
         */
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_SHORT,
                vertexArray, GL_STATIC_DRAW);

        /**
         * 重要!!! 完成后要解除绑定
         */
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getBufferId(){
        return _bufferId;
    }

}
