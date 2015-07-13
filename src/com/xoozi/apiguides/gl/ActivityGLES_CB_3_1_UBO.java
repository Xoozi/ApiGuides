package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.xoozi.apiguides.gl.glutil.Geometry.Vector;
import com.xoozi.apiguides.gl.objects.DiceVBO;
import com.xoozi.apiguides.gl.programs.CB_3_UBO_ShaderProgram;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.GL_FRONT;
import static android.opengl.GLES30.GL_BACK;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.glViewport;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glCullFace;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ActivityGLES_CB_3_1_UBO extends Activity implements
        SensorEventListener, OnTouchListener {
    private Sensor _rotationVectorSensor;
    private SensorManager _sensorManager;

    private GLSurfaceView _glSurfaceView;
    private CB_3_UBO_ShaderProgram _colorShaderProgram;

    private final float[] _modelMatrix = new float[16];
    private final float[] _projectionMatrix = new float[16];
    private final float[] _viewMatrix = new float[16];
    private final float[] _rotationMatrix = new float[16];

    private DiceVBO _dice;

    private Vector _pos = new Vector(-4, 1, 1);
    private boolean _toggle = true;
    private int _transformationFlag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(3);
        _glSurfaceView.setRenderer(new MyRenderer());
        _glSurfaceView.setOnTouchListener(this);
        setContentView(_glSurfaceView);

        _sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        _rotationVectorSensor = _sensorManager
                .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
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

    /**
     * 准备各矩阵
     */
    private void _setupMatrix(int width, int height) {

        perspectiveM(_projectionMatrix, 0, 45, (float) width / (float) height,
                1f, 10f);

        setLookAtM(_viewMatrix, 0, 2f, 2f, 2f, //eyeX, eyeY, eyeZ
                //0f, 1.2f, 2.2f, //eyeX, eyeY, eyeZ
                //0f, 1.2f, 0f, //eyeX, eyeY, eyeZ
                0f, 0f, 0f, //centerX, centerY, centerZ
                //0f, 0f, 1f);    //upX,  upY, upZ
                0f, 1f, 0f); //upX,  upY, upZ
    }

    private void _handleTouchPress(float x, float y) {
        float v;
        if (x < 400) {
            v = 0.1f;
        } else {
            v = -0.1f;
        }
        Vector moveStep = new Vector(v, 0, 0);
        Vector rms;
        synchronized (this) {

            rms = moveStep.multiplyMatrix(_rotationMatrix);
        }
        _pos = _pos.add(rms);

        _toggle = !_toggle;

        if (0 == _transformationFlag) {
            _transformationFlag = 1;
        } else {
            _transformationFlag = 0;
        }
    }

    /**
     * 将构建的对象放置在场景中
     */
    private void _positionObjectInScene(float x, float y, float z) {
        setIdentityM(_modelMatrix, 0);
        translateM(_modelMatrix, 0, x, y, z);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            synchronized (this) {
                SensorManager.getRotationMatrixFromVector(_rotationMatrix,
                        event.values);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public class MyRenderer implements Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {

            //glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);

            glEnable(GL_CULL_FACE);

            if (_toggle) {
                glCullFace(GL_BACK);
            } else {
                glCullFace(GL_FRONT);
            }

            _positionObjectInScene(_pos.x, _pos.y, _pos.z);

            _colorShaderProgram.useProgram();
            _colorShaderProgram.setTransformationFlag(_transformationFlag);

            if (0 == _transformationFlag) {
                Log.w("wtf", "transformation use uniforms");
                _colorShaderProgram.setUniforms(_modelMatrix, _viewMatrix,
                        _rotationMatrix, _projectionMatrix);
            } else {
                Log.w("wtf", "transformation use ubo");
                _colorShaderProgram.updateMatrices(_modelMatrix, _viewMatrix,
                        _rotationMatrix, _projectionMatrix);
            }
            _dice.bindData(_colorShaderProgram);
            _dice.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            _setupMatrix(width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0, 0.1f, 0.1f, 1);
            _colorShaderProgram = new CB_3_UBO_ShaderProgram(
                    ActivityGLES_CB_3_1_UBO.this);

            setIdentityM(_rotationMatrix, 0);
            _dice = new DiceVBO(0.5f);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        _handleTouchPress(event.getX(), event.getY());
        return false;
    }
}
