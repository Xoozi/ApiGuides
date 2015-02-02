package com.xoozi.apiguides.ui;

import android.os.Bundle;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import com.xoozi.apiguides.ui.views.DragTextView;

public class ActivityDragTextView extends ActivityBase{

    DragTextView    _dragText;
    int[]           _layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_textview);

        _dragText = (DragTextView) findViewById(R.id.drag_text);
    }

    @Override
    protected void onPause() {
        super.onPause();

        _layout = _dragText.getCurrentLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(null != _layout){
            _dragText.layout(_layout[0], _layout[1], _layout[2], _layout[3]);
        }
    }

}
