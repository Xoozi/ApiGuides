package com.xoozi.apiguides.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityAnimateDrawables extends ActivityBase{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SampleView(this));
    }

    public class SampleView extends View {
        private AnimateDrawable _animateDrawable;

        public SampleView(Context context) {
            super(context);

            setFocusable(true);
            setFocusableInTouchMode(true);

            Drawable dr = context.getResources().getDrawable(R.drawable.beach);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

            Animation anim = new TranslateAnimation(0, 100, 0, 150);

            anim.setDuration(2000);
            anim.setRepeatCount(-1);
            anim.initialize(10, 10, 10, 10);

            _animateDrawable = new AnimateDrawable(dr, anim);
            anim.startNow();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            _animateDrawable.draw(canvas);
            invalidate();
        }

    }
}
