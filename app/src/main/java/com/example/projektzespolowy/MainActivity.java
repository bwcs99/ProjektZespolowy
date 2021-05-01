package com.example.projektzespolowy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String PATH_TAG = "PATHS_TO_FILES";

    private ArrayList<String> paths = new ArrayList<>();
    Map tags = new HashMap<String, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.myGrid);
        gridView.setAdapter(new ImageAdaptor(paths, this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick ( AdapterView < ? > parent, View view, int position, long id ) {

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    public void reverseSort(View view){
    }


    public void startPhotoActivity(View view){

        Intent photoIntent = new Intent(this, PhotoActivity.class);
        startActivityForResult(photoIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path = null;

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == 1){

                ArrayList<String> temp =  null;

                 temp = data.getStringArrayListExtra(PATH_TAG);

               if(temp != null){

                   for(String p : temp){
                       paths.add(p);
                   }

                   Toast.makeText(this, "Received not null path", Toast.LENGTH_LONG).show();

               }


            }
        }
    }

}