package com.xoozi.apiguides.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class ProxyDrawable extends Drawable {

    private Drawable    _proxy;
    private boolean     _mutated;

    public ProxyDrawable(Drawable target){
        _proxy = target;
        _mutated = false;
    }

    public Drawable getProxy(){
        return _proxy;
    }
    
    public void setProxy(Drawable proxy){
        _proxy = proxy;
    }

    @Override
    public void draw(Canvas c) {
        if(null != _proxy)
            _proxy.draw(c);
    }

    @Override
    public int getIntrinsicHeight() {
        return null != _proxy ? _proxy.getIntrinsicHeight() : -1;
    }

    @Override
    public int getIntrinsicWidth() {
        return null != _proxy ? _proxy.getIntrinsicWidth() : -1;
    }

    @Override
    public int getOpacity() {
        return null != _proxy ? _proxy.getOpacity(): PixelFormat.TRANSPARENT;
    }

    @Override
    public void setAlpha(int alpha) {
        if(null != _proxy)
            _proxy.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        if(null != _proxy)
            _proxy.setColorFilter(filter);
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        if(null != _proxy)
            _proxy.setFilterBitmap(filter);
    }

    @Override
    public void setDither(boolean dither) {
        if(null != _proxy)
            _proxy.setDither(dither);
    }

    @Override
    public Drawable mutate() {
        if(null != _proxy && !_mutated && super.mutate() == this){
            _proxy.mutate();
            _mutated = true;
        }

        return this;
    }

}
