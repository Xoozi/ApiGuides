package com.xoozi.apiguides.gl.objects;

import java.util.List;

import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.glutil.Geometry.Ray;
import com.xoozi.apiguides.gl.objects.ObjectBuilder.GenerateData;
import com.xoozi.apiguides.gl.objects.ObjectBuilder.DrawCommand;
import com.xoozi.apiguides.gl.programs.RayShaderProgram;

/**
 * 用来指示触摸的射线
 */
public class TouchRay {
    private static final int POSITION_COMPONENT_COUNT = 3;

    private final VertexArray _vertexArray;
    private final List<DrawCommand> _drawList;

    public TouchRay(Ray ray){
        GenerateData generatedData = ObjectBuilder.createRay(ray);
        _vertexArray = new VertexArray(generatedData.vertexData);
        _drawList   = generatedData.drawList;
    }

    public void bindData(RayShaderProgram rayProgram){
        _vertexArray.setVertexAttributePointer(0,
                rayProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        for(DrawCommand dc: _drawList){
            dc.draw();
        }
    }
}
