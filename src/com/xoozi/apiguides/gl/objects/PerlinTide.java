package com.xoozi.apiguides.gl.objects;

import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glBindBuffer;

import com.xoozi.apiguides.gl.data.IndexBuffer;
import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.glutil.Geometry;
import com.xoozi.apiguides.gl.glutil.Geometry.Vector;
import com.xoozi.apiguides.gl.glutil.PerlinNoise;
import com.xoozi.apiguides.gl.programs.CB_5_Perv_Diffuse_ShaderProgram;
import com.xoozi.apiguides.gl.programs.ShaderProgram;
import com.xoozi.apiguides.gl.glutil.Geometry.Point;

public class PerlinTide {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final int           _width;
    private final int           _height;
    private final int           _numElements;
    private final VertexArray   _vertexBuffer;
    private final VertexArray   _normalBuffer;
    private final IndexBuffer   _indexBuffer;
    private final float         _tall;
    private final float         _step;
    private final float         _halfWidth;
    private final float         _halfHeight;
    private final float[]       _vertexArray;
    private final float[]       _normalRay;
    private final PerlinNoise   _perlinNoise;
    private final float         _inc = 0.06f;

    private final Vector        _lightVector;

    private float               _xn = 0.0f;
    private float               _yn = 0.0f;
    private float               _zn = 0.0f;

    public PerlinTide(int width, int height, float step, float tall){
        _tall       = tall;
        _width      = width;
        _height     = height;
        _step       = step;
        _halfWidth  = (_width * _step) / 2;
        _halfHeight = (_height * _step) / 2;
        _perlinNoise= new PerlinNoise();

        if(_width * _height > 65536){
            throw new RuntimeException(String.format("%dx%d PerlinTide is too large for the index buffer.",
                        _width, _height));
        }

        _vertexArray    = _loadVertexXY();
        _normalRay      = _loadNormalRay();
        _numElements    = _calculateNumElements();
        _vertexBuffer   = new VertexArray(_vertexArray);
        _normalBuffer   = new VertexArray(_normalRay);
        _indexBuffer    = new IndexBuffer(_createIndexData());
        Vector tmpVector= new Vector(0 , 10 * _tall, 0);
        _lightVector    = tmpVector.normalize();
    }

    public Vector getLightVector(){
        return _lightVector;
    }

    public void bindData(CB_5_Perv_Diffuse_ShaderProgram  program){
        _vertexBuffer.setVertexAttributePointer(0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        _vertexBuffer.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT * BYTES_PER_FLOAT,
                program.getNormalAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        program.setLightVector(_lightVector.x, _lightVector.y, _lightVector.z);
    }

    public void draw(boolean lines){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _indexBuffer.getBufferId());
        if(lines){
            glDrawElements(GL_LINES, _numElements, GL_UNSIGNED_SHORT, 0);
        }else{
            glDrawElements(GL_TRIANGLES, _numElements, GL_UNSIGNED_SHORT, 0);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void drawNormal(ShaderProgram rayShader){
        _normalBuffer.setVertexAttributePointer(0,
                rayShader.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
        glDrawArrays(GL_LINES, 0, _width*_height*2 + 2);
    }

    public void    update(){
        _updateHeight();
        _updateNormal();
        _vertexBuffer.updateBuffer(_vertexArray, 0, _vertexArray.length);
        _normalBuffer.updateBuffer(_normalRay, 0, _normalRay.length);
    }


    private void    _updateNormal(){
        int offset = 0;
        float posX, posY, posZ;
        for(int row = 0; row < _height; row++){
            for(int col = 0; col < _width; col ++){
                final Point top = _getPoint(row - 1, col);
                final Point bottom = _getPoint(row + 1, col);
                final Point right = _getPoint(row, col + 1);
                final Point left = _getPoint(row, col - 1);
                final Vector right2Left = Geometry.vectorBetween(right, left);
                final Vector top2Bottom = Geometry.vectorBetween(top, bottom);
                final Vector normal = right2Left.crossProduct(top2Bottom).normalize();

                _normalRay[offset] = posX = _vertexArray[offset];
                offset++;
                _normalRay[offset] = posY = _vertexArray[offset];
                offset++;
                _normalRay[offset] = posZ = _vertexArray[offset];
                offset++;


                final Vector snormal = normal.scale(10);
                _normalRay[offset]     = posX + snormal.x;
                _vertexArray[offset++] = normal.x;
                _normalRay[offset]     = posY + snormal.y;
                _vertexArray[offset++] = normal.y;
                _normalRay[offset]     = posZ + snormal.z;
                _vertexArray[offset++] = normal.z;
            }
        }

        _normalRay[offset++] = 0;
        _normalRay[offset++] = 0;
        _normalRay[offset++] = 0;
        _normalRay[offset++] = _lightVector.x;
        _normalRay[offset++] = _lightVector.y;
        _normalRay[offset++] = _lightVector.z;
    }

    private Point   _getPoint(int row, int col){

        col = _clamp(col, 0, _width - 1);
        row = _clamp(row, 0, _height - 1);
        int offset = row * col * TOTAL_COMPONENT_COUNT;
        return new Point (_vertexArray[offset+0], _vertexArray[offset+1], _vertexArray[offset+2]);
    }

    private int     _clamp(int val, int min, int max){
        return Math.max(min, Math.min(max, val));
    }

    private void    _updateHeight(){
        _xn = 0.0f;
        _yn = 0.0f;
        int offset = 0;
        for(int y = 0; y < _height; y ++){
            for(int x = 0; x < _width; x ++){
                float h = _perlinNoise.noise(_xn, _yn, _zn) * _tall;
                _xn += _inc;

                offset++;
                _vertexArray[offset++] = h;
                offset++;
                offset += 3;
            }
            _xn = 0;
            _yn += _inc;
        }
        _zn += _inc;

    }

    private float[] _loadNormalRay(){
        final float[] normalRay = new float[2 * (_width * _height * TOTAL_COMPONENT_COUNT+1)];

        return normalRay;
    }

    private float[] _loadVertexXY(){
        final float[] vertices = new float[_width * _height * TOTAL_COMPONENT_COUNT];
        int offset = 0;
        for(int row = 0; row < _height; row++){
            for(int col = 0; col < _width; col++){
                final float xPosition = col * _step - _halfWidth;
                final float zPosition = row * _step - _halfHeight;

                vertices[offset++] = xPosition;
                vertices[offset++] = 0;
                vertices[offset++] = zPosition;
                offset += 3;
            }
        }
        return vertices;
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
