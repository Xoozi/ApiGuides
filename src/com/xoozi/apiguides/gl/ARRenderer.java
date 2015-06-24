package com.xoozi.apiguides.gl;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.xoozi.apiguides.R;
import com.xoozi.apiguides.gl.glutil.Geometry;
import com.xoozi.apiguides.gl.glutil.Geometry.Point;
import com.xoozi.apiguides.gl.glutil.Geometry.Vector;
import com.xoozi.apiguides.gl.glutil.Geometry.Plane;
import com.xoozi.apiguides.gl.glutil.Geometry.Sphere;
import com.xoozi.apiguides.gl.glutil.TextureHelper;
import com.xoozi.apiguides.gl.glutil.Geometry.Ray;
import com.xoozi.apiguides.gl.objects.Heightmap;
import com.xoozi.apiguides.gl.objects.Mallet;
import com.xoozi.apiguides.gl.objects.ParticleShooter;
import com.xoozi.apiguides.gl.objects.ParticleSystem;
import com.xoozi.apiguides.gl.objects.Puck;
import com.xoozi.apiguides.gl.objects.Table;
import com.xoozi.apiguides.gl.objects.TouchRay;
import com.xoozi.apiguides.gl.programs.ColorShaderProgram;
import com.xoozi.apiguides.gl.programs.HeightmapShaderProgram;
import com.xoozi.apiguides.gl.programs.ParticleShaderProgram;
import com.xoozi.apiguides.gl.programs.RayShaderProgram;
import com.xoozi.apiguides.gl.programs.TextureShaderProgram;

import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;
/**
 * 在Camera预览渲染之后，渲染增强现实
 * 部分的渲染器
 */
public class ARRenderer{
    private final float[] _modelMatrix = new float[16];
    private final float[] _projectionMatrix = new float[16];
    private final float[] _viewMatrix = new float[16];
    private final float[] _viewRotateMatrix = new float[16];
    private final float[] _VPMatrix = new float[16];
    private final float[] _IVPMatrix= new float[16];
    private final float[] _MVPMatrix = new float[16];
    private final float[] _rotationMatrix = new float[16];

    private Table       _table;
    private Mallet      _mallet;
    private Puck        _puck;
    private TouchRay    _touthRay;


    //对方向关系有点晕，把三轴画出来
    private TouchRay    _rayCoordinate[]    = new TouchRay[3];
    
    private ParticleSystem  _particleSystem;
    private ParticleShooter _greenShooter;

    private Heightmap       _heightmap;

    private int     _textureTable;
    private int     _textureParticle;
    private TextureShaderProgram    _textureProgram;
    private ColorShaderProgram      _colorProgram;
    private RayShaderProgram        _rayProgram;
    private ParticleShaderProgram   _particleProgram;
    private HeightmapShaderProgram  _heightmapProgram;


    private int _width;
    private int _height;

    private final float _leftBound = -0.5f;
    private final float _rightBound= 0.5f;
    private final float _farBound = -0.8f;
    private final float _nearBound = 0.8f;
    
    private long        _globalStartTime;

    private boolean _malletPressed = false;
    private Point   _blueMalletPosition;
    private Point   _prevBlueMalletPosition;

    private Point   _puckPosition;
    private Vector  _puckVector;

    public ARRenderer(Context context){
        _rayCoordinate[0] = new TouchRay(new Ray(new Point(0, 0, 0),
                                new Vector(10f, 0f, 0f)));
        _rayCoordinate[1] = new TouchRay(new Ray(new Point(0, 0, 0),
                                new Vector(0f, 10f, 0f)));
        _rayCoordinate[2] = new TouchRay(new Ray(new Point(0, 0, 0),
                                new Vector(0f, 0f, 10f)));

        _table  = new Table();
        _mallet = new Mallet(0.08f, 0.15f, 32);
        _puck   = new Puck(0.06f, 0.02f, 32);
        _particleSystem = new ParticleSystem(10000);
        _globalStartTime= System.nanoTime();


        final Vector particleDirection = new Vector(0f, 0.5f, 0f);
        _greenShooter = new ParticleShooter(
                            new Point(0f, 0f, 0f),
                            particleDirection,
                            0x00ee2020,
                            5,
                            1f);

        _heightmap          = new Heightmap(((BitmapDrawable)context.getResources()
                    .getDrawable(R.drawable.heightmap)).getBitmap());

        _blueMalletPosition = new Point(0f, _mallet.height / 2f, 0.4f);
        _puckPosition = new Point(0f, _puck.height / 2f, 0f);
        _puckVector   = new Vector(0, 0, 0);

        _textureProgram     = new TextureShaderProgram(context);
        _colorProgram       = new ColorShaderProgram(context);
        _rayProgram         = new RayShaderProgram(context);
        _particleProgram    = new ParticleShaderProgram(context);
        _heightmapProgram   = new HeightmapShaderProgram(context);

        _textureTable        = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
        _textureParticle     = TextureHelper.loadTexture(context, R.drawable.particle_texture);

        setIdentityM(_rotationMatrix, 0);

    }

