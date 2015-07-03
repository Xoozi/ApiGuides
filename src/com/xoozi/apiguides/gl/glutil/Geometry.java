package com.xoozi.apiguides.gl.glutil;

import static  android.util.FloatMath.sqrt;
import static android.opengl.Matrix.multiplyMV;
/**
 * 一些几何工具
 */
public class Geometry{

    /**
     * 以起点from和终点to构造向量
     */
    public static Vector    vectorBetween(Point from, Point to){
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    /**
     *计算射线（所在直线）到某点间的距离
     */
    public static float     distanceBetween(Point point, Ray ray){
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);


        /**
         * 两个矢量的叉积的长度数值上等于两个矢量确定的三角形面积的两倍
         * 根据三角形面积公式 S=(L*H)/2，H=2S/L
         */
        float areaOfTriangleX2 = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();
        float distance = areaOfTriangleX2 / lengthOfBase;
        return  distance;
    }

    /**
     * 判断射线（所在直线）与球体是否相交
     */
    public static boolean   intersects(Sphere sphere, Ray ray){
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }


    /**
     * 求射线与平面的交点
     * 具体的做法是，只要射线和平面不是平行的
     * 那么将射线的起点，按射线方向移动（标乘），必然与平面相交
     * 计算焦点的关键就是计算需要移动的距离（标乘的因数）
     * 这里通过计算自射线起点到平面定义点的向量与平面法向量点积
     * 与  射线向量与平面法向量的点积  的比例来得到标乘的因数
     */
    public static Point     intersectionPoint(Ray ray, Plane plane){
        Vector rayToPlanVector = vectorBetween(ray.point, plane.point);

        float scaleFactor = rayToPlanVector.dotProduct(plane.normal) /
                            ray.vector.dotProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }
    
    public static class Point{
        public final float x, y, z;

        public Point(float[] vec){
            this.x = vec[0];
            this.y = vec[1];
            this.z = vec[2];
        }

        public Point(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translate(Vector vector){
            return new Point(x + vector.x,
                         y + vector.y,
                         z + vector.z);
        }

        public Point translateY(float distance){
            return new Point(x, y + distance, z);
        }
    }

    public static class Circle{
        public final Point   center;
        public final float   radius;

        public Circle(Point center, float radius){
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale){
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder{
        public final Point  center;
        public final float  radius;
        public final float  height;

        public Cylinder(Point center, float radius, float height){
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
   }

   public static class Sphere{
        public final Point  center;
        public final float  radius;
        
        public Sphere(Point center, float radius){
            this.center = center;
            this.radius = radius;
        }
   }

   public static class Ray{
        public final Point  point;
        public final Vector vector;

        public Ray(Point point, Vector vector){
            this.point = point;
            this.vector= vector;
        }
   }

   public static class Vector{
        public final float x, y, z;
        public Vector(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length(){
            return sqrt(x*x +
                        y*y +
                        z*z);
        }

        public Vector multiplyMatrix(float[] matrix){
            float [] v = new float[4];
            float [] ret = new float[4];
            v[0] = x;
            v[1] = y;
            v[2] = z;
            v[3] = 1;

            multiplyMV(ret, 0, matrix, 0, v, 0);
            return new Vector(ret[0], ret[1], ret[2]);
        }

        public Vector crossProduct(Vector other){
            return new Vector(
                    (y*other.z) - (z*other.y),
                    (z*other.x) - (x*other.z),
                    (x*other.y) - (y*other.x));
        }

        public Vector add(Vector other){
            return new Vector(x+other.x, y+other.y, z+other.z);
        }

        public float  dotProduct(Vector other){
            return x * other.x
                + y * other.y
                + z * other.z;
        }

        public Vector scale(float f){
            return new Vector(
                    x * f,
                    y * f,
                    z * f);
        }
   }

   public static class Plane{
        public final Point  point;
        public final Vector normal;

        public Plane(Point point, Vector normal){
            this.point = point;
            this.normal= normal;
        }
   }

   public static class Cube{
       public final Point   center;
       public final float   size;

       public Cube(Point center, float size){
            this.center = center;
            this.size   = size;
       }
   }
}
