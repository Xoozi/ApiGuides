package com.xoozi.apiguides.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityReorderThree extends ActivityBase
 implements OnClickListener {

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reorder_three);

        findViewById(R.id.reorder_launch_four).setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent(this, ActivityReorderFour.class);
        startActivity(intent);
    }
}
