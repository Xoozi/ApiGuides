package com.xoozi.apiguides.app;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivitySaveRestoreState extends ActivityBase{

    //private static final String KEY_SAVE = "key_save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_restore_state);

        ((TextView)findViewById(R.id.msg)).setText(R.string.save_restore_msg);
    }

    CharSequence getSavedText() {
        return ((EditText)findViewById(R.id.saved)).getText();
    }

    void setSavedText(CharSequence text) {
        ((EditText)findViewById(R.id.saved)).setText(text);
    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_SAVE, getSavedText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        setSavedText(savedInstanceState.getString(KEY_SAVE));
    }*/
}
