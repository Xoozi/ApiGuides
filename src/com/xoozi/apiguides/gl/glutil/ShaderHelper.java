package com.xoozi.apiguides.gl.glutil;

import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_COMPILE_STATUS;
import static android.opengl.GLES30.GL_LINK_STATUS;
import static android.opengl.GLES30.GL_VALIDATE_STATUS;
import static android.opengl.GLES30.glCreateShader;
import static android.opengl.GLES30.glShaderSource;
import static android.opengl.GLES30.glCompileShader;
import static android.opengl.GLES30.glGetShaderiv;
import static android.opengl.GLES30.glGetShaderInfoLog;
import static android.opengl.GLES30.glDeleteShader;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glAttachShader;
import static android.opengl.GLES30.glLinkProgram;
import static android.opengl.GLES30.glGetProgramInfoLog;
import static android.opengl.GLES30.glGetProgramiv;
import static android.opengl.GLES30.glDeleteProgram;
import static android.opengl.GLES30.glValidateProgram;

import com.xoozi.apiguides.utils.Utils;

/**
 * 着色器工具
 */
public class ShaderHelper{


    public static int buildProgram(String vertexShaderSrc,
                                   String fragmentShaderSrc){

        int program = 0;
        int vertexShaderId = 0;
        int fragmentShaderId = 0;

        do{
            vertexShaderId = _compileVertexShader(vertexShaderSrc);
            fragmentShaderId = _compileFragmentShader(fragmentShaderSrc);

            if(0 == vertexShaderId){
                Utils.amLog("Compile VertexShader failed!");
                if(0 != fragmentShaderId)
                    glDeleteShader(fragmentShaderId);

                break;
            }

            if(0 == fragmentShaderId){
                Utils.amLog("Compile FragmentShader failed!");
                if(0 != vertexShaderId)
                    glDeleteShader(vertexShaderId);
                break;
            }

            program = _linkProgram(vertexShaderId, fragmentShaderId);
            if(!_validateProgram(program)){
                if(0 != vertexShaderId)
                    glDeleteShader(vertexShaderId);

                if(0 != fragmentShaderId)
                    glDeleteShader(fragmentShaderId);
                program = 0;
            }

        }while(false);

        return program;
    }

    /**
     * 着色器编译
     */
    private static int _compileVertexShader(String shaderCode){
        
        return _compileShader(GL_VERTEX_SHADER, shaderCode);
    }
    private static int _compileFragmentShader(String shaderCode){
        return _compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }
    private static int _compileShader(int type, String shaderCode){

        //创建着色器对象
        int shaderObjectId = glCreateShader(type);

        do{
            if(0 == shaderObjectId){
                Utils.amLog("Could not create new shader.");
                break;
            }

            //上传着色器代码
            glShaderSource(shaderObjectId, shaderCode);

            //编译
            glCompileShader(shaderObjectId);

            //检查编译结果
            final int[] compileStatus = new int[1];
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

            Utils.amLog("Results of compiling source:" + "\n" + 
                    shaderCode + "\n:" + 
                    glGetShaderInfoLog(shaderObjectId));

            if(0 == compileStatus[0]){
                glDeleteShader(shaderObjectId);
                shaderObjectId = 0;

                Utils.amLog("Compilation of shader failed.");
                break;
            }


        }while(false);

        return shaderObjectId;
    }


    /**
     * 着色器链接
     */
    private static int _linkProgram(int vertexShaderId, int fragmentShaderId){

        //新建着色器程序
        int programObjectId = glCreateProgram();

        do{

            if(0 == programObjectId){
                Utils.amLog("Could not create new program");
                break;
            }

            //将编译好的着色器附着到着色器程序
            glAttachShader(programObjectId, vertexShaderId);
            glAttachShader(programObjectId, fragmentShaderId);

            //链接并检查结果
            glLinkProgram(programObjectId);
            final int [] linkStatus = new int[1];
            glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
            Utils.amLog("Result of linking program:\n" + 
                    glGetProgramInfoLog(programObjectId));
            if(0 == linkStatus[0]){
                glDeleteProgram(programObjectId);
                programObjectId = 0;
                Utils.amLog("Linking of program failed.");
                break;
            }
        }while(false);

        return programObjectId;
    }

    private static boolean _validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        Utils.amLog("Result of validating program: " + validateStatus[0] +
                "\nLog:" + glGetProgramInfoLog(programObjectId));

        return 0 != validateStatus[0];
    }
}
