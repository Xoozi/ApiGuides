package com.xoozi.apiguides.graphics;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import com.xoozi.apiguides.utils.Utils;

public class ActivityBitmapDecode extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SampleView(this));
    }

    public class SampleView extends View {

        private Bitmap  _bm1;
        private Bitmap  _bm2;
        private Bitmap  _bm3;
        private Bitmap  _bm4;
        private Paint   _paint;
        private Movie   _movie;
        private Drawable    _btn;
        private long    _movieStart;

        public SampleView(Context context) {
            super(context);

            setFocusable(true);
            InputStream is;

            is =  getResources().openRawResource(R.drawable.beach);
            BitmapFactory.Options opt = new BitmapFactory.Options(); 
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, opt);

            opt.inJustDecodeBounds = false;
            opt.inSampleSize = 4;
            _bm1 = BitmapFactory.decodeStream(is, null, opt);

            is = getResources().openRawResource(R.drawable.frog);
            _bm2 = BitmapFactory.decodeStream(is);
            int height = _bm2.getHeight();
            int width = _bm2.getWidth();
            int[] pixels = new int[width*height];
            _bm2.getPixels(pixels, 0, width, 0, 0, width, height);
            _bm3 = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_4444);
            _bm4 = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);

            _btn = getResources().getDrawable(android.R.drawable.btn_default);
            _btn.setBounds(150, 50, 300, 130);

            is = getResources().openRawResource(R.drawable.animated_gif);
            _movie = Movie.decodeStream(is);
            _movieStart = 0;

            if(null == _movie)
                Utils.amLog("_movie load failed");
            else{
                Utils.amLog("_movie load suc, dur="+_movie.duration()+
                        ", width="+_movie.width()+", height="+_movie.height());

            }


            _paint = new Paint();
            _paint.setColor(0xeeff0000);
            _paint.setStyle(Paint.Style.STROKE);
        }


        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawColor(Color.WHITE);
            ;
            canvas.drawBitmap(_bm1, 10, 10, null);
            canvas.drawBitmap(_bm2, 10, 170, null);
            canvas.drawBitmap(_bm3, 110, 170, null);
            canvas.drawBitmap(_bm4, 210, 170, null);
            _btn.draw(canvas);

            long now = SystemClock.uptimeMillis();
            if(0 == _movieStart){
                _movieStart = now;
            }
            if(null != _movie){
                int dur = _movie.duration();
                if(dur <= 0)
                    dur = 500;
                int relTime = (int)((now - _movieStart)%dur);

                _movie.setTime(relTime);

                float sx = getWidth() - _movie.width();
                float sy = getHeight() - _movie.height();
                canvas.drawRect(sx, sy, sx + _movie.width(), sy + _movie.height(), _paint);
                _movie.draw(canvas, getWidth() - _movie.width(), getHeight() - _movie.height());
                invalidate();
            }

        }

    }
}
