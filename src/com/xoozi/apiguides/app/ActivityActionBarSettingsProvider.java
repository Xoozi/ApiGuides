package com.xoozi.apiguides.app;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActivityActionBarSettingsProvider extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().
            inflate(R.menu.action_bar_settings_action_provider, menu); 
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this, "Item selected:"+item.getTitle(),
                Toast.LENGTH_SHORT).show();

        return false;
    }

    public static class SettingsProvider extends ActionProvider {

        private static final Intent _settingsIntent = 
            new Intent(Settings.ACTION_SETTINGS);

        private Context _context;

        public SettingsProvider(Context context) {
            super(context);
            _context = context;
        }

        @Override
        public View onCreateActionView() {

            LayoutInflater inflater = LayoutInflater.from(_context);
            View view  = inflater.inflate(
                    R.layout.action_bar_settings_action_provider, null);
        
            view.findViewById(R.id.button).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            _context.startActivity(_settingsIntent);
                        }

                    });
            return view;
        }

        @Override
        public boolean onPerformDefaultAction() {
            _context.startActivity(_settingsIntent);
            return true;
        }

    }
}
