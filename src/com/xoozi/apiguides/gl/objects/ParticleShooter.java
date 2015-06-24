package com.xoozi.apiguides.gl.objects;

import java.util.Random;

import com.xoozi.apiguides.gl.glutil.Geometry.Point;
import com.xoozi.apiguides.gl.glutil.Geometry.Vector;

import static android.opengl.Matrix.setRotateEulerM;
import static android.opengl.Matrix.multiplyMV;

public class ParticleShooter {
    private final Point     _position;
    private final int       _color;
    private final float     _angleVariance;
    private final float     _speedVariance;

    private final Random    _random = new Random();
    private float[]         _rotationMatrix = new float[16];
    private float[]         _directionVector = new float[4];
    private float[]         _resultVector = new float[4];

    public ParticleShooter(Point position, Vector direction, int color, 
            float angleVarianceInDegress, float speedVariance){
        _position       = position;
        _color          = color;
        _angleVariance  = angleVarianceInDegress;
        _speedVariance  = speedVariance;

        _directionVector[0] = direction.x;
        _directionVector[1] = direction.y;
        _directionVector[2] = direction.z;

    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count){
        for(int i = 0; i < count; i++){
            setRotateEulerM(_rotationMatrix, 0,
                    (_random.nextFloat() - 0.5f) * _angleVariance,
                    (_random.nextFloat() - 0.5f) * _angleVariance,
                    (_random.nextFloat() - 0.5f) * _angleVariance);
            multiplyMV(_resultVector, 0, _rotationMatrix, 0, _directionVector, 0);

            float speedAdjustment = 1f + _random.nextFloat() * _speedVariance;

            Vector dv = new Vector(_resultVector[0] * speedAdjustment,
                                    _resultVector[1] * speedAdjustment,
                                    _resultVector[2] * speedAdjustment);
            particleSystem.addParticle(_position, _color, dv, currentTime);
        }
    }
}
