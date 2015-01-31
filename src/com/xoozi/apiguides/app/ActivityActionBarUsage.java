package com.xoozi.apiguides.app;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import com.xoozi.apiguides.dom.DOMItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityActionBarUsage extends ActivityBase
    implements SearchView.OnQueryTextListener{

    TextView    _searchTextView;
    int         _sortMode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _searchTextView = new  TextView(this);

        setContentView(_searchTextView);

        Intent intent = getIntent();
        String name = intent.getStringExtra(DOMItem.KEY_NAME);
        setTitle(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);

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

}
