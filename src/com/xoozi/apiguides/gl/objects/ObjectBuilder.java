package com.xoozi.apiguides.gl.objects;

import static  android.util.FloatMath.cos;
import static  android.util.FloatMath.sin;

import java.util.ArrayList;
import java.util.List;

import com.xoozi.apiguides.utils.Utils;
import com.xoozi.apiguides.gl.glutil.Geometry.Circle;
import com.xoozi.apiguides.gl.glutil.Geometry.Cube;
import com.xoozi.apiguides.gl.glutil.Geometry.Cylinder;
import com.xoozi.apiguides.gl.glutil.Geometry.Point;
import com.xoozi.apiguides.gl.glutil.Geometry.Ray;
import com.xoozi.apiguides.gl.glutil.Geometry.Vector;

import static android.opengl.GLES30.GL_POINTS;
import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_LINE_LOOP;
import static android.opengl.GLES30.GL_TRIANGLE_FAN;
import static android.opengl.GLES30.GL_TRIANGLE_STRIP;
import static android.opengl.GLES30.glDrawArrays;


/**
 * 完成简单几何模型顶点的创建
 * 及绘制操作的定义
 */
public class ObjectBuilder{
    private static final int            FLOATS_PER_VERTEX = 3;
    private final   float[]             _vertexData;
    private final   List<DrawCommand>   _drawList = new ArrayList<DrawCommand>();
    private int                         _offset = 0;


    static GenerateData createPoint(Vector vector){
        ObjectBuilder builder = new ObjectBuilder(1);
        builder._appendPoint(vector);
        return builder.build();
    }

    /**
     * 生成射线顶点数据
     */
    static GenerateData createRay(Ray ray){
        ObjectBuilder builder = new ObjectBuilder(4);
        builder._appendRay(ray);
        return builder.build();
    }


    static GenerateData createCube(Cube cube){
        ObjectBuilder builder = new ObjectBuilder(18);
        builder._appendCube(cube);
        return builder.build();
    }

    /**
     * 生成冰球的顶点数据
     */
    static GenerateData createPuck(Cylinder puck, int numPoints){
        int size = _sizeOfCircleInVertices(numPoints) +
                    _sizeofOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);

        Circle puckTop = new Circle(puck.center.translateY(puck.height / 2f),
                                    puck.radius);
        
