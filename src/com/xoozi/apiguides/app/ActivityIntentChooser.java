package com.xoozi.apiguides.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xoozi.apiguides.ActivityBase;

public class ActivityIntentChooser extends ActivityBase
    implements OnClickListener{

    private static final int REQUEST_CODE_SELECT = 2015;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        setContentView(tv);
        tv.setText("Click here to gogogo");
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Select music"),
                                REQUEST_CODE_SELECT);  
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_CODE_SELECT == requestCode){
            if(Activity.RESULT_OK == resultCode){
                Uri uri = data.getData();
                Toast.makeText(this, "uri:"+uri.toString(),
                        Toast.LENGTH_SHORT).show();
            }else{
            
                Toast.makeText(this, "user cancel",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
