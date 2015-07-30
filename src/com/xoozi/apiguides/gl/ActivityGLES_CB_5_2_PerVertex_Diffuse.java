package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.xoozi.apiguides.R;
import com.xoozi.apiguides.gl.glutil.Geometry.Vector;
import com.xoozi.apiguides.gl.objects.PerlinTide;
import com.xoozi.apiguides.gl.programs.CB_5_Perv_Diffuse_ShaderProgram;
import com.xoozi.apiguides.gl.programs.RayShaderProgram;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.glViewport;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.rotateM;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.opengl.GLSurfaceView.Renderer;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ActivityGLES_CB_5_2_PerVertex_Diffuse extends Activity implements
        SensorEventListener, 
        OnTouchListener {
    private Sensor _rotationVectorSensor;
    private SensorManager _sensorManager;

    private GLSurfaceView _glSurfaceView;
    private CB_5_Perv_Diffuse_ShaderProgram _diffuseShaderProgram;
    private RayShaderProgram                _rayShaderProgram;

    private final float[] _modelMatrix = new float[16];
    private final float[] _projectionMatrix = new float[16];
    private final float[] _viewMatrix = new float[16];
    private final float[] _viewRotateMatrix = new float[16];
    private final float[] _VPMatrix = new float[16];
    private final float[] _IVPMatrix = new float[16];
    private final float[] _MVPMatrix = new float[16];
    private final float[] _rotationMatrix = new float[16];
    private final float[] _RRMatrix = new float[16];

    private PerlinTide    _tide;

    private Vector _eye = new Vector(0, 0, 200);
    private Vector _center;
    private int _width;
    private int _height;

    private Vector _pos = new Vector(0, 0, 0);
    private int _moveFlag = 0;
    private GestureDetector _detector;
    private MyGestureListener _gestureListener = new MyGestureListener();
    private boolean     _drawNormal = false;
    private boolean     _drawLines  = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(3);
        _glSurfaceView.setRenderer(new MyRenderer());
        _glSurfaceView.setClickable(true);
        _glSurfaceView.setOnTouchListener(this);
        setContentView(_glSurfaceView);


        _sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        _rotationVectorSensor = _sensorManager
                .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        _adjCenter();

        _detector = new GestureDetector(this, _gestureListener);
        _detector.setOnDoubleTapListener(_gestureListener);
        _detector.setIsLongpressEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_cb_5_2_diffuse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_normal:{
                _drawNormal = !_drawNormal;
                break;
            }

            case R.id.action_render:{
                _drawLines = !_drawLines;
                break;
            }
        }
        return true;
    }

    private void _adjCenter() {
        _center = new Vector(0f, _eye.y, _eye.z - 100);
    }

    /**
     * 准备各矩阵
     */
    private void _setupMatrix(int width, int height) {

        rotateM(_RRMatrix, 0, _rotationMatrix, 0, 90, 1, 0, 0);


        perspectiveM(_projectionMatrix, 0, 45, (float) width / (float) height,
                1f, 10000f);

        setLookAtM(_viewMatrix, 0, _eye.x, _eye.y, _eye.z, //eyeX, eyeY, eyeZ
                _center.x, _center.y, _center.z, //centerX, centerY, centerZ
                0f, 1f, 0f); //upX,  upY, upZ

        synchronized (this) {

            multiplyMM(_viewRotateMatrix, 0, _rotationMatrix, 0, _viewMatrix, 0);

        }
    }

    /**
     * 处理移动
     */
    private void _handleMove() {
        if (0 == _moveFlag) {
            return;
        }
        Vector face = _eye.sub(_center).normalize().scale(20);
        Vector rms;
        float yV;
        synchronized (this) {
            rms = face.multiplyMatrix(_RRMatrix);
        }
        if (rms.z < 0) {
            yV = -rms.y;
        } else {
            yV = rms.y;
        }
        rms = new Vector(rms.x, yV, -rms.z);
        if (1 == _moveFlag) {
            _pos = _pos.add(rms);
        } else if (2 == _moveFlag) {
            _pos = _pos.sub(rms);
        }
    }

    /**
     * 将构建的对象放置在场景中
     */
    private void _positionObjectInScene(float x, float y, float z) {
        setIdentityM(_modelMatrix, 0);
        /**
         * 把模型通过旋转矩阵旋转到和世界坐标系同调后发现绕X轴有顺时针旋转了90度
         * 这里把它旋转回来
         */
        rotateM(_modelMatrix, 0, 90, 1, 0, 0);
        translateM(_modelMatrix, 0, x, y, z);
        multiplyMM(_MVPMatrix, 0, _VPMatrix, 0, _modelMatrix, 0);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            synchronized (this) {
                SensorManager.getRotationMatrixFromVector(_rotationMatrix,
                        event.values);
                _setupMatrix(_width, _height);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public class MyRenderer implements Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {

            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);

            multiplyMM(_VPMatrix, 0, _projectionMatrix, 0, _viewRotateMatrix, 0);
            invertM(_IVPMatrix, 0, _VPMatrix, 0);

            _handleMove();

            _positionObjectInScene(_pos.x, _pos.y, _pos.z);
            _diffuseShaderProgram.useProgram();
            _diffuseShaderProgram.setMatrix(_MVPMatrix);
            _diffuseShaderProgram.setMaterial(0.204f, 0.455f, 0.549f);
            _diffuseShaderProgram.setLightAmbient(1, 1, 1);
            _tide.update();
            _tide.bindData(_diffuseShaderProgram);
            _tide.draw(_drawLines);

            if(_drawNormal){
                _positionObjectInScene(_pos.x, _pos.y, _pos.z);
                _rayShaderProgram.useProgram();
                _rayShaderProgram.setUniforms(_MVPMatrix, 1f, 1f, 0f, 0f);
                _tide.drawNormal(_rayShaderProgram);
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            _width = width;
            _height = height;
            _setupMatrix(width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0, 0.1f, 0.1f, 1);
            _diffuseShaderProgram = new CB_5_Perv_Diffuse_ShaderProgram(
                    ActivityGLES_CB_5_2_PerVertex_Diffuse.this);
            _rayShaderProgram = new RayShaderProgram(
                    ActivityGLES_CB_5_2_PerVertex_Diffuse.this);

            setIdentityM(_rotationMatrix, 0);
            _tide = new PerlinTide(100, 100, 5, 200);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = _detector.onTouchEvent(event);
        if(MotionEvent.ACTION_UP == event.getAction()){
            _moveFlag = 0;
        }
        return ret;
    }

    class MyGestureListener extends SimpleOnGestureListener{

        

        @Override
        public void onLongPress(MotionEvent event) {
            float x = event.getX();

            if (x < _width / 2) {
                _moveFlag = 1;
            } else {
                _moveFlag = 2;
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }
}
