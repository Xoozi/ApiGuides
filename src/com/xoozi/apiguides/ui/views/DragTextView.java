package com.xoozi.apiguides.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class DragTextView extends TextView {

    private int _previousX = 0;
    private int _previousY = 0;

    private int[] _curLayout = new int[4];

    public DragTextView(Context context) {
        super(context);
    }
    public DragTextView(Context context, AttributeSet attribute){
        this(context, attribute, 0);
    }
    public DragTextView(Context context, AttributeSet attribute, int style){
        super(context, attribute, style);
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
