package com.xoozi.apiguides.app;

import com.xoozi.apiguides.R;

import android.app.Activity;
import android.os.Bundle;

public class ActivityForwardingTarget extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forwarding_target);
    }

}
