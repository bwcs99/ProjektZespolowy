package com.example.projektzespolowy;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;

public class CustomPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder myHolder;
    private Camera myCamera;

    public CustomPreview(Context context, Camera camera){
        super(context);

        myCamera = camera;

        myHolder = getHolder();
        myHolder.addCallback(this);
        myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        try{
            myCamera.setPreviewDisplay(holder);
            myCamera.startPreview();
        } catch (IOException e){
            Log.d("Surface Creation", "Error during surface creation");
        }

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        if(myHolder.getSurface() == null){
            return;
        }

        try{
            myCamera.stopPreview();
        } catch (Exception e){

        }

        try{
            myCamera.setPreviewDisplay(myHolder);
            myCamera.startPreview();

        } catch(Exception e){
            Log.d("Starting Preview", "Error starting camera preview");
        }

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
