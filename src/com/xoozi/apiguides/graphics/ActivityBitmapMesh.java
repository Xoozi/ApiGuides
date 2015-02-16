package com.xoozi.apiguides.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityBitmapMesh extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SampleView(this));
    }

    
    class SampleView extends View {
        private static final float  K = 10000;
        private static final float  DK = 0.00001f;
        private static final int    HEIGHT = 20;
        private static final int    WIDTH = 20;
        private static final int    COUNT = (WIDTH + 1) * (HEIGHT + 1);
        
        private final float[]   _verts = new float[COUNT * 2];
        private final float[]   _orig = new float[COUNT * 2];

        private final Matrix    _matrix = new Matrix();
        private final Matrix    _invert = new Matrix();

        private final Bitmap    _bm;
        
        private int             _lastRubX = -9999;
        private int             _lastRubY;
        private Paint           _gridPaint;

        public SampleView(Context context) {
            super(context);

            _bm = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.beach);
            float w = _bm.getWidth();
            float h = _bm.getHeight();
            int index = 0;
            for(int y = 0; y <= HEIGHT; y++){

                float fy = y * h / HEIGHT;
                for(int x = 0; x <= WIDTH; x++){

                    float fx = x * w / WIDTH;
                    _setXY(_verts, index, fx, fy);
                    _setXY(_orig, index, fx, fy);
                    index+= 1;
                }
            }

            _matrix.setTranslate(10, 10);
            _matrix.invert(_invert);

            _gridPaint = new Paint();
            _gridPaint.setAntiAlias(true);
            _gridPaint.setColor(0xEE20FF20);
            _gridPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);

            canvas.concat(_matrix);
            canvas.drawBitmapMesh(_bm, WIDTH, HEIGHT, _verts, 0,
                                    null, 0, null);

            _drawGrid(canvas);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            
            float [] pt = {event.getX(), event.getY()};
            _invert.mapPoints(pt);

            int x = (int)pt[0];
            int y = (int)pt[1];

            if(x != _lastRubX || y != _lastRubY){
                _lastRubX = x;
                _lastRubY = y;
                _rub(pt[0], pt[1]);
                invalidate();
            }
            return true;
        }


        private void _setXY(float[] array, int index, float x, float y){
            array[index * 2 + 0] = x;
            array[index * 2 + 1] = y;
        }

        private void _drawGrid(Canvas canvas){

            int px, py;
            int index1, index2;

            int w = WIDTH + 1;
            for(py = 0; py < HEIGHT; py++){
                for(px = 0; px < WIDTH; px++){

                    index1 = py * w + px;
                    index2 = index1 + 1;
                    _line(canvas, index1, index2);

                    index2 = index1 + w;
                    _line(canvas, index1, index2);
                }
            }
        }

        private void _line(Canvas canvas, int index1, int index2){

            float x1, y1, x2, y2;
            x1 = _verts[index1*2+0];
            y1 = _verts[index1*2+1];
            x2 = _verts[index2*2+0];
            y2 = _verts[index2*2+1];

            canvas.drawLine(x1, y1, x2, y2, _gridPaint);
        }


        private void _rub(float tx, float ty){
                       
            float x, y;
            float dx, dy, dsq, d;
            float pull;
            
            for(int i = 0; i < COUNT * 2; i +=2){
                x = _orig[i+0];
                y = _orig[i+1];
                dx = tx - x;
                dy = ty - y;
                dsq = dx * dx + dy * dy;
                d = FloatMath.sqrt(dsq);
                pull = K / (dsq + DK);
                pull /= (d + DK);

                //Utils.amLog(String.format("tx:%f, ty:%f,index:%d, dsq:%f, d:%f, pull:%f",
                 //           tx, ty, i, dsq, d, pull));

                if(pull >= 1){
                    _verts[i+0] = tx;
                    _verts[i+1] = ty;
                }else{
                    _verts[i+0] = x + dx * pull;
                    _verts[i+1] = y + dy * pull;
                }
            }
        }


    }
}
