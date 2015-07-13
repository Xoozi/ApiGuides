package com.xoozi.apiguides.gl.data;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.glGenVertexArrays;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glEnableVertexAttribArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.xoozi.apiguides.gl.programs.ShaderProgram;

/**
 * 对OpenGLES 的Vertex Array Object的封装
 * 优点是一次性把VBO和IBO都绑定到VAO上
 * 绘制时,只需要绑定VAO然后绘制就行了
 * 简化了流程
 * 代码更干净了
 */
public class VertexArrayObject {

    private static final int POSITION_COMPONENT_COUNT = 3;

    private final int _vaoId;

    public VertexArrayObject(ShaderProgram program, float[] vertexData, short[] indexData){
        final int vertexBufObjs[]  = new int[1];
        final int indexBufObjs[]   = new int[1];
        final int vertexArrayObjs[]= new int[1];
        int vboId;
        int iboId;
        FloatBuffer vertexArray;
        ShortBuffer indexArray;

        /**
         * 创建VBO对象, 对象句柄由数组参数返回
         * 将VBO绑定到隐含的管线
         * 在显存中分配VBO所需的空间,并用Native堆中的数据初始化它
         */
        glGenBuffers(vertexBufObjs.length, vertexBufObjs, 0);
        if(0 == vertexBufObjs[0]){
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        vboId = vertexBufObjs[0];
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        vertexArray = ByteBuffer
            .allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData);
        vertexArray.position(0);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * Constants.BYTES_PER_FLOAT,
                vertexArray, GL_STATIC_DRAW);



        /**
         * 创建IBO对象, 对象句柄由数组参数返回
         * 将IBO绑定到隐含的管线
         * 在显存中分配IBO所需的空间,并用Native堆中的数据初始化它
         */
        glGenBuffers(indexBufObjs.length, indexBufObjs, 0);
        if(0 == indexBufObjs[0]){
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        iboId = indexBufObjs[0];
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        indexArray = ByteBuffer
            .allocateDirect(indexData.length * Constants.BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indexData);
        indexArray.position(0);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity() * Constants.BYTES_PER_SHORT,
                indexArray, GL_STATIC_DRAW);


        /**
         * 创建VAO对象
         * 绑定VAO
         * 像VBO bindData一样 再绑定一次vboId
         * 调用glVertexAttribPointer的重载版本将显存中的VBO传给着色器
         * 启用着色器输入变量
         * 再一次绑定IBO 完成VAO的初始化
         */
        glGenVertexArrays(vertexArrayObjs.length, vertexArrayObjs, 0);
        _vaoId = vertexArrayObjs[0];
        glBindVertexArray(_vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glVertexAttribPointer(program.getPositionAttributeLocation(), 
                POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, 0);
        glEnableVertexAttribArray(program.getPositionAttributeLocation());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);


        /**
         * 解除VAO到隐含管线的绑定
         * 解除VBO到隐含管线的绑定
         * 解除IBO到隐含管线的绑定
         */
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void bindData(){
        glBindVertexArray(_vaoId);
    }
}
