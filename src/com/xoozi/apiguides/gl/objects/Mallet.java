package com.xoozi.apiguides.gl.objects;

import java.util.List;
import com.xoozi.apiguides.gl.data.VertexArray;
import com.xoozi.apiguides.gl.glutil.Geometry.Point;
import com.xoozi.apiguides.gl.objects.ObjectBuilder.GenerateData;
import com.xoozi.apiguides.gl.objects.ObjectBuilder.DrawCommand;
import com.xoozi.apiguides.gl.programs.ColorShaderProgram;

public class Mallet{
    private static final int POSITION_COMPONENT_COUNT = 3;
    public final float radius, height;

    private final VertexArray       _vertexArray;
    private final List<DrawCommand> _drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet){
        GenerateData generatedData = ObjectBuilder.createMallet(
                new Point(0f, 0f, 0f), radius, height,
                numPointsAroundMallet);
        this.radius = radius;
        this.height = height;

        _vertexArray = new VertexArray(generatedData.vertexData);
        _drawList   = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram){
        _vertexArray.setVertexAttributePointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(){
        for(DrawCommand dc: _drawList){
            dc.draw();
        }
    }
}
