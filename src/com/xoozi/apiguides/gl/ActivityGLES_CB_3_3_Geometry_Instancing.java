package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.xoozi.apiguides.gl.glutil.Geometry.Vector;
import com.xoozi.apiguides.gl.objects.DiceRaid;
import com.xoozi.apiguides.gl.objects.GridVAO;
import com.xoozi.apiguides.gl.programs.CB_3_Geometry_Instance_ShaderProgram;
import com.xoozi.apiguides.gl.programs.ColorShaderProgram;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.GL_CULL_FACE;
import static android.opengl.GLES30.GL_FRONT;
import static android.opengl.GLES30.GL_BACK;
import static android.opengl.GLES30.glViewport;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glCullFace;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ActivityGLES_CB_3_3_Geometry_Instancing extends Activity implements
        SensorEventListener, OnTouchListener {
    private Sensor _rotationVectorSensor;
    private SensorManager _sensorManager;

    private GLSurfaceView _glSurfaceView;
    private CB_3_Geometry_Instance_ShaderProgram _GIShaderProgram;
    private ColorShaderProgram _colorShaderProgram;

    private final float[] _modelMatrix = new float[16];
    private final float[] _projectionMatrix = new float[16];
    private final float[] _viewMatrix = new float[16];
    private final float[] _viewRotateMatrix = new float[16];
    private final float[] _VPMatrix = new float[16];
    private final float[] _IVPMatrix = new float[16];
    private final float[] _MVPMatrix = new float[16];
    private final float[] _rotationMatrix = new float[16];

    private DiceRaid    _diceRaid;
    private GridVAO     _grid;

    private int _width;
    private int _height;

    private Vector _center;
    private Vector _eye = new Vector(0, 0, 200);
    private Vector _pos = new Vector(0, 0, 0);
    private boolean _toggle = false;

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

        _adjCenter();
    }
    private void _adjCenter(){
        _center = new Vector(0f, _eye.y, _eye.z - 100);
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
                1f, 10000f);


        setLookAtM(_viewMatrix, 0, 
                _eye.x,     _eye.y,     _eye.z, //eyeX, eyeY, eyeZ
                _center.x,  _center.y,  _center.z, //centerX, centerY, centerZ
                0f, 1f, 0f); //upX,  upY, upZ

        synchronized (this) {

            multiplyMM(_viewRotateMatrix, 0, _rotationMatrix, 0, _viewMatrix, 0);
        }
    }

    private void _handleTouchPress(float x, float y) {

        _toggle = !_toggle;
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
            glEnable(GL_CULL_FACE);

            if(_toggle){
                glCullFace(GL_FRONT);
            }else{
                glCullFace(GL_BACK);
            }

            multiplyMM(_VPMatrix, 0, _projectionMatrix, 0, _viewRotateMatrix, 0);
            invertM(_IVPMatrix, 0, _VPMatrix, 0);



            /*if(_toggle){
                //_GIShaderProgram.useProgram();
                Utils.amLog("drawInstance");
                _diceRaid.drawInstance(_VPMatrix, _modelMatrix);
            }else{*/



                _GIShaderProgram.useProgram();
                _GIShaderProgram.setUniforms(_MVPMatrix);
                _positionObjectInScene(_pos.x, _pos.y, _pos.z);
                _diceRaid.draw(_MVPMatrix);
                //_diceRaid.drawInstance(_VPMatrix, _modelMatrix);

                _colorShaderProgram.useProgram();
                _positionObjectInScene(_pos.x, _pos.y, _pos.z);
                _colorShaderProgram.setUniforms(_MVPMatrix, 1, 0.3f, 0.5f, 0.7f);
                _grid.draw();
            //}
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
            _GIShaderProgram = new CB_3_Geometry_Instance_ShaderProgram(
                    ActivityGLES_CB_3_3_Geometry_Instancing.this);

            _colorShaderProgram = new ColorShaderProgram(
                    ActivityGLES_CB_3_3_Geometry_Instancing.this);

            setIdentityM(_rotationMatrix, 0);
            _colorShaderProgram.useProgram();
            _diceRaid = new DiceRaid(_colorShaderProgram, 20);

            _grid = new GridVAO(_colorShaderProgram, 2000, 2000, 200, 200);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        _handleTouchPress(event.getX(), event.getY());
        return false;
    }
}
