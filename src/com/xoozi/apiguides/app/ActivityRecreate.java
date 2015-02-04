package com.xoozi.apiguides.app;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityRecreate extends ActivityBase
    implements OnClickListener{
    
    private static final String KEY_THEME = "key_theme";
    private int _curTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(null != savedInstanceState){
            _curTheme = savedInstanceState.getInt(KEY_THEME, 0);
            
            switch(_curTheme){
                case android.R.style.Theme_Holo_Light:
                    _curTheme = 
                        android.R.style.Theme_Holo_Dialog;
                    break;

                case android.R.style.Theme_Holo_Dialog:
                    _curTheme =
                         android.R.style.Theme_Holo_Light_DarkActionBar;
                    break;
                
                default:
                    _curTheme = android.R.style.Theme_Holo_Light;
                    break;
            }
            setTheme(_curTheme);
        }

        setContentView(R.layout.activity_recreate);

        findViewById(R.id.recreate).setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_THEME, _curTheme);
    }

    @Override
    public void onClick(View arg0) {
        recreate();
    }

}
