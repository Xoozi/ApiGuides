package com.xoozi.apiguides.ble;

import java.util.ArrayList;
import java.util.List;



/**
 * 高斯滤波工具
 */
public class GaussFilter{

    private List<Double>    _list = new ArrayList<Double>();


    /**
     * 输入一次采样 返回滤波后的结果，如果结果不理想返回负无穷大
     */
    public double    addSample(double sample){
        double u = 0, o = 0, av = 0;
        int c = 0;
        int count = _list.size();
        _list.add(sample);

        for(double d:_list){
            u += d;
        }
        u = u / count;

        for(int i = 0; i < count; i++){
            o += Math.pow((_list.get(i) - u), 2);
        }
        o = Math.sqrt(o/count);


        for(int i = 0; i < count; i++){
            double t = _list.get(i);
            if(t > (u-2*o) && t < (u+2*o)){
                c ++;
                av += t;
            }
        }

        if(0 == c){
            return Double.NEGATIVE_INFINITY;
        }else{
            return av/c;
        }
    }

    /**
     * 返回采样次数
     */
    public int getCount(){
        return _list.size();
    }

    /**
     * 重置
     */
    public void reset(){
        _list.clear();
    }
}
