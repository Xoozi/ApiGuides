package com.xoozi.apiguides.gl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;

/**
 * 为便于使用，封装一下相机接口
 */
public class CameraInterface {
    private Camera                  _camera;
    private Camera.Parameters       _params;
    private boolean                 _isPreviewing = false;
    private static CameraInterface  _cameraInterface;

    public interface CamOpenOverCallback{
        public void cameraHasOpened();
    }

    private CameraInterface(){
    }

    public static synchronized CameraInterface getInstance(){
        if(_cameraInterface == null){
            _cameraInterface = new CameraInterface();
        }
        return _cameraInterface;
    }

    /**
     * 打开相机
     */
    public void doOpenCamera(CamOpenOverCallback callback){
        if(_camera == null){
            _camera = Camera.open();
            if(callback != null){
                callback.cameraHasOpened();
            }
        }else{
            doStopCamera();
        }
    }

    /**
     * 开始预览
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate){
        if(_isPreviewing){
            _camera.stopPreview();
            return;
        }
        if(_camera != null){
            try {
                _camera.setPreviewTexture(surface);
            } catch (IOException e) {
                e.printStackTrace();
            }
            _initCamera(previewRate);
        }
    }

    /**
     * 关闭并释放相机
     */
    public void doStopCamera(){
        if(null != _camera)
        {
            _camera.setPreviewCallback(null);
            _camera.stopPreview(); 
            _isPreviewing = false; 
            _camera.release();
            _camera = null;     
        }
    }


    /**
     * 是否正在预览
     */
    public boolean isPreviewing(){
        return _isPreviewing;
    }


    /**
     * 获取支持的最大预览尺寸
     */
    private Size _getSupportedMaxPreviewSize(Camera.Parameters params){
        List<Size> sizeList =  params.getSupportedPreviewSizes();
        Collections.sort(sizeList, new CameraSizeComparator());

        return sizeList.get(sizeList.size()-1);
    }


    /**
     * 初始化相机
     */
    private void _initCamera(float previewRate){
        if(_camera != null){

            _params = _camera.getParameters();
            Size previewSize = _getSupportedMaxPreviewSize(_params);
            _params.setPreviewSize(previewSize.width, previewSize.height);

            _camera.setDisplayOrientation(90);

            List<String> focusModes = _params.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
                _params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            _camera.setParameters(_params); 
            _camera.startPreview();

            _isPreviewing = true;
            _params = _camera.getParameters();
        }
    }

    /**
     * 预览屏幕尺寸比较器
     */
    private   class CameraSizeComparator implements Comparator<Camera.Size>{
        public int compare(Size lhs, Size rhs) {
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }
    }
}
