package com.xoozi.apiguides.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import static android.util.FloatMath.sin;
import static android.util.FloatMath.cos;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.utils.Utils;

public class ActivityAwesome extends ActivityBase
    implements OnGestureListener{

    private GestureDetector _gestureDetector;
    private AwesomeView     _awesomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _awesomeView = new AwesomeView(this);

        setContentView(_awesomeView);

        _gestureDetector = new GestureDetector(this, this);
    }

    public class AwesomeView extends View {

        private float _angle = 0;
        private float _speed = 0.05f;
        private float _radius =30.0f;
        private float _sx = 2.0f;
        private float _sy = 2.0f;
        private int   _fillColor = Color.argb(4, 0, 0, 0);
        private int   _pointColor = Color.WHITE;
        private Paint _fillPaint;
        private Paint _paint;
        private Bitmap _bm;
        private float _size = 320;

        public AwesomeView(Context context) {
            super(context);
            _paint = new Paint();
            _paint.setColor(_pointColor);
            _paint.setAntiAlias(true);

            _fillPaint = new Paint();
            _fillPaint.setColor(Color.BLACK);

            _bm = Bitmap.createBitmap((int)_size, (int)_size, Bitmap.Config.ARGB_8888);
        }

        void updateSX(float flag){
            if(flag > 0){
                _sx += 0.1f;
            }else if(flag < 0){
                _sx -= 0.1f;
            }

            Utils.amLog("_sx:"+_sx);
        }

        void updateSY(float flag){
            if(flag > 0){
                _sy += 0.1f;
            }else if(flag < 0){
                _sy -= 0.1f;
            }
            Utils.amLog("_sy:"+_sy);
        }

        void drawIntoBitmap(Bitmap bm){

            Canvas c = new Canvas(bm);
            c.drawColor(_fillColor);

            _radius = _size * 0.2f;

            float sinval = sin(_angle);
            float cosval = cos(_angle);
            float x = (_size - _radius)/2 + cosval * _radius;
            float y = (_size - _radius)/2 + sinval * _radius;
            c.drawCircle(x, y, _size / 50, _paint);            

            float x2 = x + cos(_angle * _sx) * _radius / 2;
            float y2 = y + sin(_angle * _sy) * _radius / 2;
            c.drawCircle(x2, y2, _size / 30, _paint);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            drawIntoBitmap(_bm);

            canvas.drawBitmap(_bm, 0, 0, null);

            _angle += _speed;

            invalidate();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return _gestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float velocityX,
            float velocityY){
        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);
        if(absX > absY){
            _awesomeView.updateSX(velocityX); 
            return true;
        }else{
            _awesomeView.updateSY(velocityY);
            return true;
        }
    }

    @Override
    public void onLongPress(MotionEvent arg0) {

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
            float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }
}
