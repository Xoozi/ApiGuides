package com.xoozi.apiguides.sensor;


import com.xoozi.apiguides.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class ActivitySensorRotation extends Activity implements 
    SensorEventListener {

    private TextView        _lableXe;
    private TextView        _lableYe;
    private TextView        _lableZe;
    private SensorManager   _sensorManager;
    private Sensor          _rotationVectorSensor;
    private Sensor          _accelerometerSensor;
    private final float[]   _accs = new float[4];

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_rotation);

        _lableXe    = (TextView)findViewById(R.id.lable_x_e);
        _lableYe    = (TextView)findViewById(R.id.lable_y_e);
        _lableZe    = (TextView)findViewById(R.id.lable_z_e);

        _sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        _rotationVectorSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        _accelerometerSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        _accs[0] = 0;
        _accs[1] = 0;
        _accs[2] = 0;
        _accs[3] = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        _sensorManager.registerListener(this, _rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _sensorManager.unregisterListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            _do(event);
        }else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
             
            _accs[0] = event.values[0];
            _accs[1] = event.values[1];
            _accs[2] = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }


    private void _do(SensorEvent event){
        double x0, x1, x2;
        double y0, y1, y2;
        double z0, z1, z2;
        double o0, o1, o2;
        double ax, ay, az;
        double a0, a1, a2;
        double tmp;

        float[] gg = new float[16];
        float[] g = new float[3];
        SensorManager.getRotationMatrixFromVector(gg, event.values);
        SensorManager.getOrientation(gg, g);
        o0 = g[0];
        o1 = g[1];
        o2 = g[2];

        ax = _accs[0];
        ay = _accs[1];
        az = _accs[2];

        y0 = - Math.sin(o1);
        y1 = Math.cos(o1)*Math.cos(o0);
        y2 = Math.cos(o1)*Math.sin(o0);

        tmp = Math.acos(-(Math.tan(o1)*Math.tan(o2)));
        x0 = -Math.sin(o2);
        x1 = Math.cos(o2)*Math.cos(o0+tmp);
        x2 = Math.cos(o2)*Math.sin(o0+tmp);

        z0 = x2*y1 - x1*y2;
        z1 = x0*y2 - x2*y0;
        z2 = x1*y0 - x0*y1;

        a0 = ax*x0 + ay*y0 + az*z0;
        a1 = ax*x1 + ay*y1 + az*z1;
        a2 = ax*x2 + ay*y2 + az*z2;
        
        _lableXe.setText("Xe:"+a2);
        _lableYe.setText("Ye:"+a1);
        _lableZe.setText("Ze:"+a0);
    }

}