        builder._appendCircle(puckTop, numPoints);
        builder._appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    /**
     * 生成击球柄顶点数据
     */
    static  GenerateData createMallet(Point center, float radius, float height, int numPoints){
        int size = _sizeOfCircleInVertices(numPoints) * 2 +
                    _sizeofOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        float baseHeight = height * 0.25f;


        Circle baseCircle = new Circle(
            center.translateY(-baseHeight),
            radius);

        Cylinder baseCylinder = new Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f),
                radius, baseHeight);

        builder._appendCircle(baseCircle, numPoints);
        builder._appendOpenCylinder(baseCylinder, numPoints);

        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(
                center.translateY(height * 0.5f),
                handleRadius);

        Cylinder handleCylinder = new Cylinder(
                handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius, handleHeight);

        builder._appendCircle(handleCircle, numPoints);
        builder._appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    private void _appendPoint(Vector vector){

        _vertexData[_offset++] = vector.x;
        _vertexData[_offset++] = vector.y;
        _vertexData[_offset++] = vector.z;

        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_POINTS,0, 1);
            }
        });
    }


    /**
     *  生成一个射线的顶点数据，将它附加到顶点数据集
     */
    private void _appendRay(Ray ray){
        final int rayStartVertex = _offset / FLOATS_PER_VERTEX;

        Point from = ray.point;
        Point to = from.translate(ray.vector);

        Utils.amLog(String.format("from:(%f, %f, %f)", from.x, from.y, from.z));
        Utils.amLog(String.format("to:(%f, %f, %f)", to.x, to.y, to.z));

        _vertexData[_offset++] = from.x;
        _vertexData[_offset++] = from.y;
        _vertexData[_offset++] = from.z;

        _vertexData[_offset++] = to.x;
        _vertexData[_offset++] = to.y;
        _vertexData[_offset++] = to.z;

        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_LINES, rayStartVertex, 2);
            }
        });

        final int pointsStartVertex = _offset / FLOATS_PER_VERTEX;

        _vertexData[_offset++] = from.x;
        _vertexData[_offset++] = from.y;
        _vertexData[_offset++] = from.z;

        _vertexData[_offset++] = to.x;
        _vertexData[_offset++] = to.y;
        _vertexData[_offset++] = to.z;

        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_POINTS, pointsStartVertex, 2);
            }
        });
    }

    /**
     *  生成一个立方体的顶点数据，将它附加到顶点数据集
     */
    private void _appendCube(Cube cube){

        Point center = cube.center;
        float size = cube.size/2;
        final int _0StartVertex = _offset / FLOATS_PER_VERTEX;
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z + size;
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z + size;
        
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z + size;
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z - size;

        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z + size;
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z + size;
        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_LINES, _0StartVertex, 6);
            }
        });

        final int _6StartVertex = _offset / FLOATS_PER_VERTEX;
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z - size;
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z + size;
        
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z - size;
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z - size;

        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z - size;
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z - size;
        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_LINES, _6StartVertex, 6);
            }
        });

        final int loopStartVertex = _offset / FLOATS_PER_VERTEX;
        //3
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z + size;

        //4
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z + size;

        //7
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y + size;
        _vertexData[_offset++] = center.z - size;

        //5
        _vertexData[_offset++] = center.x - size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z - size;

        //2
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z - size;

        //1
        _vertexData[_offset++] = center.x + size;
        _vertexData[_offset++] = center.y - size;
        _vertexData[_offset++] = center.z + size;

        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_LINE_LOOP, loopStartVertex, 6);
            }
        });
    }


    /**
     * 生成一个圆的顶点数据，将它附加到顶点数据集
     */
    private void _appendCircle(Circle c, int numPoints){
        final int startVertex = _offset / FLOATS_PER_VERTEX;
        final int numVertices = _sizeOfCircleInVertices(numPoints);
        float centerX, centerY, centerZ, radius;
        centerX = c.center.x;
        centerY = c.center.y;
        centerZ = c.center.z;
        radius  = c.radius;

        _vertexData[_offset++] = centerX;
        _vertexData[_offset++] = centerY;
        _vertexData[_offset++] = centerZ;

        final float step = ((float)1 / (float) numPoints) 
                            * ((float) Math.PI * 2f);
        for(int i = 0; i <= numPoints; i++){
            float radians = step * i;

            _vertexData[_offset++] = 
                centerX + radius *  cos(radians);
            _vertexData[_offset++] = centerY;
            _vertexData[_offset++] = 
                centerZ + radius * sin(radians);
        }

        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    /**
     * 生成一个开口圆柱壁的顶点数据
     * 将它附加到顶点数据集
     */
    private void _appendOpenCylinder(Cylinder cy, int numPoints){
        final int startVertex = _offset / FLOATS_PER_VERTEX;
        final int numVertices = _sizeofOpenCylinderInVertices(numPoints);
        final float xCenter = cy.center.x;
        final float yCenter = cy.center.y;
        final float zCenter = cy.center.z;
        final float radius  = cy.radius;
        final float height  = cy.height;
        final float yStart  = yCenter - (height / 2f);
        final float yEnd    = yCenter + (height / 2f);
        final float step = ((float)1 / (float) numPoints) 
                            * ((float) Math.PI * 2f);
        for(int i = 0; i <= numPoints; i++){
            float radians = step * i;
            float xPosition = xCenter + radius * cos(radians);
            float zPosition = zCenter + radius * sin(radians);

            _vertexData[_offset++] = xPosition;
            _vertexData[_offset++] = yStart;
            _vertexData[_offset++] = zPosition;

            _vertexData[_offset++] = xPosition;
            _vertexData[_offset++] = yEnd;
            _vertexData[_offset++] = zPosition;
        }

        _drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }


    private ObjectBuilder(int sizeInVertices){
        _vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    /**
     * 计算圆的顶点数
     * 设置圆由n边形逼近时
     * 实际需要定义的顶点为n个
     * 加上中间的 TRIANGLE_FAN 的起点
     * 以及周围的最后一点
     * 一共1 + n + 1
     */
    private static int _sizeOfCircleInVertices(int numPoints){
        return 1 + (numPoints + 1);
    }

    /**
     * 计算开口圆柱壁的顶点数
     * 这些顶点组织方式为TRIANGLE_STRIP
     * 三角形带由上下两个圆周上的点围成
     * 圆周上的点首尾相接，其数量为n + 1
     * 而圆周有两个，所以总数(n + 1) * 2
     */
    private static int _sizeofOpenCylinderInVertices(int numPoints){
        return (numPoints + 1) * 2;
    }

    /**
     * 生成顶点数据
     */
    private GenerateData build(){
        return new GenerateData(_vertexData, _drawList);
    }



    /**
     * 绘制命令 封装动作开始顶点序号和绘制跨度
     */
    static interface DrawCommand{
        void draw();
    }

    /**
     * 生成的顶点数据和绘制列表
     */
    static class GenerateData{
        final float[]           vertexData;
        final List<DrawCommand> drawList;

        GenerateData(float[] vertexData, List<DrawCommand> drawList){
            this.vertexData = vertexData;
            this.drawList   = drawList;
        }
    }
}
