package com.xoozi.apiguides.graphics;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityAlphaBitmap extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SampleView(this));
    }

    public class SampleView extends View {
        private Bitmap  _bm1;
        private Bitmap  _bm2;
        private Bitmap  _bm3;
        private Shader  _shader;

        void drawIntoBitmap(Bitmap bm){
            float width = bm.getWidth();
            float height = bm.getHeight();


            Canvas c = new Canvas(bm);
            Paint  p = new Paint();
            p.setAlpha(0x80);
            p.setAntiAlias(true);

            c.drawCircle(width/2, height/2, width/2, p);
            p.setAlpha(0x40);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            p.setTextSize(60);
            p.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fm = p.getFontMetrics();
            c.drawText("Alpha", width/2, (height - fm.ascent)/2, p);
        }

        public SampleView(Context context) {
            super(context);

            setFocusable(true);

            InputStream is = getResources().openRawResource(R.drawable.app_sample_code);
            _bm1 = BitmapFactory.decodeStream(is);
            _bm2 = _bm1.extractAlpha();
            _bm3 = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
            drawIntoBitmap(_bm3);

            _shader = new LinearGradient(0, 0, 100, 70, new int[] {
                                         Color.RED, Color.YELLOW, Color.GREEN, 
                                        Color.CYAN, Color.BLUE, Color.MAGENTA},
                                         null, Shader.TileMode.MIRROR);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawColor(Color.WHITE);
            Paint p = new Paint();
            float y = 10;

            p.setColor(Color.RED);
            canvas.drawBitmap(_bm1, 10, y, p);
            y += _bm1.getHeight();

            canvas.drawBitmap(_bm2, 10, y, p);
            y += _bm2.getHeight();

            p.setShader(_shader);
            canvas.drawBitmap(_bm3, 10, y, p);
        }

    }
}
