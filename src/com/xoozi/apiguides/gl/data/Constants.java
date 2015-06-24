package com.xoozi.apiguides.gl.data;

public interface Constants{

    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_SHORT = 2;
    public static final int POSITION_COMPONENT_COUNT = 4;
    public static final int COLOR_COMPONENT_COUNT = 3;
    public static final int STRIDE = (POSITION_COMPONENT_COUNT 
                            + COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;
}
