package com.example.liushanpu.mycamerademo;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

/**
 * Created by liushanpu on 18/3/30.
 */

public class CustomCamera extends Activity implements SurfaceHolder.Callback{

    private static final String CUSTOM_TAG = "CustomCamera";

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        mPreview = (SurfaceView) findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
            if (mCamera == null) {
                android.util.Log.d(CUSTOM_TAG, "the mCamera is null in onResume");
                return;
            }
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    public void capture(View view) {
    }

    /**
     * To get the Camera object
     * @return
     */
    private Camera getCamera() {
        //We can find Camera is deprecated, it is suggest use Camera2 to instead of it.
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            android.util.Log.d(CUSTOM_TAG, "open camera is failure");
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * Start preview the camera picture
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //Change the orientation from landscape to portrait
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * release the camera resource
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
