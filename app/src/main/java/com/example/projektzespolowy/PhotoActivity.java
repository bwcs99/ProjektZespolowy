package com.example.projektzespolowy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    public String takenPicture;
    private String PATH_TAG = "PATHS_TO_FILES";
    private String PARCELABLE_TAG = "PARCELABLE";
    private Camera camera = null;
    public String pathToFile;
    private CustomPreview preview = null;
    public ArrayList<PhotoItem> photoItems = new ArrayList<>();

    private Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = createImageFile();

            if(pictureFile == null){
                Log.d("Image File Creation", "Error during creation of image file");
                return;
            }

            pathToFile = pictureFile.getAbsolutePath();
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            String currDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            PhotoItem newPhoto = new PhotoItem(currDate, pathToFile, Long.parseLong(timeStamp));
            photoItems.add(newPhoto);

            try{

                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

            } catch(FileNotFoundException e){

                Log.d("File Not Found", "File not found !");

            } catch(IOException e){

                Log.d("Save Fail !", "Failed saving image");
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Button captureButton = (Button) findViewById(R.id.triggerButton);

        camera = getCameraInstance();
        preview = new CustomPreview(this, camera);

        FrameLayout previewBox = (FrameLayout) findViewById(R.id.myFrame);
        previewBox.addView(preview);

        captureButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, picture);
                    }
                }
        );
    }

    @Override
    protected  void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera(){

        if(camera != null){
            camera.release();
            camera = null;
        }
    }

    private Camera getCameraInstance(){

        Camera c = null;

        try{
            c = Camera.open();
        } catch (Exception e){
            c = null;
        }

        return c;
    }

    public void returnToMain(View view){

        Intent finishIntent = new Intent();

        finishIntent.putParcelableArrayListExtra(PATH_TAG, photoItems);
        setResult(Activity.RESULT_OK, finishIntent);

        finish();

    }

    private File createImageFile(){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File myDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            return File.createTempFile("JPEG_" + timeStamp.toString(), ".jpg", myDirectory);
        } catch(IOException e){
            return null;
        }

    }
}