    public void draw(){

        multiplyMM(_VPMatrix, 0, _projectionMatrix, 0, _viewRotateMatrix, 0);
        invertM(_IVPMatrix, 0, _VPMatrix, 0);

        //画3个轴
        {
            _positionObjectInScene(0, 0, 0);
            _rayProgram.useProgram();
            _rayProgram.setUniforms(_MVPMatrix, 1f, 1f, 0f, 0f);
            _rayCoordinate[0].bindData(_rayProgram);
            _rayCoordinate[0].draw();

            _positionObjectInScene(0, 0, 0);
            _rayProgram.useProgram();
            _rayProgram.setUniforms(_MVPMatrix, 1f, 0f, 1f, 0f);
            _rayCoordinate[1].bindData(_rayProgram);
            _rayCoordinate[1].draw();

            _positionObjectInScene(0, 0, 0);
            _rayProgram.useProgram();
            _rayProgram.setUniforms(_MVPMatrix, 1f, 0f, 0f, 1f);
            _rayCoordinate[2].bindData(_rayProgram);
            _rayCoordinate[2].draw();
        }

        _positionTableInScene();
        _textureProgram.useProgram();
        _textureProgram.setUniforms(_MVPMatrix, _textureTable);
        _table.bindData(_textureProgram);
        _table.draw();

        _positionObjectInScene(0, _mallet.height / 2f, -0.4f);
        _colorProgram.useProgram();
        _colorProgram.setUniforms(_MVPMatrix, 0.8f, 1f, 0f, 0f);
        _mallet.bindData(_colorProgram);
        _mallet.draw();

        _positionObjectInScene(_blueMalletPosition.x,
                               _blueMalletPosition.y,
                               _blueMalletPosition.z);
        _colorProgram.useProgram();
        _colorProgram.setUniforms(_MVPMatrix, 0.8f, 0f, 0f, 1f);
        _mallet.bindData(_colorProgram);
        _mallet.draw();

        //_puckVector = _puckVector.scale(0.99f);
        _puckPosition = _puckPosition.translate(_puckVector);
        if(_puckPosition.x < _leftBound + _puck.radius ||
                _puckPosition.x > _rightBound - _puck.radius){
            _puckVector = new Vector(-_puckVector.x, _puckVector.y, _puckVector.z);
            //_puckVector = _puckVector.scale(0.90f);
        }
        if(_puckPosition.z < _farBound + _puck.radius ||
                _puckPosition.z > _nearBound - _puck.radius){
            _puckVector = new Vector(_puckVector.x, _puckVector.y, -_puckVector.z);
            //_puckVector = _puckVector.scale(0.90f);
        }
        _puckPosition = new Point(
                _clamp(_puckPosition.x, _leftBound + _puck.radius, _rightBound - _puck.radius),
                _puckPosition.y,
                _clamp(_puckPosition.z, _farBound + _puck.radius, _nearBound - _puck.radius));
        _positionObjectInScene(_puckPosition.x, _puckPosition.y, _puckPosition.z);
        _colorProgram.useProgram();
        _colorProgram.setUniforms(_MVPMatrix, 0.8f, 0.8f, 0.8f, 1f);
        _puck.bindData(_colorProgram);
        _puck.draw();

        if(null != _touthRay){
            _positionObjectInScene(0, 0, 0);
            _rayProgram.useProgram();
            _rayProgram.setUniforms(_MVPMatrix, 1f, 0f, 1f, 0f);
            _touthRay.bindData(_rayProgram);
            _touthRay.draw();
        }

        _positionObjectInScene(-4f, 0f, 0f);
        float currentTime = (System.nanoTime() - _globalStartTime) / 1000000000f;
        _greenShooter.addParticles(_particleSystem, currentTime, 5);
        _particleProgram.useProgram();
        _particleProgram.setUniforms(_MVPMatrix, currentTime, _textureParticle);
        _particleSystem.bindData(_particleProgram);
        _particleSystem.draw();


        _positionObjectInScene(-1f, 0f, 1f);
        _heightmapProgram.useProgram();
        _heightmapProgram.setUniforms(_MVPMatrix);
        _heightmap.bindData(_heightmapProgram);
        _heightmap.draw();
    }


