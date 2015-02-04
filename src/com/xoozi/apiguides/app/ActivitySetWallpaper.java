package com.xoozi.apiguides.app;

import java.io.IOException;

import android.app.WallpaperManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivitySetWallpaper extends ActivityBase
 implements OnClickListener {

    private static final int[] _colors =
            {Color.BLUE, Color.GREEN, Color.RED, Color.LTGRAY, Color.MAGENTA, Color.CYAN,
                    Color.YELLOW, Color.WHITE};

    private WallpaperManager    _wpManager;
    private ImageView           _imgWP;
    private Drawable            _dabWP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_wallpaper);
        findViewById(R.id.randomize).setOnClickListener(this);
        findViewById(R.id.setwallpaper).setOnClickListener(this);

        _imgWP = (ImageView)findViewById(R.id.imageview);
        _imgWP.setDrawingCacheEnabled(true);

        _wpManager = WallpaperManager.getInstance(this);
        _dabWP = _wpManager.getDrawable();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.randomize:
            int color = (int) Math.floor(Math.random() * _colors.length);
                _dabWP.setColorFilter(_colors[color], PorterDuff.Mode.MULTIPLY);
                _imgWP.setImageDrawable(_dabWP);
                _imgWP.invalidate();
                break;

            case R.id.setwallpaper:

                try {
                    _wpManager.setBitmap(_imgWP.getDrawingCache());
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
