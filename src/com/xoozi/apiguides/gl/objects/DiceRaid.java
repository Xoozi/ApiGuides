package com.xoozi.apiguides.gl.objects;


import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_STATIC_DRAW;
//import static android.opengl.GLES30.GL_DYNAMIC_DRAW;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_MAP_WRITE_BIT;
import static android.opengl.GLES30.glGenVertexArrays;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glBufferSubData;
import static android.opengl.GLES30.glVertexAttribPointer;
//import static android.opengl.GLES30.glVertexAttribDivisor;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glDrawElementsInstanced;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glMapBufferRange;
import static android.opengl.GLES30.glUnmapBuffer;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.multiplyMM;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.xoozi.apiguides.gl.data.Constants;
import com.xoozi.apiguides.gl.programs.ShaderProgram;
import com.xoozi.apiguides.utils.Utils;

public class DiceRaid {

    private static int   DIMENSION  = 2;
    private static float DISTANCE   = 5.0f;

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int MATRIX_COMPONENT_COUNT = 16;

    private final short[] _indices = new short[] { 
            0, 3, 1,    3, 2, 1, 
            7, 4, 6,    4, 5, 6, 
            4, 0, 5,    0, 1, 5, 
            3, 7, 2,    7, 6, 2, 
            1, 2, 5,    2, 6, 5, 
            3, 0, 7,     0, 4, 7 };
    private final float[] _colors = new float[] { 0, 0, 0, //v0
            0, 0, 1, //v1
            0, 1, 0, //v2
            0, 1, 1, //v3

            1, 0, 0, //v4
            1, 0, 1, //v5
            1, 1, 0, //v6
            1, 1, 1 //v7
    };
    private final float[] _vertices;

    private final int         _indexNum;
    private final int         _matrixNum;
    private int               _vaoId;
    private int               _matrixId;

    public DiceRaid(ShaderProgram program, float size){
        this(program, size, DIMENSION, DIMENSION, DIMENSION);
    }

    public DiceRaid(ShaderProgram program, float size, int xDim, int yDim, int zDim){
        Utils.amLog("size:"+size);
            _vertices = new float[] { 
            -size, -size, size, //v0
            -size, size, size, //v1
            size, size, size, //v2
            size, -size, size, //v3

            -size, -size, -size, //v4
            -size, size, -size, //v5
            size, size, -size, //v6
            size, -size, -size, //v7
        };

        
        int i = 0 ;
        for(float v:_vertices){
           Utils.amLog("v["+i+"]="+v); 
        }

        _indexNum = _indices.length;
        _matrixNum= xDim * yDim * zDim;
        _initVAO(program, _vertices, _colors, _indices);
    }

    public void draw(float[] MVPMatrix){

        glBindVertexArray(_vaoId);
        glDrawElements(GL_TRIANGLES, _indexNum, GL_UNSIGNED_SHORT, 0);
    }

    public void drawInstance(float[]PVMatrix, float[] modelMatrix){
        glBindBuffer( GL_ARRAY_BUFFER, _matrixId );
        ByteBuffer      buf = (ByteBuffer)glMapBufferRange(GL_ARRAY_BUFFER, 0, 
                                        _matrixNum * MATRIX_COMPONENT_COUNT* Constants.BYTES_PER_FLOAT, 
                                        GL_MAP_WRITE_BIT);
        int matrixStride = MATRIX_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;
        int bufOffset = 0;
        float[] M2Matrix = new float[16];
        float[] PVMMatrix = new float[16];
        for ( int i = 0; i < DIMENSION; i++){
            for ( int j = 0; j < DIMENSION; j++){
                for ( int k = 0; k < DIMENSION; k++){
                    _setMatrix(buf, bufOffset,
                            PVMatrix, modelMatrix,
                            M2Matrix, PVMMatrix,
                            i*DISTANCE, j*DISTANCE, k*DISTANCE);
                    bufOffset += matrixStride;
                }
            }
        }
        glUnmapBuffer ( GL_ARRAY_BUFFER);
        glBindVertexArray(_vaoId);

        glDrawElementsInstanced(GL_TRIANGLES, _indexNum, GL_UNSIGNED_SHORT, 0, _matrixNum);
    }

    private void _setMatrix(ByteBuffer b, int bufOffset, 
                            float[] PVMatrix, float[] modelMatrix, 
                            float[] M2Matrix, float[] PVMMatrix,
                            float x, float y, float z){
        translateM(M2Matrix, 0, modelMatrix, 0, x, y, z);
        multiplyMM(PVMMatrix, 0, PVMatrix, 0, M2Matrix, 0);

        for(int i = 0 ; i < MATRIX_COMPONENT_COUNT; i++){
            b.putFloat(bufOffset, PVMMatrix[i]);
            bufOffset += Constants.BYTES_PER_FLOAT;
        }
    }


