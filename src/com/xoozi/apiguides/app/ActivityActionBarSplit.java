package com.xoozi.apiguides.app;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Use the Action Bar Usage's UI to demonstrate
 * the split Action Bar
 **/
public class ActivityActionBarSplit extends ActivityBase
    implements SearchView.OnQueryTextListener, ActionBar.TabListener{

    ActionBar   _actionBar;
    TextView    _searchTextView;
    int         _sortMode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _searchTextView = new  TextView(this);
        _actionBar = getActionBar();

        setContentView(_searchTextView);

        _actionBar.addTab(_actionBar.newTab().setText("Tab 0").setTabListener(this));
        _actionBar.addTab(_actionBar.newTab().setText("Tab 1").setTabListener(this));
        _actionBar.addTab(_actionBar.newTab().setText("Tab 2").setTabListener(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_split, menu);

        SearchView sv = (SearchView) menu.
            findItem(R.id.action_search).getActionView();
        sv.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(-1 != _sortMode){

            Drawable icon = menu.findItem(_sortMode).getIcon();
            menu.findItem(R.id.action_sort).setIcon(icon);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(R.id.action_change == item.getItemId()){
            if (_actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
                _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                _actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
            } else {
                _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                _actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
            }
        }

        Toast.makeText(this, "Selected Action:"+
                item.getTitle(), Toast.LENGTH_SHORT).show();

        return true;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        
        _searchTextView.setText(query);
        
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "Submit search:"+
                query, Toast.LENGTH_LONG).show();
        return true;
    }

    public void onSort(MenuItem item){
        _sortMode = item.getItemId();

        invalidateOptionsMenu();
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

    }

    @Override
    public void onTabSelected(Tab arg0, FragmentTransaction arg1) {

    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

    }

}
