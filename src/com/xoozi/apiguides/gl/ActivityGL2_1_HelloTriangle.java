package com.xoozi.apiguides.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.programs.ColorShaderProgram;

import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glViewport;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.Matrix.setIdentityM;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.opengl.GLSurfaceView.Renderer;

public class ActivityGL2_1_HelloTriangle extends Activity{

    private GLSurfaceView       _glSurfaceView;
    private ColorShaderProgram  _colorShaderProgram;
    private final float     _vertices[] = {-0.5f,   -0.5f,      0.0f,
                                           0.5f,    0.5f,       0.0f,
                                           0.f,     -0.6f,      0.0f};
    private float[]         _matrix = new float[16];

    private VertexArray     _verData = new VertexArray(_vertices);


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
            _colorShaderProgram.useProgram();
            _colorShaderProgram.setUniforms(_matrix, 0, 0.5f, 0.1f, 0.2f);
            _verData.setVertexAttributePointer(0, _colorShaderProgram.getPositionAttributeLocation(),
                                                3, 0);
            glDrawArrays(GL_TRIANGLES, 0, 6);            
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0, 0, 0, 1);
            _colorShaderProgram = new ColorShaderProgram(ActivityGL2_1_HelloTriangle.this);
            setIdentityM(_matrix, 0);
        }

    }
}
