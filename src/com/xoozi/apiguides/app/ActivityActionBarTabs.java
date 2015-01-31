package com.xoozi.apiguides.app;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityActionBarTabs extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action_bar_tabs);

    }

    public void onAddTab(View v) {
        final ActionBar bar = getActionBar();
        final int tabCount = bar.getTabCount();
        final String text = "Tab " + tabCount;
        bar.addTab(bar.newTab()
                .setText(text)
                .setTabListener(new TabListener(new TabContentFragment(text))));
    }

    public void onRemoveTab(View v) {
        final ActionBar bar = getActionBar();
        if (bar.getTabCount() > 0) {
            bar.removeTabAt(bar.getTabCount() - 1);
        }
    }

    public void onToggleTabs(View v) {
        final ActionBar bar = getActionBar();

        if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        } else {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
        }
    }

    public void onRemoveAllTabs(View v) {
        getActionBar().removeAllTabs();
    }

    private class TabListener implements ActionBar.TabListener{
        private TabContentFragment _tabFragment;

        public TabListener(TabContentFragment fragment){
            _tabFragment = fragment;
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(ActivityActionBarTabs.this, "Reselected tab:" + tab.getTag()
                    , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {

            ft.add(R.id.fragment_content, _tabFragment, _tabFragment.getText());
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(_tabFragment);
        }
    }

    private class TabContentFragment extends Fragment{
        private String _text;

        public TabContentFragment(String text){
            _text = text;
        }
        
        public String getText(){
            return _text;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View fragmentView = inflater.inflate(R.layout.filed_action_bar_tab_content, 
                    container, false);

            TextView textView = (TextView)fragmentView.findViewById(R.id.text);
            textView.setText(_text);
            return fragmentView;
        }

    }
}
