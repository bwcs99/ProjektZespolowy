package com.example.projektzespolowy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PhotoItem> items = new ArrayList<>();
    private boolean isOrderReversed = false;
    Map tags = new HashMap<String, Integer>();

    public GridView gridView = null;


    private final String ARR_TAG = "ITEMS_ARRAY";
    private final String ORDER_TAG = "CURRENT_ORDER";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.myGrid);

        isOrderReversed = loadOrder();

        loadImageData();

        if(items.size() != 0){
            sortItems();
        }

        gridView.setAdapter(new ImageAdapter(items, this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick ( AdapterView < ? > parent, View view, int position, long id ) {

                Intent intent = new Intent(getApplicationContext(), ImagesDetails.class);

                intent.putParcelableArrayListExtra(getString(R.string.PARCELABLE_TAG), items);
                startActivity(intent);


            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showAlertDialog(position);
                return false;
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sortItems();
            }
        });
    }

    public void saveImageData(ArrayList<PhotoItem> list){

        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences(getString(R.string.imagesData), MODE_PRIVATE);
        String listJSON = gson.toJson(list);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(getString(R.string.savedImages), listJSON);
        editor.apply();

    }

    public void loadImageData(){

        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences(getString(R.string.imagesData), MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<PhotoItem>>() {}.getType();

        String myJSON = sp.getString(getString(R.string.savedImages), null);

        if(myJSON != null){
            items = gson.fromJson(myJSON, type);
        }  else {
            items = new ArrayList<PhotoItem>();
        }

    }

    public void saveOrder(boolean newOrder){

        SharedPreferences sp = getSharedPreferences(getString(R.string.orderState), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(getString(R.string.orderValue), newOrder);
        editor.apply();

    }

    public boolean loadOrder(){

        SharedPreferences sp = getSharedPreferences(getString(R.string.orderState), MODE_PRIVATE);

        return sp.getBoolean(getString(R.string.orderValue), false);
    }

    private void showAlertDialog(int position){

        final boolean[] isDeleted = {false};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.message1)
                .setTitle(R.string.title1);

        builder.setPositiveButton(R.string.yes1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File fileToDelete = new File(items.get(position).getPath().trim());
                isDeleted[0] = fileToDelete.delete();
                items.remove(position);

                if(isDeleted[0]){
                    saveImageData(items);
                    changeAdapter();
                }
                Log.d("PZ", "Image successfully removed !");

            }
        });

        builder.setNegativeButton(R.string.no1, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return;

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void changeAdapter(){
        gridView.setAdapter(new ImageAdapter(items, this));
    }

    public void sortItems(){

        if(isOrderReversed){

            Collections.sort(items);
            isOrderReversed = false;
            saveOrder(isOrderReversed);
            changeAdapter();

        } else {

            Collections.sort(items, Collections.reverseOrder());
            isOrderReversed = true;
            saveOrder(isOrderReversed);
            changeAdapter();
        }

        saveOrder(isOrderReversed);

    }


    public void startPhotoActivity(View view){

        Intent photoIntent = new Intent(this, PhotoActivity.class);
        startActivityForResult(photoIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        String path = null;

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == 1){

                ArrayList<PhotoItem> temp =  null;

                String PATH_TAG = "PATHS_TO_FILES";
                temp = data.getParcelableArrayListExtra(PATH_TAG);

               if(temp != null){

                   items.addAll(temp);

               }

               saveImageData(items);

               if(gridView != null){

                   sortItems();
                   changeAdapter();
               }


            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelableArrayList(ARR_TAG, items);
        outState.putBoolean(ORDER_TAG, isOrderReversed);

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        items = savedInstanceState.getParcelableArrayList(ARR_TAG);
        isOrderReversed = savedInstanceState.getBoolean(ORDER_TAG);

        sortItems();

    }
}