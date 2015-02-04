package com.xoozi.apiguides.app;

import android.os.Bundle;
import android.view.WindowManager;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityTranslucentBlur extends ActivityBase{

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, 
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_translucent);
    }
}
