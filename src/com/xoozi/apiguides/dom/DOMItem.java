package com.xoozi.apiguides.dom;

import java.io.File;

import org.w3c.dom.Element;

import com.xoozi.apiguides.ActivityMain;
import com.xoozi.apiguides.R;
import com.xoozi.apiguides.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DOMItem{
    public static final String KEY_TYPE = "type";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_PATH = "path";
    public static final String KEY_NAME = "name";

    public static final String NODE_ITEM            = "item";
    public static final String ATTR_NAME            = "name";
    public static final String ATTR_CONTENT         = "content";
    public static final String ATTR_TYPE            = "type";

    public static final String VALUE_TYPE_DIR       = "dir";
    public static final String VALUE_TYPE_LIST      = "list";
    public static final String VALUE_TYPE_ACTIVITY  = "activity";



    private String  _path;
    private String  _name;
    private String  _content;
    private String  _type;

    public DOMItem(Element element, String path){
        _name       = element.getAttribute(ATTR_NAME);
        _content    = element.getAttribute(ATTR_CONTENT);
        _type       = element.getAttribute(ATTR_TYPE);
        _path       = path;
    }

    public String getName(){
        return _name;
    }

    public String getContent(){
        return _content;
    }
    
    public String getType(){
        return _type;
    }

    public void action(Context context){
        if(null != _type){
            if(_type.equals(VALUE_TYPE_LIST)){
                _actionList(context);
            }else if(_type.equals(VALUE_TYPE_ACTIVITY)){
                _actionActivity(context);
            }else if(_type.equals(VALUE_TYPE_DIR)){
                _actionDir(context);
            }else{
                Utils.amLog("action wtf");
            }
        }
    }

    private void _actionActivity(Context context){
        Utils.amLog("action activity:"+_content);
        
        try {
            @SuppressWarnings("rawtypes")
            Class cl = Class.forName(_content);
            Intent  intent   = new Intent(context, cl);
            intent.putExtra(KEY_NAME, _name);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, R.string.toast_no_activity, 
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void _actionList(Context context){
        Utils.amLog("action list:"+_content);

        Intent  intent   = new Intent(context, ActivityMain.class);
        intent.putExtra(KEY_CONTENT, _content);
        intent.putExtra(KEY_TYPE, VALUE_TYPE_LIST);
        intent.putExtra(KEY_PATH, _path);
        context.startActivity(intent);
    }

    private void _actionDir(Context context){
        Utils.amLog("action dir:"+ _path + File.separator + _content);

        Intent  intent   = new Intent(context, ActivityMain.class);
        intent.putExtra(KEY_CONTENT, _content);
        intent.putExtra(KEY_TYPE, VALUE_TYPE_DIR);
        intent.putExtra(KEY_PATH, _path);
        context.startActivity(intent);
    }
}
