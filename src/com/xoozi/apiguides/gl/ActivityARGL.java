package com.xoozi.apiguides.gl;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class ActivityARGL extends Activity
        implements SensorEventListener{
    private CameraGLSView _cameraGLSView;
    private Sensor _rotationVectorSensor;
    private SensorManager _sensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        _rotationVectorSensor = _sensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);

        _cameraGLSView = new CameraGLSView(this);
        setContentView(_cameraGLSView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _cameraGLSView.onResume();
        _sensorManager.registerListener(this, _rotationVectorSensor, 
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _cameraGLSView.onPause();
        _sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        _cameraGLSView.onSensorChanged(event);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
