package com.xoozi.apiguides;

import java.io.File;
import java.util.List;

import com.xoozi.apiguides.dom.AssetConfig;
import com.xoozi.apiguides.dom.DOMItem;
import com.xoozi.apiguides.dom.DOMList;
import com.xoozi.apiguides.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityMain extends Activity implements OnItemClickListener
{
    private DOMList         _domList;
    private List<DOMItem>   _itemList;
    private ListView        _listView;
    private ListAdapter     _listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _initWork();
    }

    @Override
    public void onItemClick(AdapterView<?> av, View view, int pos, long id) {

        DOMItem item = _itemList.get(pos);
        item.action(this);
    }

    private void _initWork(){
        Intent intent = getIntent();

        String content = intent.getStringExtra(DOMItem.KEY_CONTENT);
        String type = intent.getStringExtra(DOMItem.KEY_TYPE);
        String path = intent.getStringExtra(DOMItem.KEY_PATH);
        try {
            if(null == type){

                _domList = AssetConfig.loadRoot(this).getIndexList();
            }else if(type.equals(DOMItem.VALUE_TYPE_LIST)){

                _domList = AssetConfig.loadList(this, path, content);
            }else if(type.equals(DOMItem.VALUE_TYPE_DIR)){

                _domList = AssetConfig.loadDir(this, 
                        path + File.separator + content
                        ).getIndexList();
            }
            _itemList = _domList.getList();
            _listView = (ListView) findViewById(R.id.list_items);
            if(null == _listView){
                Utils.amLog("wtf");
            }
            setTitle(_domList.getName());

            _listAdapter = new ListAdapter(this);
            _listView.setAdapter(_listAdapter);
            _listView.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }


    private class ListAdapter extends BaseAdapter {

        private LayoutInflater      _layoutInflater;

        ListAdapter(Context context){
            
            _layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if(null == _itemList){
                return 0;
            }else{
                return _itemList.size();
            }
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            if(null==convertView){
                convertView = _layoutInflater.inflate(R.layout.list_item, null);
            }
            
            TextView txtName = (TextView) convertView.findViewById(R.id.txt_name);

            txtName.setText(_itemList.get(position).getName());
            
            return convertView;
        }
    }


}
