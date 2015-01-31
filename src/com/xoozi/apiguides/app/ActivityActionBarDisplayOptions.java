package com.xoozi.apiguides.app;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import com.xoozi.apiguides.utils.Utils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class ActivityActionBarDisplayOptions extends ActivityBase
implements OnClickListener, ActionBar.TabListener{

    private View        _customView;
    private ActionBar   _actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action_bar_display_options);
        _initWork();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_display_options, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int flag = 0;    
        
        switch(view.getId()){
            case R.id.toggle_home_as_up:
                flag = ActionBar.DISPLAY_HOME_AS_UP;
                break;

            case R.id.toggle_show_custom:
                flag = ActionBar.DISPLAY_SHOW_CUSTOM;
                break;

            case R.id.toggle_show_home:
                flag = ActionBar.DISPLAY_SHOW_HOME;
                break;

            case R.id.toggle_show_title:
                flag = ActionBar.DISPLAY_SHOW_TITLE;
                break;

            case R.id.toggle_use_logo:
                flag = ActionBar.DISPLAY_USE_LOGO;
                break;

            case R.id.toggle_navigation:
                _actionBar.setNavigationMode(
                _actionBar.getNavigationMode() == 
                ActionBar.NAVIGATION_MODE_STANDARD ? 
                ActionBar.NAVIGATION_MODE_TABS : 
                ActionBar.NAVIGATION_MODE_STANDARD);
                return;

            case R.id.cycle_custom_gravity:
                ActionBar.LayoutParams lp = 
                    (ActionBar.LayoutParams) _customView.getLayoutParams();
                int newGravity = 0;
                int curGravity = lp.gravity & 
                    Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
                Utils.amLog(String.format("curGravity:%#x", curGravity));
                switch(curGravity){
                    case  Gravity.START:
                        newGravity = Gravity.CENTER_HORIZONTAL;
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        newGravity = Gravity.END;
                        break;
                    case Gravity.END:
                        newGravity = Gravity.START;
                        break;
                }
                lp.gravity = lp.gravity &
                    ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | newGravity;
                _actionBar.setCustomView(_customView, lp);
                return;
        }

        int change = _actionBar.getDisplayOptions() ^ flag;
        _actionBar.setDisplayOptions(change, flag);
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

    private void _initWork(){
        _customView = getLayoutInflater().
            inflate(R.layout.action_bar_display_option_custom, null);

        findViewById(R.id.toggle_home_as_up).setOnClickListener(this);
        findViewById(R.id.toggle_navigation).setOnClickListener(this);
        findViewById(R.id.toggle_show_custom).setOnClickListener(this);
        findViewById(R.id.toggle_show_home).setOnClickListener(this);
        findViewById(R.id.toggle_show_title).setOnClickListener(this);
        findViewById(R.id.toggle_use_logo).setOnClickListener(this);
        findViewById(R.id.cycle_custom_gravity).setOnClickListener(this);

        _actionBar = getActionBar();
        _actionBar.setCustomView(_customView, 
                new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));

        _actionBar.addTab(_actionBar.newTab().setText("Tab 0").setTabListener(this));
        _actionBar.addTab(_actionBar.newTab().setText("Tab 1").setTabListener(this));
        _actionBar.addTab(_actionBar.newTab().setText("Tab 2").setTabListener(this));

    }
}
