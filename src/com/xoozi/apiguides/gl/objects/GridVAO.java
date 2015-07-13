package com.xoozi.apiguides.gl.objects;

import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_UNSIGNED_SHORT;
import static android.opengl.GLES30.glDrawElements;

import com.xoozi.apiguides.gl.data.VertexArrayObject;
import com.xoozi.apiguides.gl.programs.ShaderProgram;

public class GridVAO {

    private static final int XDimension = 400;
    private static final int ZDimension = 400;
    private static final int XDivision  = 100;
    private static final int ZDivision  = 100;

    private VertexArrayObject   _vao;
    private final int           _indexNum;

    public GridVAO(ShaderProgram program){
        this(program, XDimension, ZDimension,
                      XDivision,  ZDivision);
    }

    public GridVAO(ShaderProgram program,
                    int XDim, int ZDim,
                    int XDiv, int ZDiv){

        // Calculate total vertices and indices.
        int vertexNum        = 3 * (XDiv+1) * (ZDiv+1) * 2;
        _indexNum            = ((XDiv+1) + (ZDiv+1)) * 2;
        
        // Allocate required space for vertex and indice location.
        float[] gridVertex     = new float[ vertexNum ];
        short[] gridIndices   = new short[ _indexNum ];
        
        // Store unit division interval and half length of dimension.
        float xInterval       = XDim/XDiv;
        float zInterval       = ZDim/ZDiv;
        float xHalf           = XDim/2;
        float zHalf           = ZDim/2;
        int i                   = 0;

        // Assign vertices along X-axis.
        for( int j=0; j<XDiv+1; j++){
            gridVertex[i++] = j*xInterval - xHalf; gridVertex[i++] = 0.0f; gridVertex[i++] =  zHalf;
            gridVertex[i++] = j*xInterval - xHalf; gridVertex[i++] = 0.0f; gridVertex[i++] = -zHalf;
        }

        // Assign vertices along Z-axis.
        for( int j=0; j<ZDiv+1; j++){
            gridVertex[i++] = -xHalf; gridVertex[i++] = 0.0f; gridVertex[i++] = j*zInterval - zHalf;
            gridVertex[i++] = xHalf;  gridVertex[i++] = 0.0f; gridVertex[i++] = j*zInterval - zHalf;
        }

        i = 0;
        // Assign indices along X-axis.
        for( int j=0; j<XDiv+1; j++ ){
            gridIndices[i++] = (short)(2*j);
            gridIndices[i++] = (short)(2*j+1);
        }
        // Assign indices along Z-axis.
        for( int j=0; j<ZDiv+1; j++ ){
            gridIndices[i++] = (short)(((XDiv+1)*2) + (2*j));
            gridIndices[i++] = (short)(((XDiv+1)*2) + (2*j+1));
        }

        _vao = new VertexArrayObject(program, gridVertex, gridIndices);
    }

    public void draw(){
        _vao.bindData();
        glDrawElements(GL_LINES, _indexNum, GL_UNSIGNED_SHORT, 0);
    }
}
