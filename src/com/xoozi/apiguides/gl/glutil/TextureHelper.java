package com.xoozi.apiguides.gl.glutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES30.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES30.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES30.GL_LINEAR;
import static android.opengl.GLES30.glGenTextures;
import static android.opengl.GLES30.glDeleteTextures;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glTexParameteri;
import static android.opengl.GLES30.glGenerateMipmap;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper{


    public static int loadTexture(Context context, int resourceId){
        int ret = 0;
        do{
            final int[] textureObjectIds = new int[1];
            glGenTextures(1, textureObjectIds, 0);

            if(0 == textureObjectIds[0]){
                break;
            }

        
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), resourceId, options);
            if(null == bitmap){
                glDeleteTextures(1, textureObjectIds, 0);
                break;
            }

            glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

            glGenerateMipmap(GL_TEXTURE_2D);

            glBindTexture(GL_TEXTURE_2D, 0);

            ret = textureObjectIds[0];
        }while(false);

        return ret;
    }
}
