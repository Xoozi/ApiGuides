package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.SensorEvent;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CameraGLSView extends GLSurfaceView 
    implements Renderer,
    SurfaceTexture.OnFrameAvailableListener {

    private Context        _context;
    private SurfaceTexture _surface;
    private CameraRenderer _cameraRenderer;
    private ARRenderer     _arRenderer;
    private int _textureID = -1;

    public CameraGLSView(Context context){
        super(context);
        _context = context;
        setEGLContextClientVersion(3);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        //setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public CameraGLSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        //setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glEnable(GLES30.GL_CULL_FACE);
        _textureID = _createTextureID();
        _surface = new SurfaceTexture(_textureID);
        _surface.setOnFrameAvailableListener(this);
        _cameraRenderer = new CameraRenderer(_context, _textureID);
        _arRenderer = new ARRenderer(_context);
        CameraInterface.getInstance().doOpenCamera(null);
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        if(!CameraInterface.getInstance().isPreviewing()){
            CameraInterface.getInstance().doStartPreview(_surface, 1.33f);
        }

        _arRenderer.onSurfaceChanged(width, height);
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        _surface.updateTexImage();
        float[] mtx = new float[16];
        _surface.getTransformMatrix(mtx);
        _cameraRenderer.draw();

        _arRenderer.draw();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        CameraInterface.getInstance().doStopCamera();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // TODO Auto-generated method stub
        this.requestRender();
    }

    public void onSensorChanged(SensorEvent event) {
        _arRenderer.onSensorChanged(event);
    }

    private int _createTextureID()
    {
        int[] texture = new int[1];

        GLES30.glGenTextures(1, texture, 0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);        
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }
    public SurfaceTexture _getSurfaceTexture(){
        return _surface;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != event){
            final float normalizedX = (event.getX() / (float) getWidth()) * 2.0f - 1f;
            final float normalizedY = -((event.getY() / (float) getHeight()) * 2.0f - 1f);

            if(MotionEvent.ACTION_DOWN == event.getAction()){
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        _arRenderer.handleTouchPress(normalizedX, normalizedY);
                    }
                });

                return true;
            }else if(MotionEvent.ACTION_MOVE == event.getAction()){
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        _arRenderer.handleTouchDrag(normalizedX, normalizedY);
                    }
                });

                return true;
            }
        }
        
        return false;
    }

}
