package com.xoozi.apiguides.app;

import com.xoozi.apiguides.ActivityBase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class ActivityActionBarMechanics extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Normal item");

        MenuItem actionItem = menu.add("Action Button");

        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        actionItem.setIcon(android.R.drawable.ic_menu_share);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this, "Select item id:" + 
                item.getItemId() + ", title:" + item.getTitle(), 
                Toast.LENGTH_SHORT).show();
        return true;
    }


}
