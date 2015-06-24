package com.xoozi.apiguides.gl.objects;

import android.graphics.Bitmap;
import android.graphics.Color;

//import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glBindBuffer;

import com.xoozi.apiguides.gl.data.IndexBuffer;
import com.xoozi.apiguides.gl.data.VertexBuffer;
import com.xoozi.apiguides.gl.programs.HeightmapShaderProgram;

public class Heightmap{
    private static final int POSITION_COMPONENT_COUNT = 3;


    private final int           _width;
    private final int           _height;
    private final int           _numElements;
    private final VertexBuffer  _vertexBuffer;
    private final IndexBuffer   _indexBuffer;

    public Heightmap(Bitmap bitmap){
        _width      = bitmap.getWidth();
        _height     = bitmap.getHeight();


        if(_width * _height > 65536){
            throw new RuntimeException(String.format("%dx%d Heightmap is too large for the index buffer.",
                        _width, _height));
        }

        _numElements    = _calculateNumElements();
        _vertexBuffer   = new VertexBuffer(_loadBitmapData(bitmap));
        _indexBuffer    = new IndexBuffer(_createIndexData());
    }

    public void bindData(HeightmapShaderProgram program){
        _vertexBuffer.setVertexAttribPointer(0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _indexBuffer.getBufferId());
        glDrawElements(GL_LINES, _numElements, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private float[] _loadBitmapData(Bitmap bitmap){
        final int[] pixels = new int[_width * _height];
        bitmap.getPixels(pixels, 0, _width, 0, 0 , _width, _height);
        bitmap.recycle();

        final float[] heightmapVertices = 
            new float[_width * _height * POSITION_COMPONENT_COUNT];
        int offset = 0;
        for(int row = 0; row < _height; row++){
            for(int col = 0; col < _width; col++){
                final float xPosition = ((float)col / (float)(_width - 1)) - 0.5f;
                final float yPosition = (float) Color.red(pixels[(row * _height) + col]) / (float) 255;
                final float zPosition = ((float)row / (float)(_height - 1)) - 0.5f;

                heightmapVertices[offset++] = xPosition;
                heightmapVertices[offset++] = yPosition;
                heightmapVertices[offset++] = zPosition;
            }
        }
        return heightmapVertices;
    }

    private int _calculateNumElements(){
        return (_width - 1) * (_height - 1) * 2 * 3;
    }

    private short[] _createIndexData(){
        final short[] indexData = new short[_numElements];
        int offset = 0;

        for(int row = 0; row < _height -1 ; row++){
            for(int col = 0; col < _width - 1; col++){
                short topLeft       = (short)(row * _width + col);
                short topRight      = (short)(row * _width + col + 1);
                short bottomLeft    = (short)((row + 1) * _width + col);
                short bottomRight   = (short)((row + 1) * _width + col + 1);

                indexData[offset++] = topLeft;
                indexData[offset++] = bottomLeft;
                indexData[offset++] = topRight;

                indexData[offset++] = topRight;
                indexData[offset++] = bottomLeft;
                indexData[offset++] = bottomRight;
            }
        }
        return indexData;
    }
}
