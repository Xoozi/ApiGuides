package com.xoozi.apiguides.sensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

public class AccelerationView extends View {
    private Bitmap  _bitmap;
    private Paint   _paint = new Paint();
    private Canvas  _canvas = new Canvas();
    private Path    _path = new Path();
    private RectF   _rect = new RectF();
    private float   _lastValues[] = new float[3];
    private int     _colors[] = new int[3];
    private float   _lastX;
    private float   _scale;
    private float   _offsetY;
    private float   _maxX;
    private float   _speed = 1.0f;
    private float   _width;
    private float   _height;
    private float   _threshold;

    public AccelerationView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        _initWork();
    }   

    public AccelerationView(Context context) {
        super(context);
        _initWork();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        _bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        _canvas.setBitmap(_bitmap);
        _canvas.drawColor(0xFFFFFFFF);
        _offsetY = h * 0.5f;
        _scale = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        _width = w;
        _height = h;
        if (_width < _height) {
            _maxX = w;
        } else {
            _maxX = w-50;
        }
        _lastX = _maxX;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (_bitmap != null) {
            final Paint paint = _paint;

            if (_lastX >= _maxX) {
                _lastX = 0;
                final Canvas cavas = _canvas;
                final float yoffset = _offsetY;
                final float maxx = _maxX;
                final float oneG = SensorManager.STANDARD_GRAVITY * _scale;
                final float thre = _threshold * _scale;
                paint.setColor(0xFFAAAAAA);
                cavas.drawColor(0xFFFFFFFF);
                cavas.drawLine(0, yoffset,      maxx, yoffset,      paint);
                cavas.drawLine(0, yoffset+thre, maxx, yoffset+thre, paint);
                cavas.drawLine(0, yoffset-thre, maxx, yoffset-thre, paint);
                cavas.drawLine(0, yoffset+oneG, maxx, yoffset+oneG, paint);
                cavas.drawLine(0, yoffset-oneG, maxx, yoffset-oneG, paint);
            }
            canvas.drawBitmap(_bitmap, 0, 0, null);
        }
    }

    public void setThreshold(float threshold){
        _threshold = threshold;
    }

    public void onSensorChanged(SensorEvent event) {
        if (_bitmap != null) {
            final Canvas canvas = _canvas;
            final Paint paint = _paint;
            float deltaX = _speed;
            float newX = _lastX + deltaX;

            for (int i=0 ; i<3 ; i++) {
                final float v = _offsetY + event.values[i] * _scale;
                paint.setColor(_colors[i]);
                canvas.drawLine(_lastX, _lastValues[i], newX, v, paint);
                _lastValues[i] = v;
            }
            _lastX += _speed;
            invalidate();
        }
    }

    private void _initWork(){
        _colors[0] = Color.argb(192, 255, 0, 0);
        _colors[1] = Color.argb(192, 0, 255, 0);
        _colors[2] = Color.argb(192, 0, 0, 255);

        _paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        _rect.set(-0.5f, -0.5f, 0.5f, 0.5f);
        _path.arcTo(_rect, 0, 180);
    }
}
