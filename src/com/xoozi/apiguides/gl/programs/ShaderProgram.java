package com.xoozi.apiguides.gl.programs;

import static android.opengl.GLES30.glUseProgram;

import com.xoozi.apiguides.gl.glutil.ShaderHelper;
import com.xoozi.apiguides.gl.glutil.TextResourceReader;

import android.content.Context;



public abstract class ShaderProgram{

    // Uniform constants
    protected static final String U_MATRIX              = "u_Matrix";
    protected static final String U_TEXTURE_UNIT        = "u_TextureUnit";
    protected static final String U_COLOR               = "u_Color";
    protected static final String U_TIME                = "u_Time";

    // Attribute constants
    protected static final String A_DIRECTION_VECTOR    = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
    protected static final String A_POSITION            = "a_Position";
    protected static final String A_COLOR               = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int     _program;
    protected final boolean _valid;

    protected ShaderProgram(Context context, int vertexShaderResId,
                            int fragmentShaderResId){

        _program = ShaderHelper.buildProgram(
                        TextResourceReader.readTextFileFromResource(
                            context, vertexShaderResId),
                        TextResourceReader.readTextFileFromResource(
                            context, fragmentShaderResId));

        _valid = (0 != _program);
    }


    public void useProgram(){

        // Set the current OpenGL shader program to this program.
        if(_valid)
            glUseProgram(_program);
    }

    public int getProgram(){
        return _program;
    }

    public boolean isValid(){
        return _valid;
    }

    public int getPositionAttributeLocation(){
        return 0;
    }

    public int getColorAttributeLocation(){
        return 0;
    }
}
