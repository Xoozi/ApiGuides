package com.xoozi.apiguides.utils;

import android.util.Log;

public class Utils{

    private static boolean _enableLog = true;

    public static  void amLog(String msg){
        if(!_enableLog)
            return ;

        StackTraceElement dummy = Thread.currentThread().getStackTrace()[3];
        String className    = dummy.getClassName(); 
        String methodName   = dummy.getMethodName();  
        Log.w(className+"."+methodName+"()", msg);
    }


    public static void enableLog(boolean flag){
        _enableLog = flag;
    }

}
