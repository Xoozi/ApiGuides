package com.xoozi.apiguides.dom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;

public class ConfigDir{
    public static final String CONFIG_INDEX     = "index.xml";
    private DOMList _indexList;

    ConfigDir(Context context, String path) 
        throws IOException, ParserConfigurationException, SAXException {
                
        AssetManager am =  context.getAssets();

        InputStream isConfig = am.open(path + File.separator + CONFIG_INDEX);

        
        _indexList = new DOMList(isConfig, path);

        isConfig.close();
    }

    public DOMList getIndexList(){
        return _indexList;
    }
}
