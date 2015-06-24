package com.xoozi.apiguides.gl.glutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;

/**
 * 用于从资源加载着色器代码的工具
 */
public class TextResourceReader{

    /**
     * 从资源读取文本
     */
    public static String readTextFileFromResource(Context context,
                                                    int resourceId){

        StringBuilder body = new StringBuilder();

        try{

            InputStream inputStream = context.getResources().
                                    openRawResource(resourceId);
            InputStreamReader inputStreamReader = 
                new InputStreamReader(inputStream);

            BufferedReader  bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while(null != (nextLine = bufferedReader.readLine())){
                body.append(nextLine);
                body.append('\n');
            }
        }catch (IOException e){
            throw new RuntimeException("Could not open resource: " + resourceId, e);
        }catch (Resources.NotFoundException nfe){
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }

        return body.toString();
    }
}
