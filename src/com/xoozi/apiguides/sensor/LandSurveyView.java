package com.xoozi.apiguides.sensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LandSurveyView extends View {
    private static final float STEP = 5;
    private Bitmap _bitmap;
    private Paint _paint = new Paint();
    private Paint _paintArrow = new Paint();
    private Canvas _canvas = new Canvas();
    private Path _path = new Path();
    private RectF _rect = new RectF();
    private float _bearing;
    private float _scale;
    private float _width;
    private float _height;

    private float _x = 0;
    private float _y = 0;

    public LandSurveyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _initWork();
    }

    public LandSurveyView(Context context) {
        super(context);
        _initWork();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        _bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        _canvas.setBitmap(_bitmap);
        _canvas.drawColor(0x5000FF00);
        _scale = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        _width = w;
        _height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (_bitmap != null) {

            canvas.drawBitmap(_bitmap, 0, 0, null);
        }
        _drawArrow(canvas);
    }

    public void onOrientationChanged(SensorEvent event){
        _bearing = event.values[0];
        invalidate();
    }

    public void onStep(){
        if (_bitmap != null) {

            float  bearing = (float)Math.toRadians(_bearing);
            float  x = _x + (float)Math.sin(bearing)*STEP;
            float  y = _y - (float)Math.cos(bearing)*STEP;
            _canvas.save();
            _canvas.translate(_width/2, _height/2);
            _canvas.drawLine(_x, _y, x, y, _paint);
            _canvas.restore();
            _x = x;
            _y = y;
            Log.d("gg", ""+_x+","+_y);
            invalidate();
        }
    }

    private void _drawArrow(Canvas canvas){
        int saveCount = canvas.getSaveCount();
        float centerX = _width - 50;
        float centerY = 50;
        canvas.save();
        //Log.d("gg", "bearing:"+_bearing);
        canvas.rotate(_bearing, centerX, centerY);
	    Path rollArrow = new Path();
	    rollArrow.moveTo(centerX - 8, centerY + 30);
	    rollArrow.lineTo(centerX, centerY - 30);
	    rollArrow.lineTo(centerX + 8, centerY + 30);
	    rollArrow.lineTo(centerX - 8, centerY + 30);
	    canvas.drawPath(rollArrow, _paintArrow);
        canvas.restoreToCount(saveCount);
    }

    private void _initWork(){

        _paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        _paint.setColor(0xEEEEEEEE);
        _paint.setStrokeWidth(2);
        _paintArrow.setAntiAlias(true);
        _paintArrow.setColor(0xEEEEEEEE);
        _paintArrow.setStyle(Paint.Style.FILL);
        _rect.set(-0.5f, -0.5f, 0.5f, 0.5f);
        _path.arcTo(_rect, 0, 180);
    }
}
