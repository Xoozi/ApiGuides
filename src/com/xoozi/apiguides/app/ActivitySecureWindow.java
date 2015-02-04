package com.xoozi.apiguides.app;

import android.os.Bundle;
import android.view.WindowManager;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivitySecureWindow extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_secure_window);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

}
