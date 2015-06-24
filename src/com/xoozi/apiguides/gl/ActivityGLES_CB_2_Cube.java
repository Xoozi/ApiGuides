package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.xoozi.apiguides.gl.objects.Dice;
import com.xoozi.apiguides.gl.programs.ColorShaderProgram;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.glViewport;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.Matrix.setIdentityM;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.opengl.GLSurfaceView.Renderer;

public class ActivityGLES_CB_2_Cube extends Activity
        implements SensorEventListener{
    private Sensor _rotationVectorSensor;
    private SensorManager _sensorManager;

    private GLSurfaceView           _glSurfaceView;
    private ColorShaderProgram      _colorShaderProgram;



    private final float[] _rotationMatrix = new float[16];

    private Dice                    _dice = new Dice(0.5f);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(3);
        _glSurfaceView.setRenderer(new MyRenderer());
        setContentView(_glSurfaceView);

        _sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        _rotationVectorSensor = _sensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _glSurfaceView.onResume();
        _sensorManager.registerListener(this, _rotationVectorSensor, 
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _glSurfaceView.onPause();
        _sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            synchronized(this){
                SensorManager.getRotationMatrixFromVector(
                        _rotationMatrix , event.values);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public class MyRenderer implements Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);


            _colorShaderProgram.useProgram();
            _colorShaderProgram.setUniforms(_rotationMatrix, 1, 0.3f, 0.5f, 0.7f);
            _dice.bindData(_colorShaderProgram);
            _dice.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0, 0.1f, 0.1f, 1);
            _colorShaderProgram = new ColorShaderProgram(
                    ActivityGLES_CB_2_Cube.this);

            setIdentityM(_rotationMatrix, 0);
        }
    }
}
