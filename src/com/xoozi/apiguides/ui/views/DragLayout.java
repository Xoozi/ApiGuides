package com.xoozi.apiguides.ui.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class DragLayout extends LinearLayout {

    private int _previousX = 0;
    private int _previousY = 0;

    private int[] _curLayout = new int[4];

    public DragLayout(Context context, int layoutId) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutId, this, true);
    }

    public int[] getCurrentLayout(){
        return _curLayout;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int curX = (int)event.getX(); 
        final int curY = (int)event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                    _previousX = curX;
                    _previousY = curY;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = curX - _previousX;
                int deltaY = curY - _previousY;
                final int left = getLeft();
                final int top = getTop();

                _curLayout[0] = left + deltaX;
                _curLayout[1] = top + deltaY;
                _curLayout[2] = left + deltaX + getWidth();
                _curLayout[3] = top + deltaY + getHeight();

                if(deltaX != 0 || deltaY != 0){
                    layout(_curLayout[0],
                            _curLayout[1],
                            _curLayout[2],
                            _curLayout[3]);
                }

                _previousX = curX - deltaX;
                _previousY = curY - deltaY;
                break;

            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

}
