package com.xoozi.apiguides.app;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;

public class ActivityActionBarDropdownNavigation extends ActivityBase
    implements OnNavigationListener{

    private ActionBar               _actionBar;
    private SpinnerAdapter          _spinnerAdapter;
    private String[]                _strings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar_dropdown);
        _initWork();    
    }

    @Override
    public boolean onNavigationItemSelected(int pos, long itemId) {

        ListContentFragment newFragment = new ListContentFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_content, newFragment, _strings[pos]);

        ft.commit();
        return true;
    }

    private void _initWork(){

        _actionBar = getActionBar();
        _strings = getResources().getStringArray(R.array.action_list);
        _spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.action_list, 
                android.R.layout.simple_spinner_dropdown_item);

        _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        _actionBar.setListNavigationCallbacks(_spinnerAdapter, this);
    }



    private class ListContentFragment extends Fragment{

        private String  _text;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            _text = getTag();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView tv = new TextView(getActivity());
            tv.setText(_text);

            return tv;
        }

    }
}
