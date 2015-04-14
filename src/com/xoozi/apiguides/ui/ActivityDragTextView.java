package com.xoozi.apiguides.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import com.xoozi.apiguides.ui.views.DragLayout;
import com.xoozi.apiguides.ui.views.DragTextView;

public class ActivityDragTextView extends ActivityBase{

    DragTextView    _dragText;
    DragLayout      _dragLayout;
    int[]           _layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_textview);

        _dragText = (DragTextView) findViewById(R.id.drag_text);

        ViewGroup screen = (ViewGroup) findViewById(R.id.screen); 
        _dragLayout = new DragLayout(this, R.layout.field_drag_layout);
        screen.addView(_dragLayout);
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
