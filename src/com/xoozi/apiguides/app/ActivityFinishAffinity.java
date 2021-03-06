package com.xoozi.apiguides.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityFinishAffinity extends ActivityBase{

    int mNesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish_affinity);

        mNesting = getIntent().getIntExtra("nesting", 1);
        ((TextView)findViewById(R.id.seq)).setText("Current nesting: " + mNesting);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.nest);
        button.setOnClickListener(mNestListener);
        button = (Button)findViewById(R.id.finish);
        button.setOnClickListener(mFinishListener);
    }

    private OnClickListener mNestListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(ActivityFinishAffinity.this, ActivityFinishAffinity.class);
            intent.putExtra("nesting", mNesting+1);
            startActivity(intent);
        }
    };

    private OnClickListener mFinishListener = new OnClickListener() {
        public void onClick(View v) {
            finishAffinity();
        }
    };
}