    public void onSurfaceChanged(int width, int height) {
        _width = width;
        _height = height;
        _setupMatrix(width, height);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            synchronized(this){
                SensorManager.getRotationMatrixFromVector(
                        _rotationMatrix , event.values);
                rotateM(_rotationMatrix, 0, 90f, 1f, 0f, 0f);
                _setupMatrix(_width, _height);
            }
        }
    }

    public void handleTouchPress(float x, float y){

        Ray ray = _convertNormalized2DPointToRay(x, y);
        Sphere malletBoundingSphere = new Sphere(
                new Point(_blueMalletPosition.x,
                          _blueMalletPosition.y,
                          _blueMalletPosition.z),
                _mallet.height / 2f);
        _malletPressed = Geometry.intersects(malletBoundingSphere, ray);

        _touthRay = new TouchRay(ray);
    }

    public void handleTouchDrag(float x, float y){
        if(_malletPressed){
            Ray ray = _convertNormalized2DPointToRay(x, y);
            _touthRay = new TouchRay(ray);

            Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 1, 0));
            Point touchPoint = Geometry.intersectionPoint(ray, plane);
            _prevBlueMalletPosition = _blueMalletPosition;
            _blueMalletPosition = new Point(
                    _clamp(touchPoint.x, _leftBound + _mallet.radius, _rightBound - _mallet.radius),
                    _mallet.height / 2f,
                    _clamp(touchPoint.z, 0 + _mallet.radius, _nearBound - _mallet.radius));


            float distance = Geometry.vectorBetween(_blueMalletPosition, _puckPosition).length();

            if(distance < (_puck.radius + _mallet.radius)){
                _puckVector = Geometry.vectorBetween(_prevBlueMalletPosition, _blueMalletPosition);
            }
        }
    }

    /**
     * 准备各矩阵
     */
    private void _setupMatrix(int width, int height){

        perspectiveM(_projectionMatrix, 0, 45, (float)width/(float)height, 1f, 10f);

        setLookAtM(_viewMatrix, 0, 
                //2f, 2f, 2f, //eyeX, eyeY, eyeZ
                0f, 1.2f, 2.2f, //eyeX, eyeY, eyeZ
                //0f, 1.2f, 0f, //eyeX, eyeY, eyeZ
                0f, 0f, 0f,     //centerX, centerY, centerZ
                //0f, 0f, 1f);    //upX,  upY, upZ
                0f, 1f, 0f);    //upX,  upY, upZ
                
        synchronized(this){

            multiplyMM(_viewRotateMatrix, 0, _rotationMatrix, 0, _viewMatrix, 0);
        }
    }

    /**
     * 将桌子放置在场景中
     */
    private void    _positionTableInScene(){
        setIdentityM(_modelMatrix, 0);
        rotateM(_modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(_MVPMatrix, 0, _VPMatrix, 0, _modelMatrix, 0);
    }

    /**
     * 将构建的对象放置在场景中
     */
    private void    _positionObjectInScene(float x, float y, float z){
        setIdentityM(_modelMatrix, 0);
        translateM(_modelMatrix, 0, x, y, z);
        multiplyMM(_MVPMatrix, 0, _VPMatrix, 0, _modelMatrix, 0);
    }


    private Ray     _convertNormalized2DPointToRay(float normalizedX, float normalizedY){
        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc = {normalizedX, normalizedY, 1, 1};
        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(nearPointWorld, 0, _IVPMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointWorld, 0, _IVPMatrix, 0, farPointNdc, 0);

        _divideByW(nearPointWorld);
        _divideByW(farPointWorld);

        Point nearPointRay = new Point(nearPointWorld);
        Point farPointRay = new Point(farPointWorld);
        return new Ray(nearPointRay,
                Geometry.vectorBetween(
                    nearPointRay,
                    farPointRay));
    }

    private void    _divideByW(float[] vector){
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    private float _clamp(float value, float min, float max){
        return Math.min(max, Math.max(value, min));
    }
}
