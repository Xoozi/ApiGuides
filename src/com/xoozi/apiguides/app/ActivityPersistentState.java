package com.xoozi.apiguides.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityPersistentState extends ActivityBase{

    private EditText    _editSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persistent_state);

       ((TextView) findViewById(R.id.msg)).setText(
                getResources().getString(R.string.persistent_msg));
       _editSave = (EditText)findViewById(R.id.saved);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
        
        String text = sp.getString(Integer.toString(_editSave.getId()), null);
        if(null != text){
            _editSave.setText(text);
            int start = sp.getInt(
                    Integer.toString(_editSave.getId())+"_start", -1);
            int end = sp.getInt(
                    Integer.toString(_editSave.getId())+"_end", -1);

            if(start >= 0 && end >=0){
                _editSave.setSelection(start, end);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor spe = 
            getPreferences(Activity.MODE_PRIVATE).edit();
        spe.putString(Integer.toString(_editSave.getId()), 
                        _editSave.getText().toString());
        spe.putInt(Integer.toString(_editSave.getId())+"_start", 
                        _editSave.getSelectionStart());
        spe.putInt(Integer.toString(_editSave.getId())+"_end", 
                        _editSave.getSelectionEnd());
        spe.commit();

    }

}
