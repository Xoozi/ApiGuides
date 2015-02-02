package com.xoozi.apiguides.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityAnimation extends ActivityBase implements
    OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        _initWork();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.fade_animation:
                startActivity(new Intent(this, ActivityAlertDialogs.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                break;

            case R.id.zoom_animation:
                startActivity(new Intent(this, ActivityAlertDialogs.class));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                break;

            case R.id.modern_fade_animation:{
                ActivityOptions opts = ActivityOptions
                    .makeCustomAnimation(this, R.anim.fade, R.anim.hold);
                startActivity(new Intent(this, ActivityAlertDialogs.class),
                        opts.toBundle());
            }
            break;

            case R.id.modern_zoom_animation:{
                ActivityOptions opts = ActivityOptions
                    .makeCustomAnimation(this, R.anim.zoom_enter, 
                            R.anim.zoom_exit);
                startActivity(new Intent(this, ActivityAlertDialogs.class),
                        opts.toBundle());
            }
            break;

            case R.id.scale_up_animation:{
                ActivityOptions opts = ActivityOptions
                    .makeScaleUpAnimation(view, 0, 0, 
                            view.getWidth(), view.getHeight());
                startActivity(new Intent(this, ActivityAlertDialogs.class),
                        opts.toBundle());
            }
            break;

            case R.id.zoom_thumbnail_animation:{
                view.setDrawingCacheEnabled(true);
                view.setPressed(false);
                view.refreshDrawableState();

                Bitmap bm = view.getDrawingCache();
                Canvas cv = new Canvas(bm);
                cv.drawARGB(125, 255, 0, 0);
                ActivityOptions opts = ActivityOptions
                    .makeThumbnailScaleUpAnimation(view, bm, 0, 0);

                startActivity(new Intent(this, ActivityAlertDialogs.class),
                        opts.toBundle());

                view.setDrawingCacheEnabled(false); 
            }
            break;
        }
    }

    private void _initWork(){

        findViewById(R.id.fade_animation).setOnClickListener(this);
        findViewById(R.id.zoom_animation).setOnClickListener(this);

        if(android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN){
            findViewById(R.id.modern_fade_animation)
                .setOnClickListener(this);
            findViewById(R.id.modern_zoom_animation)
                .setOnClickListener(this);
            findViewById(R.id.scale_up_animation)
                .setOnClickListener(this);
            findViewById(R.id.zoom_thumbnail_animation)
                .setOnClickListener(this);
        }else{
            findViewById(R.id.modern_fade_animation)
                .setEnabled(false);
            findViewById(R.id.modern_zoom_animation)
                .setEnabled(false);
            findViewById(R.id.scale_up_animation)
                .setEnabled(false);
            findViewById(R.id.zoom_thumbnail_animation)
                .setEnabled(false);
        }
    }
}
