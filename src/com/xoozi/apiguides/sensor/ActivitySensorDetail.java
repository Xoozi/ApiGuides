package com.xoozi.apiguides.sensor;


import java.util.List;

import com.xoozi.apiguides.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivitySensorDetail extends Activity
    implements SensorEventListener{

    public static final String  KEY_SENSOR_NAME = "sensor_name";
    
    private static final String THETA = "\u0398";
    private static final String ACCELERATION_UNITS = "m/s\u00B2";

    private String      _sensorName;
    private String[]    _types;
    
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView name;
    private TextView type;
    private TextView maxRange;
    private TextView minDelay;
    private TextView power;
    private TextView resolution;
    private TextView vendor;
    private TextView version;
    private TextView accuracy;
    private TextView timestampLabel;
    private TextView timestamp;
    private TextView timestampUnits;
    private TextView dataLabel;
    private TextView dataUnits;
    private TextView xAxis;
    private TextView xAxisLabel;
    private TextView yAxis;
    private TextView yAxisLabel;
    private TextView zAxis;
    private TextView zAxisLabel;
    private TextView singleValue;
    private TextView cosLabel;
    private TextView cos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        _initWork(); 
    }


    private void _initWork(){
        Intent  data = getIntent();
        _sensorName = data.getStringExtra(KEY_SENSOR_NAME);

        _types = getResources().getStringArray(R.array.sensor_type);

        name = (TextView) findViewById(R.id.name);
        type = (TextView) findViewById(R.id.type);
        maxRange = (TextView) findViewById(R.id.maxRange);
        minDelay = (TextView) findViewById(R.id.minDelay);
        power = (TextView) findViewById(R.id.power);
        resolution = (TextView) findViewById(R.id.resolution);
        vendor = (TextView) findViewById(R.id.vendor);
        version = (TextView) findViewById(R.id.version);
        accuracy = (TextView) findViewById(R.id.accuracy);
        timestampLabel = (TextView) findViewById(R.id.timestampLabel);
        timestamp = (TextView) findViewById(R.id.timestamp);
        timestampUnits = (TextView) findViewById(R.id.timestampUnits);
        dataLabel = (TextView) findViewById(R.id.dataLabel);
        dataUnits = (TextView) findViewById(R.id.dataUnits);
        xAxis = (TextView) findViewById(R.id.xAxis);
        xAxisLabel = (TextView) findViewById(R.id.xAxisLabel);
        yAxis = (TextView) findViewById(R.id.yAxis);
        yAxisLabel = (TextView) findViewById(R.id.yAxisLabel);
        zAxis = (TextView) findViewById(R.id.zAxis);
        zAxisLabel = (TextView) findViewById(R.id.zAxisLabel);
        singleValue = (TextView) findViewById(R.id.singleValue);
        cosLabel = (TextView) findViewById(R.id.cosLabel);
        cos = (TextView) findViewById(R.id.cos);
        
        findViewById(R.id.delayFastest).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sensorManager.unregisterListener(ActivitySensorDetail.this);
                sensorManager.registerListener(ActivitySensorDetail.this,
                        sensor, 
                        SensorManager.SENSOR_DELAY_FASTEST);
            }
        });
        
        findViewById(R.id.delayGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sensorManager.unregisterListener(ActivitySensorDetail.this);
                sensorManager.registerListener(ActivitySensorDetail.this,
                        sensor, 
                        SensorManager.SENSOR_DELAY_GAME);
            }
        });
        
        findViewById(R.id.delayNormal).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sensorManager.unregisterListener(ActivitySensorDetail.this);
                sensorManager.registerListener(ActivitySensorDetail.this,
                        sensor, 
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        });
        
        findViewById(R.id.delayUi).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sensorManager.unregisterListener(ActivitySensorDetail.this);
                sensorManager.registerListener(ActivitySensorDetail.this,
                        sensor, 
                        SensorManager.SENSOR_DELAY_UI);
            }
        });

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor>    sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor s:sensorList){
            if(s.getName().equals(_sensorName))
                sensor = s;
        }

        name.setText(sensor.getName());
        type.setText(_types[sensor.getType()-1]);
        maxRange.setText(String.valueOf(sensor.getMaximumRange()));
        minDelay.setText(String.valueOf(sensor.getMinDelay()));
        power.setText(String.valueOf(sensor.getPower()));
        resolution.setText(String.valueOf(sensor.getResolution()));
        vendor.setText(String.valueOf(sensor.getVendor()));
        version.setText(String.valueOf(sensor.getVersion()));
        
        sensorManager.registerListener(this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void displaySensor(Sensor sensor)
    {
        
        this.sensor = sensor;
        
        name.setText(sensor.getName());
        type.setText(String.valueOf(sensor.getType()));
        maxRange.setText(String.valueOf(sensor.getMaximumRange()));
        minDelay.setText(String.valueOf(sensor.getMinDelay()));
        power.setText(String.valueOf(sensor.getPower()));
        resolution.setText(String.valueOf(sensor.getResolution()));
        vendor.setText(String.valueOf(sensor.getVendor()));
        version.setText(String.valueOf(sensor.getVersion()));
        
        sensorManager.registerListener(this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        switch(accuracy)
        {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                this.accuracy.setText("SENSOR_STATUS_ACCURACY_HIGH");
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                this.accuracy.setText("SENSOR_STATUS_ACCURACY_MEDIUM");
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                this.accuracy.setText("SENSOR_STATUS_ACCURACY_LOW");
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                this.accuracy.setText("SENSOR_STATUS_UNRELIABLE");
                break;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        onAccuracyChanged(event.sensor, event.accuracy);
        
        timestampLabel.setVisibility(View.VISIBLE);
        timestamp.setVisibility(View.VISIBLE);
        timestamp.setText(String.valueOf(event.timestamp));
        timestampUnits.setVisibility(View.VISIBLE);
        
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                showEventData("Acceleration - gravity on axis",
                        ACCELERATION_UNITS,
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                break;
                
            case Sensor.TYPE_MAGNETIC_FIELD:
                showEventData("Abient Magnetic Field",
                        "uT",
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                break;
            case Sensor.TYPE_GYROSCOPE:
                showEventData("Angular speed around axis",
                        "radians/sec",
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                break;
            case Sensor.TYPE_LIGHT:
                showEventData("Ambient light",
                        "lux",
                        event.values[0]);
                break;
            case Sensor.TYPE_PRESSURE:
                showEventData("Atmospheric pressure",
                        "hPa",
                        event.values[0]);
                break;
            case Sensor.TYPE_PROXIMITY:
                showEventData("Distance",
                        "cm",
                        event.values[0]);
                break;
            case Sensor.TYPE_GRAVITY:
                showEventData("Gravity",
                        ACCELERATION_UNITS,
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                showEventData("Acceleration (not including gravity)",
                        ACCELERATION_UNITS,
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                
                showEventData("Rotation Vector",
                        null,
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                
                xAxisLabel.setText("x*sin(" + THETA + "/2)");
                yAxisLabel.setText("y*sin(" + THETA + "/2)");
                zAxisLabel.setText("z*sin(" + THETA + "/2)");
                
                if (event.values.length == 4)
                {
                    cosLabel.setVisibility(View.VISIBLE);
                    cos.setVisibility(View.VISIBLE);
                    cos.setText(String.valueOf(event.values[3]));
                }
                
                break;
            case Sensor.TYPE_ORIENTATION:
                showEventData("Angle",
                        "Degrees",
                        event.values[0],
                        event.values[1],
                        event.values[2]);
                
                xAxisLabel.setText("Bearing:");
                yAxisLabel.setText("Pitch:");
                zAxisLabel.setText("Roll:");
                
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                showEventData("Relatice ambient air humidity",
                        "%",
                        event.values[0]);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                showEventData("Ambien temperature",
                        "degree Celcius",
                        event.values[0]);
                break;
        }
    }
    
    private void showEventData(String label, String units, float x, float y, float z)
    {
        dataLabel.setVisibility(View.VISIBLE);
        dataLabel.setText(label);
        
        if (units == null)
        {
            dataUnits.setVisibility(View.GONE);
        }
        else
        {
            dataUnits.setVisibility(View.VISIBLE);
            dataUnits.setText("(" + units + "):");
        }
        
        singleValue.setVisibility(View.GONE);
        
        xAxisLabel.setVisibility(View.VISIBLE);
        xAxisLabel.setText("X Axis:");
        xAxis.setVisibility(View.VISIBLE);
        xAxis.setText(String.valueOf(x));
        
        yAxisLabel.setVisibility(View.VISIBLE);
        yAxisLabel.setText("Y Axis:");
        yAxis.setVisibility(View.VISIBLE);
        yAxis.setText(String.valueOf(y));
        
        zAxisLabel.setVisibility(View.VISIBLE);
        zAxisLabel.setText("Z Axis:");
        zAxis.setVisibility(View.VISIBLE);
        zAxis.setText(String.valueOf(z));
    }
    
    private void showEventData(String label, String units, float value)
    {
        dataLabel.setVisibility(View.VISIBLE);
        dataLabel.setText(label);
        
        dataUnits.setVisibility(View.VISIBLE);
        dataUnits.setText("(" + units + "):");
        
        singleValue.setVisibility(View.VISIBLE);
        singleValue.setText(String.valueOf(value));
        
        xAxisLabel.setVisibility(View.GONE);
        xAxis.setVisibility(View.GONE);
        
        yAxisLabel.setVisibility(View.GONE);
        yAxis.setVisibility(View.GONE);
        
        zAxisLabel.setVisibility(View.GONE);
        zAxis.setVisibility(View.GONE);
    }

}
