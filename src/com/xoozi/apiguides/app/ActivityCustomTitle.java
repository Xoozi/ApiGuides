package com.xoozi.apiguides.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityCustomTitle extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_custom_title);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, 
                R.layout.field_custom_title);
        _initWork();
    }

    private void _initWork(){

        final TextView  leftText = (TextView)findViewById(R.id.left_text);
        final TextView  rightText = (TextView)findViewById(R.id.right_text);
        final EditText  rightEdit = (EditText)findViewById(R.id.right_text_edit);
        final EditText  leftEdit = (EditText)findViewById(R.id.left_text_edit);

        leftEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                //leftText.setText(leftEdit.getText().toString());
                leftText.setText(leftEdit.getText());
                //use IME getText() will take the edit text theme to TextView
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
            }
        });

        rightEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                //rightText.setText(rightEdit.getText().toString());
                rightText.setText(rightEdit.getText());
                //use IME getText() will take the edit text theme to TextView
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
            }

        });
    }
}