    private void _initVAO(ShaderProgram program, float[] vertexData, float[] colorData, short[] indexData){
        final int VERTEX_LOCATION = program.getPositionAttributeLocation();
        final int COLOR_LOCATION  = program.getColorAttributeLocation();
        /*final int MATRIX1_LOCATION= program.getMatrixAttributeLocation();
        final int MATRIX2_LOCATION= MATRIX1_LOCATION + 1;
        final int MATRIX3_LOCATION= MATRIX1_LOCATION + 2;
        final int MATRIX4_LOCATION= MATRIX1_LOCATION + 3;*/
        final int vertexBufObjs[]  = new int[1];
        //final int matrixBufObjs[]  = new int[1];
        final int indexBufObjs[]   = new int[1];
        final int vertexArrayObjs[]= new int[1];
        final int vboId;
        final int iboId;
        FloatBuffer vertexArray;
        FloatBuffer colorArray;
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
        vertexArray = ByteBuffer
            .allocateDirect(2 * vertexData.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData);
        vertexArray.position(0);
        /*colorArray = ByteBuffer
            .allocateDirect(colorData.length * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(colorData);
        colorArray.position(0);*/

        int vertexSize = vertexArray.capacity() * Constants.BYTES_PER_FLOAT;
        //int colorSize = colorArray.capacity() * Constants.BYTES_PER_FLOAT;
        //Utils.amLog("vertexSize:"+vertexSize+", colorSize:"+colorSize);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexSize , vertexArray, GL_STATIC_DRAW);
        //glBufferData(GL_ARRAY_BUFFER, vertexSize + colorSize, null, GL_STATIC_DRAW);
        //glBufferSubData( GL_ARRAY_BUFFER, 0,            vertexSize,	vertexArray);
        //glBufferSubData( GL_ARRAY_BUFFER, vertexSize,   colorSize,	colorArray);



        /**
         * 创建VBO对象来容纳 矩阵们
         * 用glBufferData来分配空间
         
        glGenBuffers(matrixBufObjs.length, matrixBufObjs, 0);
        if(0 == matrixBufObjs[0]){
            throw new RuntimeException("Could not create a new vertex buffer object.");
        }
        _matrixId = matrixBufObjs[0];
        glBindBuffer( GL_ARRAY_BUFFER, _matrixId );
        glBufferData(GL_ARRAY_BUFFER, _matrixNum * MATRIX_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT, null, GL_DYNAMIC_DRAW);
        */

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
         * 像CBO bindData一样 再绑定一次cboId
         * 调用glVertexAttribPointer的重载版本将显存中的VBO传给着色器
         * 启用着色器输入变量
         * 再一次绑定IBO 完成VAO的初始化
         */
        glGenVertexArrays(vertexArrayObjs.length, vertexArrayObjs, 0);
        _vaoId = vertexArrayObjs[0];
        glBindVertexArray(_vaoId);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glEnableVertexAttribArray(VERTEX_LOCATION);
        //glEnableVertexAttribArray(COLOR_LOCATION);
        glVertexAttribPointer(VERTEX_LOCATION, 
                POSITION_COMPONENT_COUNT
                , GL_FLOAT,
                false, 0, 0);
        /*glVertexAttribPointer(COLOR_LOCATION, 
                POSITION_COMPONENT_COUNT
                , GL_FLOAT,
                false, 0, vertexSize/2);*/

        /*glBindBuffer(GL_ARRAY_BUFFER, cboId);
        glVertexAttribPointer(COLOR_LOCATION, 
                POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, 0, 0);
        glEnableVertexAttribArray(COLOR_LOCATION);*/
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);

        // Create VBO for transformation matrix and set attribute parameters
        /*int matrixStride = MATRIX_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;
        glBindBuffer(GL_ARRAY_BUFFER, _matrixId);
        glVertexAttribPointer(MATRIX1_LOCATION, 4, GL_FLOAT, false, matrixStride, Constants.BYTES_PER_FLOAT * 0);
        glVertexAttribPointer(MATRIX2_LOCATION, 4, GL_FLOAT, false, matrixStride, Constants.BYTES_PER_FLOAT * 4);
        glVertexAttribPointer(MATRIX3_LOCATION, 4, GL_FLOAT, false, matrixStride, Constants.BYTES_PER_FLOAT * 8);
        glVertexAttribPointer(MATRIX4_LOCATION, 4, GL_FLOAT, false, matrixStride, Constants.BYTES_PER_FLOAT * 12);
        glEnableVertexAttribArray(MATRIX1_LOCATION);
        glEnableVertexAttribArray(MATRIX2_LOCATION);
        glEnableVertexAttribArray(MATRIX3_LOCATION);
        glEnableVertexAttribArray(MATRIX4_LOCATION);
        glVertexAttribDivisor(MATRIX1_LOCATION, 1);
        glVertexAttribDivisor(MATRIX2_LOCATION, 1);
        glVertexAttribDivisor(MATRIX3_LOCATION, 1);
        glVertexAttribDivisor(MATRIX4_LOCATION, 1);*/



        /**
         * 解除VAO到隐含管线的绑定
         * 解除VBO CBO到隐含管线的绑定
         * 解除IBO到隐含管线的绑定
         */
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }
}
