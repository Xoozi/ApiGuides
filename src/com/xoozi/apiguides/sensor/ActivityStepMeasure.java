package com.xoozi.apiguides.sensor;


import com.xoozi.apiguides.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityStepMeasure extends Activity{

    private SensorManager               _sensorManager;
    private AccelerationView            _av;
    private LandSurveyView              _sv;
    private StepMeasureTool             _stepMeasure = new StepMeasureTool();
    private AccelerationSensorListener  _asl = new AccelerationSensorListener();
    private OrientationSensorListener   _osl = new OrientationSensorListener();
    private TextView            _lableStepCount;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_stepmeasure);
        _av = (AccelerationView)findViewById(R.id.acc_view);
        float threshold = ((float)(SensorManager.STANDARD_GRAVITY * 0.2));
        _av.setThreshold(threshold);

        _sv = (LandSurveyView)findViewById(R.id.survey_view);
        
        _stepMeasure.setThreshold(threshold);
        _lableStepCount = (TextView)findViewById(R.id.lable_step_count);
        _sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        _sensorManager.registerListener(_asl,
                _sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_FASTEST);
        _sensorManager.registerListener(_osl,
                _sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    protected void onStop() {
        _sensorManager.unregisterListener(_asl);
        _sensorManager.unregisterListener(_osl);
        super.onStop();
    }
    
    public class AccelerationSensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            _stepMeasure.update(event.timestamp,
                                event.values[0],
                                event.values[1],
                                event.values[2]);
            _lableStepCount.setText(String.valueOf(_stepMeasure.getStepCount()));
            _av.onSensorChanged(event);
        }
    }
    public class OrientationSensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            _sv.onOrientationChanged(event);
        }
    }

    public  class StepMeasureTool{
        private long        _timestamp = 0;
        private int         _stepCount = 0;
        private boolean     _lock = false;
        private float       _accZPrev = 0;
        private float       _threshold;

        public void update(long timestamp, float accX, float accY, float accZ){
            if(!_lock && _check(accZ))
                _lock = true;
            else if(_lock && _check(accZ)){
                _lock = false;

                if(((timestamp - _timestamp)/1000000) > 700){
                    _stepCount++;
                    _sv.onStep();
                    _timestamp= timestamp;
                }
            }
            _accZPrev = accZ;
        }

        private boolean _check(float accZ){
            if(_accZPrev < _threshold &&
                    accZ > _threshold){
                return true;
            }else if(accZ < _threshold &&
                    _accZPrev > _threshold){
                return true; 
            }else{
                return false;
            }
        }

        public void setThreshold(float t){
            _threshold = t;
        }

        public float    getThreshold(){
            return _threshold;
        }

        public int  getStepCount(){
            return _stepCount;
        }

        public void reset(){
            _stepCount = 0;
        }
    }
}
