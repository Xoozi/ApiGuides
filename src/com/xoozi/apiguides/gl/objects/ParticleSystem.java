package com.xoozi.apiguides.gl.objects;

import android.graphics.Color;

import static android.opengl.GLES30.GL_POINTS;
import static android.opengl.GLES30.GL_BLEND;
import static android.opengl.GLES30.GL_ONE;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glDisable;
import static android.opengl.GLES30.glBlendFunc;

import com.xoozi.apiguides.gl.data.Constants;
import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.glutil.Geometry.Point;
import com.xoozi.apiguides.gl.glutil.Geometry.Vector;
import com.xoozi.apiguides.gl.programs.ParticleShaderProgram;

public class ParticleSystem{

    private static final int POSITION_COMPONENT_COUNT   = 3;
    private static final int COLOR_COMPONENT_COUNT      = 3;
    private static final int VECTOR_COMPONENT_COUNT     = 3;
    private static final int START_TIME_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = 
                            POSITION_COMPONENT_COUNT 
                            + COLOR_COMPONENT_COUNT
                            + VECTOR_COMPONENT_COUNT
                            + START_TIME_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;

    private final float[]       _particles;
    private final VertexArray   _vertexArray;
    private final int           _maxParticleCount;

    private int _currentParticleCount;
    private int _nextParticle;

    public ParticleSystem(int maxParticleCount){
        _maxParticleCount   = maxParticleCount;
        _particles          = new float[_maxParticleCount * TOTAL_COMPONENT_COUNT];
        _vertexArray        = new VertexArray(_particles);
        _nextParticle       = 0;
        _currentParticleCount = 0;
    }

    public void addParticle(Point position, int color, Vector direction, float startTime){
        final int particleOffset = _nextParticle * TOTAL_COMPONENT_COUNT;
        int currentOffset = particleOffset;
        _nextParticle ++;

        if(_currentParticleCount < _maxParticleCount){
            _currentParticleCount++; 
        }

        if(_nextParticle == _maxParticleCount){
            _nextParticle = 0;
        }

        _particles[currentOffset++] = position.x;
        _particles[currentOffset++] = position.y;
        _particles[currentOffset++] = position.z;

        _particles[currentOffset++] = Color.red(color) / 255f;
        _particles[currentOffset++] = Color.green(color) / 255f;
        _particles[currentOffset++] = Color.blue(color) / 255f;

        _particles[currentOffset++] = direction.x;
        _particles[currentOffset++] = direction.y;
        _particles[currentOffset++] = direction.z;

        _particles[currentOffset++] = startTime;

        _vertexArray.updateBuffer(_particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }


    public void bindData(ParticleShaderProgram particleProgram){
        int dataOffset = 0;

        _vertexArray.setVertexAttributePointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;
        
        _vertexArray.setVertexAttributePointer(dataOffset,
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        _vertexArray.setVertexAttributePointer(dataOffset,
                particleProgram.getDirectionVectorAttributeLocation(),
                VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        _vertexArray.setVertexAttributePointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                START_TIME_COMPONENT_COUNT, STRIDE);
    }

    public void draw(){
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glDrawArrays(GL_POINTS, 0, _currentParticleCount);
        glDisable(GL_BLEND);
    }
}
