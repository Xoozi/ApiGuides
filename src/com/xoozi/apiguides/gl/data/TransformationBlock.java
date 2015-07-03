package com.xoozi.apiguides.gl.data;

import static android.opengl.GLES30.GL_UNIFORM_BUFFER;
import static android.opengl.GLES30.GL_DYNAMIC_DRAW;
import static android.opengl.GLES30.GL_UNIFORM_BLOCK_DATA_SIZE;
import static android.opengl.GLES30.GL_INVALID_INDEX;
import static android.opengl.GLES30.GL_MAP_WRITE_BIT;
import static android.opengl.GLES30.GL_MAP_INVALIDATE_RANGE_BIT;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindBufferBase;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glGetUniformBlockIndex;
import static android.opengl.GLES30.glGetActiveUniformBlockiv;
import static android.opengl.GLES30.glUniformBlockBinding;
import static android.opengl.GLES30.glMapBufferRange;
import static android.opengl.GLES30.glUnmapBuffer;


import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.xoozi.apiguides.gl.programs.ShaderProgram;


/**
 * 对OpenGLES 的转换矩阵们的封装
 *   把Model, View, Rotation, Projection, 
 *   四个矩阵封装在UniformBlock里, 提高传输效率
 *   在Java代码里进行矩阵运算效率不高, 放到GPU去算吧
 */
public class TransformationBlock {
    private final static String BLOCK_NAME = "Transformation";

    private final int _blockIndex;
    private final int _blockBindingPoint;
    private final int _blockSize;
    private final int _bufferId;

    public TransformationBlock(ShaderProgram program){

        int programId = program.getProgram();

        /**
         * 创建UBO对象
         */
        _blockIndex = glGetUniformBlockIndex(programId,  BLOCK_NAME);
        if(GL_INVALID_INDEX == _blockIndex){
            throw new RuntimeException("Could not create a new uniform block object.");
        }


        /**
         * 查询uniform block 的大小
         */
        int[] sizeBuff = new int[1];
        glGetActiveUniformBlockiv(programId, _blockIndex,
                GL_UNIFORM_BLOCK_DATA_SIZE, sizeBuff, 0);
        _blockSize = sizeBuff[0];

        


        /**
         * 创建UBO对象, 对象句柄由数组参数返回
         * 绑定到隐含管线
         * 在显存中分配UBO的空间
         */
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        _bufferId = buffers[0];
        glBindBuffer(GL_UNIFORM_BUFFER, _bufferId);
        glBufferData(GL_UNIFORM_BUFFER, _blockSize, null, GL_DYNAMIC_DRAW);


        /**
         * 将Uniform block index绑定到 BindingPoint
         */
        _blockBindingPoint = 1;
        glUniformBlockBinding(programId, _blockIndex, _blockBindingPoint);
        glBindBufferBase(GL_UNIFORM_BUFFER, _blockBindingPoint, _bufferId);


        /**
         * 重要!! 完成后要解除UBO到隐含管线的绑定
         */
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    /**
     * 更新各个矩阵
     */
    public void updateMatrices(float[] modelMatrix, 
                                       float[] viewMatrix,
                                       float[] rotationMatrix,
                                       float[] projectionMatrix){
        /**
         * 将UBO绑定到隐含管线
         */
        glBindBuffer(GL_UNIFORM_BUFFER, _bufferId);



        /**
         * 映射显存到native堆
         */
        Buffer      buf = glMapBufferRange(GL_UNIFORM_BUFFER, 0, 
                                        _blockSize, 
                                        GL_MAP_WRITE_BIT | GL_MAP_INVALIDATE_RANGE_BIT);
        ByteBuffer  byteBuf = null;
        if (buf instanceof ByteBuffer) {
            byteBuf = (ByteBuffer)buf;
        }


        /**
         * 写native堆上的内存
         */

        _setMatrix(byteBuf, 48, modelMatrix);
        _setMatrix(byteBuf, 32, viewMatrix);
        _setMatrix(byteBuf, 16, rotationMatrix);
        _setMatrix(byteBuf, 0, projectionMatrix);

        /**
         * 解除并刷新显存映射
         */
        glUnmapBuffer(GL_UNIFORM_BUFFER);

        /**
         * 重要!! 完成后要解除UBO到隐含管线的绑定
         */
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    private void _setMatrix(ByteBuffer b, int index, float[] matrix){

        int startOffset = index;
        int strides = 16;
        boolean rowMajor = true;
        for (int i = 0; i < 4; i++){
            int offset = startOffset + strides * i;
            for (int j = 0; j < 4; j++){
                                                                            
                int element = rowMajor ? j * 4 + i : i * 4 + j;
                b.putFloat(offset, matrix[element]);
                offset += Constants.BYTES_PER_FLOAT;
            }
        }
    }
}
