package com.xoozi.apiguides;

import com.xoozi.apiguides.dom.DOMItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityBase extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String name = intent.getStringExtra(DOMItem.KEY_NAME);
        if(null != name)
            setTitle(name);
    }

}
