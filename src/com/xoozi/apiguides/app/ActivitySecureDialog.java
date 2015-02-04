package com.xoozi.apiguides.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivitySecureDialog extends ActivityBase
    implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_secure_dialog);
        findViewById(R.id.show).setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {

        AlertDialog dlg = new AlertDialog.Builder(this)
            .setPositiveButton(android.R.string.ok, null)
            .setMessage(R.string.secure_dialog_activity_text)
            .create();

        dlg.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        dlg.show();
    }

}
