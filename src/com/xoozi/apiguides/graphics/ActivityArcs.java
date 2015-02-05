package com.xoozi.apiguides.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

import com.xoozi.apiguides.ActivityBase;

public class ActivityArcs extends ActivityBase{
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SampleView(this));
    }

    public class SampleView extends View {

        private static final float STEP = 1;

        private Paint       _framePaint;
        private RectF       _bigOval;
        private int         _bigIndex = 0;
        private ArcData[]   _arcs;
        private float       _starAngle = 15;
        private float       _sweepAngle = 0;




        public SampleView(Context context) {
            super(context);

            _arcs = new ArcData[4];

            _framePaint = new Paint();
            _framePaint.setColor(0xEE222222);
            _framePaint.setStyle(Paint.Style.STROKE);
            _framePaint.setAntiAlias(true);

            _bigOval = new RectF(40, 10, 280, 250);
            
            _arcs[0] = new ArcData(10, 270, 70, 330,
                        0xEEAA0000, Paint.Style.FILL, false);
            _arcs[1] = new ArcData(90, 270, 150, 330,
                        0xEE00AA00, Paint.Style.FILL, true);
            _arcs[2] = new ArcData(170, 270, 230, 330,
                        0xEE0000AA, Paint.Style.STROKE, false);
            _arcs[3] = new ArcData(250, 270, 310, 330,
                        0xEEAAAAAA, Paint.Style.STROKE, true);

        }


        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawColor(Color.WHITE);

            drawArc(canvas, _bigOval, _starAngle, _sweepAngle,
                    _arcs[_bigIndex]._useCenter, _arcs[_bigIndex]._paint);


            
            for(int index = 0; index < 4; index++){
                drawArc(canvas, _arcs[index]._rect, _starAngle, _sweepAngle,
                        _arcs[index]._useCenter, _arcs[index]._paint);
            }

            _sweepAngle += STEP;
            if(_sweepAngle == 360){
                _sweepAngle = 0;
                _bigIndex += 1;
                _bigIndex %= 4;
            }

            invalidate();
        }

        private void drawArc(Canvas canvas, RectF oval, 
                    float startAngle, float sweepAngle,
                    boolean useCenter, Paint paint){
            canvas.drawRect(oval, _framePaint);
            canvas.drawArc(oval, startAngle, sweepAngle, useCenter,
                    paint);
        }

    }

    private class ArcData{
        RectF   _rect;
        Paint   _paint;
        boolean _useCenter;

        ArcData(float left, float top, float right, float bottom,
                int color, Paint.Style style, boolean useCenter){
            _paint = new Paint();
            _paint.setColor(color);
            _paint.setStyle(style);
            _paint.setAntiAlias(true);

            _useCenter = useCenter;

            _rect = new RectF(left, top, right, bottom);
        }
    }
}
