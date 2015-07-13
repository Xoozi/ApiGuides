package com.xoozi.apiguides.gl.data;


import static android.opengl.GLES30.GL_UNIFORM_BUFFER;
import static android.opengl.GLES30.GL_UNIFORM_TYPE;
import static android.opengl.GLES30.GL_UNIFORM_OFFSET;
import static android.opengl.GLES30.GL_UNIFORM_SIZE;
import static android.opengl.GLES30.GL_UNIFORM_ARRAY_STRIDE;
import static android.opengl.GLES30.GL_UNIFORM_MATRIX_STRIDE;
import static android.opengl.GLES30.GL_UNIFORM_IS_ROW_MAJOR;
import static android.opengl.GLES30.GL_DYNAMIC_DRAW;
import static android.opengl.GLES30.GL_UNIFORM_BLOCK_DATA_SIZE;
import static android.opengl.GLES30.GL_INVALID_INDEX;
import static android.opengl.GLES30.GL_MAP_WRITE_BIT;
import static android.opengl.GLES30.GL_MAP_INVALIDATE_BUFFER_BIT;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindBufferBase;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glGetUniformBlockIndex;
import static android.opengl.GLES30.glGetActiveUniformBlockiv;
import static android.opengl.GLES30.glUniformBlockBinding;
import static android.opengl.GLES30.glMapBufferRange;
import static android.opengl.GLES30.glUnmapBuffer;
import static android.opengl.GLES30.glGetUniformIndices;
import static android.opengl.GLES30.glGetActiveUniformsiv;


import java.nio.ByteBuffer;

import android.util.Log;

import com.xoozi.apiguides.gl.programs.ShaderProgram;


/**
 * 对OpenGLES 的转换矩阵们的封装
 *   把Model, View, Rotation, Projection, 
 *   四个矩阵封装在UniformBlock里, 提高传输效率
 *   在Java代码里进行矩阵运算效率不高, 放到GPU去算吧
 */
public class TransformationBlock {
    private final static String     BLOCK_NAME = "Transformation";
    private final static String[]   FIELDS_NAMES = new String[]{
                                                        BLOCK_NAME+".ModelMatrix",
                                                        BLOCK_NAME+".ViewMatrix",
                                                        BLOCK_NAME+".RotationMatrix",
                                                        BLOCK_NAME+".ProjectionMatrix"
                                                        };
    private final int _program;

    private final int _blockIndex;
    private final int _blockBindingPoint;
    private final int _blockSize;
    private final int _bufferId;

    private final int _indices[]        = new int[4];
    private final int _offsets[]        = new int[4];
    private final int _sizes[]          = new int[4];
    private final int _matrixStrides[]  = new int[4];
    private final int _arrayStrides[]   = new int[4];
    private final int _types[]          = new int[4];
    private final int _matrixRowMajor[] = new int[4];

    public TransformationBlock(ShaderProgram program){

        _program = program.getProgram();

        /**
         * 获取Uniform Block索引
         */
        _blockIndex = glGetUniformBlockIndex(_program,  BLOCK_NAME);
        if(GL_INVALID_INDEX == _blockIndex){
            throw new RuntimeException("Could not create a new uniform block object.");
        }
        /**
         * 将Uniform Block Index绑定到BindingPoint
         */
        _blockBindingPoint = _blockIndex;
        glUniformBlockBinding(_program, _blockIndex, _blockBindingPoint);

        /**
         * 查询uniform block 的大小
         */
        int[] sizeBuff = new int[1];
        glGetActiveUniformBlockiv(_program, _blockIndex,
                GL_UNIFORM_BLOCK_DATA_SIZE, sizeBuff, 0);
        _blockSize = sizeBuff[0];

        /**
         * 创建Uniform Buffer对象
         * 绑定到隐含管线
         * 在显存中分配UBO的空间
         * 将Uniform Buffer也绑定到同一个BindingPoint
         */
        final int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);
        _bufferId = buffers[0];
        glBindBuffer(GL_UNIFORM_BUFFER, _bufferId);
        glBufferData(GL_UNIFORM_BUFFER, _blockSize, null, GL_DYNAMIC_DRAW);
        glBindBufferBase(GL_UNIFORM_BUFFER, _blockBindingPoint, _bufferId);

        /**
         * 查询每个字段的索引
         */
        glGetUniformIndices(_program, FIELDS_NAMES, _indices, 0);

        glGetActiveUniformsiv(_program, 4, _indices, 0, GL_UNIFORM_TYPE, _types, 0);
        glGetActiveUniformsiv(_program, 4, _indices, 0, GL_UNIFORM_SIZE, _sizes, 0);
        glGetActiveUniformsiv(_program, 4, _indices, 0, GL_UNIFORM_OFFSET, _offsets, 0);
        glGetActiveUniformsiv(_program, 4, _indices, 0, GL_UNIFORM_ARRAY_STRIDE, _arrayStrides, 0);
        glGetActiveUniformsiv(_program, 4, _indices, 0, GL_UNIFORM_MATRIX_STRIDE, _matrixStrides, 0);
        glGetActiveUniformsiv(_program, 4, _indices, 0, GL_UNIFORM_IS_ROW_MAJOR, _matrixRowMajor, 0);

        Log.w("log", "Block Index:"+_blockIndex);
        Log.w("log", "Block Size:"+_blockSize);
        Log.w("log", "Buffer id:"+_bufferId);
        Log.w("log", "Binding Point:"+_blockBindingPoint);
        _logArray("indices", _indices);
        _logArray("types", _types);
        _logArray("sizes", _sizes);
        _logArray("offsets", _offsets);
        _logArray("arrayStrides", _arrayStrides);
        _logArray("matrixStrides", _matrixStrides);
        _logArray("matrixRowMajor", _matrixRowMajor);

        /**
         * 重要!! 完成后要解除UBO到隐含管线的绑定
         */
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    private void _logArray(String name, int[] array){
        StringBuilder sb = new StringBuilder();
        sb.append(name+":");
        for(int n : array){
            sb.append("\n\t"+n);
        }
        Log.w("log", sb.toString());
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
        glBindBufferBase(GL_UNIFORM_BUFFER, _blockBindingPoint, _bufferId);
        glBindBuffer(GL_UNIFORM_BUFFER, _bufferId);

        /**
         * 映射显存到native堆
         */
        ByteBuffer      buf = (ByteBuffer)glMapBufferRange(GL_UNIFORM_BUFFER, 0, 
                                        _blockSize, 
                                        GL_MAP_WRITE_BIT | GL_MAP_INVALIDATE_BUFFER_BIT);


        /**
         * 写native堆上的内存
         */

        _setMatrix(buf, 0, modelMatrix);
        _setMatrix(buf, 1, viewMatrix);
        _setMatrix(buf, 2, rotationMatrix);
        _setMatrix(buf, 3, projectionMatrix);


        /**
         * 解除并刷新显存映射
         */
        boolean ret = glUnmapBuffer(GL_UNIFORM_BUFFER);

        Log.w("wtf", "UnmapBuffer ret:"+ret);


        /**
         * 重要!! 完成后要解除UBO到隐含管线的绑定
         */
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    private void _setMatrix(ByteBuffer b, int index, float[] matrix){

        int startOffset = _offsets[index];
        int strides     = _matrixStrides[index];
        boolean rowMajor = false;
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
