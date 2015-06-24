package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.programs.CB_1_ShaderProgram;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glViewport;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.opengl.GLSurfaceView.Renderer;

public class ActivityGLES_CB_1_Triangle extends Activity {

    private GLSurfaceView           _glSurfaceView;
    private CB_1_ShaderProgram      _cb1ShaderProgram;
    private final float _vertices[] = { 0.0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f };
    private final float _colors[]   = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };

    private float _degree = 0;
    private float _radian;

    private VertexArray _verData = new VertexArray(_vertices);
    private VertexArray _colData = new VertexArray(_colors);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(3);
        _glSurfaceView.setRenderer(new MyRenderer());
        setContentView(_glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _glSurfaceView.onPause();
    }

    public class MyRenderer implements Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            _cb1ShaderProgram.useProgram();
            _cb1ShaderProgram.setUniforms(_radian);
            _verData.setVertexAttributePointer(0,
                    _cb1ShaderProgram.getPositionAttributeLocation(), 2, 0);
            _colData.setVertexAttributePointer(0,
                    _cb1ShaderProgram.getColorAttributeLocation(), 3, 0);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            _radian = _degree++/57.2957795f;
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0, 0.1f, 0.1f, 1);
            _cb1ShaderProgram = new CB_1_ShaderProgram(
                    ActivityGLES_CB_1_Triangle.this);
        }

    }
}
