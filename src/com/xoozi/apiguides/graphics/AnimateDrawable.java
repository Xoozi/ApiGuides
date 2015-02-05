package com.xoozi.apiguides.graphics;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

public class AnimateDrawable extends ProxyDrawable {
    private Animation       _animation;
    private Transformation  _transformation = new Transformation();

    public AnimateDrawable(Drawable target) {
        super(target);
    }

    public AnimateDrawable(Drawable target, Animation animation){
        super(target);
        _animation = animation;
    }

    public Animation getAnimation(){
        return _animation;
    }

    public void     setAnimation(Animation animation){
        _animation = animation;
    }

    public boolean  hasStarted(){
        return null != _animation && _animation.hasStarted();
    }

    public boolean  hasEnded(){
        return null == _animation || _animation.hasEnded();
    }

    @Override
    public void draw(Canvas c) {

        Drawable draw = getProxy();

        if(null != draw){
            int sc = c.save(); 

            Animation anim = _animation;
            if(null != anim){
                anim.getTransformation(
                        AnimationUtils.currentAnimationTimeMillis(),
                        _transformation);
                c.concat(_transformation.getMatrix());
            }
            draw.draw(c);
            c.restoreToCount(sc);
        }
    }

}
