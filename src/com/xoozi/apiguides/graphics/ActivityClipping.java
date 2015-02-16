package com.xoozi.apiguides.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Bundle;
import android.view.View;

import com.xoozi.apiguides.ActivityBase;

public class ActivityClipping extends ActivityBase{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SampleView(this));
    }

    public class SampleView extends View {
        private Paint       _paint;
        private Path        _path;

        public SampleView(Context context) {
            super(context);

            _paint = new Paint();
            _paint.setAntiAlias(true);
            _paint.setStrokeWidth(6);
            _paint.setTextSize(16);
            _paint.setTextAlign(Paint.Align.RIGHT);

            _path = new Path();
        }

        @Override
        public void draw(Canvas canvas) {

            canvas.drawColor(Color.GRAY);

            canvas.save();
            canvas.translate(10, 10);
            _drawSence(canvas);
            canvas.restore();


            canvas.save();
            canvas.translate(160, 10);
            canvas.clipRect(10, 10, 90, 90);
            canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE);
            _drawSence(canvas);
            canvas.restore();

            canvas.save();
            canvas.translate(10, 160);
            _path.reset();
            canvas.clipPath(_path);
            _path.addCircle(50, 50, 50, Path.Direction.CCW);
            canvas.clipPath(_path, Region.Op.REPLACE);
            _drawSence(canvas);
            canvas.restore();


            canvas.save();
            canvas.translate(160, 160);
            canvas.clipRect(0, 0, 60, 60);
            canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
            _drawSence(canvas);
            canvas.restore();

            canvas.save();
            canvas.translate(10, 310);
            canvas.clipRect(0, 0, 60, 60);
            canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
            _drawSence(canvas);
            canvas.restore();

            canvas.save();
            canvas.translate(160, 310);
            canvas.clipRect(0, 0, 60, 60);
            canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE);
            _drawSence(canvas);
            canvas.restore();
        }


        private void _drawSence(Canvas canvas){
            canvas.clipRect(0, 0, 100, 100);
            canvas.drawColor(Color.WHITE);

            _paint.setColor(Color.RED);
            canvas.drawLine(0, 0, 100, 100, _paint);

            _paint.setColor(Color.GREEN);
            canvas.drawCircle(30, 70, 30, _paint);

            _paint.setColor(Color.BLUE);
            canvas.drawText("Clipping", 100, 30, _paint);
        }
    }
}
