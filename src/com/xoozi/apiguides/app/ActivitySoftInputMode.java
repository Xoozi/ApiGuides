package com.xoozi.apiguides.app;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivitySoftInputMode extends ActivityBase{

    private Spinner _resizeModes;

    private final String [] _labels = new String[] {
            "Unspecified", "Resize", "Pan", "Nothing"
    };

    private final int[] _modeValues = new int[] {
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED,
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE,
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_soft_input_mode);

        _resizeModes = (Spinner) findViewById(R.id.resize_mode);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, _labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _resizeModes.setAdapter(adapter);
        _resizeModes.setSelection(0);
        _resizeModes.setOnItemSelectedListener(
            new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                int position, long arg3) {

                            getWindow().setSoftInputMode(_modeValues[position]);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                            getWindow().setSoftInputMode(_modeValues[0]);
                        }
        });
    }

}
