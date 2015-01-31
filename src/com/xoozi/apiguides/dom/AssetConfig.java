package com.xoozi.apiguides.dom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;

public class AssetConfig{

    public static final String CONFIG_FOLDER    = "config";

    public static ConfigDir loadRoot(Context context) 
        throws IOException, ParserConfigurationException, SAXException {
        return loadDir(context, CONFIG_FOLDER);
    }

    public static ConfigDir loadDir(Context context, String path) 
        throws IOException,ParserConfigurationException,SAXException {
        return new ConfigDir(context, path);
    }

    public static DOMList loadList(Context context, String path,String content)
            throws IOException, ParserConfigurationException,SAXException {

      DOMList ret = null;

        AssetManager am =  context.getAssets();

        InputStream isConfig = am.open(path + File.separator + content);

        
        ret = new DOMList(isConfig, path);

        isConfig.close();

        return ret;
    }

}
